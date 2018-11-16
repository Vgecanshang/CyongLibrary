package com.cy.cylibrary;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.cy.cylibrary.utils.SystemBarTintManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Activity基类
 * @author cy
 * @date 2018/11/16
 */
public class CBaseActivity extends AppCompatActivity {

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemBar();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        unbinder = ButterKnife.bind(this);
    }

    public final <E extends View> E getView(int id) {
        return (E) findViewById(id);
    }

    public final <E extends View> E getView(View view, int id) {
        return (E) view.findViewById(id);
    }

    public Activity getActivity() {
        return this;
    }

    /** 设置系统状态栏 */
    public void setSystemBar() {

        // 两种方案：
        // 一种是加长title的高度为status的高度，然后透上去，
        // 一种是直接设置status的颜色，但是6.0以上status下会有一条黑线,对4.4以下还得做处理。

        if(!hadSetSystemStatus()){
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getActivity().getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            View decorView = getActivity().getWindow().getDecorView();
            if(decorView != null){
                int vis = decorView.getSystemUiVisibility();

                if(setStatusTextColorWhite()){
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//白字白图标
                }else{
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//黑字，黑图标
                }
                //View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN:解决在6.0系统以上，设置颜色后， status下面有条黑线的问题
                decorView.setSystemUiVisibility(vis | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(getStatusColor()));
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setTranslucentStatus(getActivity(), true);
            }
            SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
            tintManager.setStatusBarTintEnabled(true);
            // 使用颜色资源
            tintManager.setStatusBarTintResource(getStatusColor());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getActivity().getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView()
                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        }

    }

    /** 设置系统状态栏透明 */
    public void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /** 系统状态栏的颜色 */
    protected int getStatusColor(){
        return R.color.colorPrimary;
    }

    /** 是否需要设置系统状态栏的颜色 */
    protected boolean hadSetSystemStatus(){
        return false;
    }

    /** 是否设置系统状态栏文字的颜色为白色，否 则为黑色 */
    protected boolean setStatusTextColorWhite(){
        return true ;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(unbinder != null){
            unbinder.unbind();
        }
    }
}
