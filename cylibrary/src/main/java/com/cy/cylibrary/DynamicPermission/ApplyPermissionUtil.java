package com.cy.cylibrary.DynamicPermission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import com.cy.cylibrary.utils.CLogger;

/**
 * Created by cy on 2018/5/31.
 */
public class ApplyPermissionUtil {

    public static final String TAG = "ApplyPermissionUtil";

    public static int PAGE_TYPE_FRAGMENT = 201;
    public static int PAGE_TYPE_ACTIVITY = 202;

    /**
     * 读取联系人权限
     */
    public static final int TYPE_REQUEST_READ_CONTACT = 100;
    /**
     * 位置定位权限
     */
    public static final int TYPE_REQUEST_READ_LOCATION = 101;
    /**
     * 相机权限
     */
    public static final int TYPE_REQUEST_USE_CAMERA = 102;
    /**
     * 相册权限
     */
    public static final int TYPE_REQUEST_USE_PHOTO = 103;
    /**
     * 读写文件权限
     */
    public static final int TYPE_EXTERNAL_STORAGE = 104;

    /**
     * 安装其他应用权限(Android8.0系统)
     */
    public static final int TYPE_REQUEST_INSTALL_PACKAGES = 105;

    /** Activity实例 */
    private Context context = null;
    /** Fragment实例 */
    private Fragment fragment = null;
    /** 标记请求动态权限的是Fragment还是Activity */
    private int pageType = PAGE_TYPE_ACTIVITY;
    private RequestPermissionsListener requestPermissionsListener = null;

    public ApplyPermissionUtil(Context context, RequestPermissionsListener requestPermissionsListener) {
        this.context = context;
        this.pageType = PAGE_TYPE_ACTIVITY;
        this.requestPermissionsListener = requestPermissionsListener;
    }


    public ApplyPermissionUtil(Fragment fragment, RequestPermissionsListener requestPermissionsListener) {
        this.fragment = fragment;
        this.pageType = PAGE_TYPE_FRAGMENT;
        this.requestPermissionsListener = requestPermissionsListener;
    }

    /**
     * 6.0及以上的系统  动态获取权限
     *
     * @param permissions 动态获取的权限
     * @param requestCode 权限申请回调code
     */
    public void requestPermissions(String[] permissions, int requestCode) {

        if(requestCode == TYPE_REQUEST_INSTALL_PACKAGES){
            //适配Android8.0系统的未知安装应用权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                boolean b = context.getPackageManager().canRequestPackageInstalls();
                if (b) {
                    requestPermissionsListener.getRequestPermissionResult(true, requestCode);
                    return;
                } else {
                    //未有安装未知应用的权限
                }
            } else {
                requestPermissionsListener.getRequestPermissionResult(true, requestCode);
                return;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 判断Android版本是否大于23
            int checkCallPhonePermission = PackageManager.PERMISSION_GRANTED;
            boolean isGetAllPermission = true;
            for (int i = 0; i < permissions.length; i++) {
                checkCallPhonePermission = ContextCompat.checkSelfPermission(context, permissions[i]);
                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {//只要有一个未获得权限则认为 未取得权限
                    isGetAllPermission = false;
                    break;
                }
            }
            if (!isGetAllPermission) {// 未取得权限

                if (pageType == PAGE_TYPE_FRAGMENT) {
                    fragment.requestPermissions(permissions, requestCode);//用Fragment的requestPermissions
                } else if (pageType == PAGE_TYPE_ACTIVITY) {
                    ActivityCompat.requestPermissions((Activity) context, permissions, requestCode);//Activity下的请求动态权限method
                } else {
                    CLogger.e( "requestPermission param pageType not define.");
                }

                return;
            } else {// 已有权限
                requestPermissionsListener.getRequestPermissionResult(true, requestCode);
            }
        } else {// API 版本在23以下
            requestPermissionsListener.getRequestPermissionResult(true, requestCode);
        }
    }


    /**
     * 监听权限申请结果
     * 该方法必须定义在Activity或Fragment的onRequestPermissionsResult方法中
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void listenerRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestPermissionsListener == null) {
            CLogger.e("requestPermissionsListener is null.");
            return;
        }

        CLogger.d("listenerRequestPermissionsResult： " + requestCode);
        /**
         * 遇到的问题： 允许权限后，始终后无法在该Fragment获取回调 ， 拒绝则有
         *
         * 原因：由于该DialogFragment依附MainActivity的基类JYActivity，
         * 我们在JYActivity中重写了onRequestPermissionsResult
         * 所以要在允许和拒绝后 调用super.onRequestPermissionsResult，
         * 这样Fragment的onRequestPermissionsResult才能有回调
         */

        if (grantResults != null && grantResults.length > 0) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//确定(获取权限成功)
                requestPermissionsListener.getRequestPermissionResult(true, requestCode);
            } else {//拒绝
                if(requestCode == TYPE_REQUEST_INSTALL_PACKAGES){
                    if(context != null){
                        //Android8.0的系统需要引导用户至安装未知应用界面。
                        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                        ((Activity)context).startActivityForResult(intent, TYPE_REQUEST_INSTALL_PACKAGES);
                    }
                }else{
                    requestPermissionsListener.getRequestPermissionResult(false, requestCode);
                }

            }
        } else {
            requestPermissionsListener.getRequestPermissionResult(false, requestCode);
        }

    }

    /**
     * Android8.0监听是否有安装未知应用的的权限
     * 该方法必须定义在Activity或Fragment的onActivityResult方法中
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void listenerInstallPackagePermissionResult(int requestCode, int resultCode, Intent data){
        if(requestCode == TYPE_REQUEST_INSTALL_PACKAGES){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                boolean b = context.getPackageManager().canRequestPackageInstalls();
                if (b) {
                    requestPermissionsListener.getRequestPermissionResult(true, requestCode);
                } else {
                    requestPermissionsListener.getRequestPermissionResult(false, requestCode);
                }
            } else {
                requestPermissionsListener.getRequestPermissionResult(true, requestCode);
            }

        }
    }

    public interface RequestPermissionsListener {
        /**
         * 动态获取权限回调
         *
         * @param success           true-成功 , false-失败
         * @param requestPermission 权限类型
         */
        void getRequestPermissionResult(boolean success, int requestPermission);
    }


}
