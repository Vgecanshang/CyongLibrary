package com.cy.cylibrary.DynamicPermission;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.subjects.PublishSubject;

/**
 * 用于申请权限的Fragment
 * @author cy
 * @date 2018/11/26
 */
public class PermissionsFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST_CODE = 42;
    /**
     * 安装其他应用权限(Android8.0系统)
     */
    public static final int TYPE_REQUEST_INSTALL_PACKAGES = 105;
    // Contains all the current permission requests.
    // Once granted or denied, they are removed from it.
    private Map<String, PublishSubject<Permission>> mSubjects = new HashMap<>();
    private boolean mLogging;

    public PermissionsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @TargetApi(Build.VERSION_CODES.M)
    void requestPermissions(@NonNull String[] permissions) {
        requestPermissions(permissions, PERMISSIONS_REQUEST_CODE);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != PERMISSIONS_REQUEST_CODE) return;

        boolean[] shouldShowRequestPermissionRationale = new boolean[permissions.length];

        for (int i = 0; i < permissions.length; i++) {
            shouldShowRequestPermissionRationale[i] = shouldShowRequestPermissionRationale(permissions[i]);
        }

        onRequestPermissionsResult(permissions, grantResults, shouldShowRequestPermissionRationale);
    }

    void onRequestPermissionsResult(String permissions[], int[] grantResults, boolean[] shouldShowRequestPermissionRationale) {
        for (int i = 0, size = permissions.length; i < size; i++) {
            log("onRequestPermissionsResult  " + permissions[i]);
            // Find the corresponding subject
            PublishSubject<Permission> subject = mSubjects.get(permissions[i]);
            if (subject == null) {
                // No subject found
                Log.e(PermissionUtil.TAG, "RxPermissions.onRequestPermissionsResult invoked but didn't find the corresponding permission request.");
                return;
            }

            boolean granted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && permissions[i].equals(Manifest.permission.REQUEST_INSTALL_PACKAGES)) {
                granted = getActivity().getPackageManager().canRequestPackageInstalls();
            }
            if(!granted && permissions[i].equals(Manifest.permission.REQUEST_INSTALL_PACKAGES)){
                //Android8.0的系统无安装权限需要跳转至安装未知应用界面。
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                startActivityForResult(intent, TYPE_REQUEST_INSTALL_PACKAGES);
            }else{
                mSubjects.remove(permissions[i]);
                subject.onNext(new Permission(permissions[i], granted, shouldShowRequestPermissionRationale[i]));
                subject.onComplete();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TYPE_REQUEST_INSTALL_PACKAGES){
            PublishSubject<Permission> subject = mSubjects.get(Manifest.permission.REQUEST_INSTALL_PACKAGES);
            mSubjects.remove(Manifest.permission.REQUEST_INSTALL_PACKAGES);
            boolean granted = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                granted = getActivity().getPackageManager().canRequestPackageInstalls();
            }
            subject.onNext(new Permission(Manifest.permission.REQUEST_INSTALL_PACKAGES, granted, shouldShowRequestPermissionRationale(Manifest.permission.REQUEST_INSTALL_PACKAGES)));
            subject.onComplete();
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    boolean isGranted(String permission) {
        final FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity == null) {
            throw new IllegalStateException("This fragment must be attached to an activity.");
        }
        return fragmentActivity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(Build.VERSION_CODES.M)
    boolean isRevoked(String permission) {
        final FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity == null) {
            throw new IllegalStateException("This fragment must be attached to an activity.");
        }
        return fragmentActivity.getPackageManager().isPermissionRevokedByPolicy(permission, getActivity().getPackageName());
    }

    public void setLogging(boolean logging) {
        mLogging = logging;
    }

    public PublishSubject<Permission> getSubjectByPermission(@NonNull String permission) {
        return mSubjects.get(permission);
    }

    public boolean containsByPermission(@NonNull String permission) {
        return mSubjects.containsKey(permission);
    }

    public void setSubjectForPermission(@NonNull String permission, @NonNull PublishSubject<Permission> subject) {
        mSubjects.put(permission, subject);
    }

    void log(String message) {
        if (mLogging) {
            Log.d(PermissionUtil.TAG, message);
        }
    }

}
