package com.android.weather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.weather.R;
import com.android.weather.helper.SPUtils;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * desc   : 闪屏界面
 */
public final class SplashActivity extends AppCompatActivity
        implements OnPermission, Animation.AnimationListener {

    private static final int ANIM_TIME = 1000;

    @BindView(R.id.iv_splash_bg)
    View mImageView;
    @BindView(R.id.iv_splash_icon)
    View mIconView;
    @BindView(R.id.iv_splash_name)
    View mNameView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initView();
    }


    protected void initView() {
        // 初始化动画
        AlphaAnimation aa = new AlphaAnimation(0.4f, 1.0f);
        aa.setDuration(ANIM_TIME * 2);
        aa.setAnimationListener(this);
        mImageView.startAnimation(aa);

        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(ANIM_TIME);
        mIconView.startAnimation(sa);

        RotateAnimation ra = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(ANIM_TIME);
        mNameView.startAnimation(ra);

    }



    private void requestPermission() {
        XXPermissions.with(this)
                .permission(Permission.Group.STORAGE)
                .permission(Permission.Group.LOCATION)
                .request(this);
    }

    /**
     * {@link OnPermission}
     */

    @Override
    public void hasPermission(List<String> granted, boolean isAll) {
        if ((Boolean) SPUtils.get(this, "isLogin", false)) {//判断是否登录
            if ((Boolean) SPUtils.get(this, "isAutoLogin", false)) {//是否设置了自动登录
                startActivity(new Intent(SplashActivity.this,HomeActivity.class));

            } else {
                startActivity(new Intent(SplashActivity.this,LoginActivity.class));

            }
        } else {
            startActivity(new Intent(SplashActivity.this,LoginActivity.class));


        }
    }

    @Override
    public void noPermission(List<String> denied, boolean quick) {
        if (quick) {
            Toast.makeText(this, "权限获取失败", Toast.LENGTH_SHORT).show();
            XXPermissions.gotoPermissionSettings(SplashActivity.this, true);
        } else {
            Toast.makeText(this, "权限获取失败", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestPermission();
                }
            },1000);
        }
    }

    @Override
    public void onBackPressed() {
        //禁用返回键
        //super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (XXPermissions.isHasPermission(SplashActivity.this, Permission.Group.STORAGE)) {
            hasPermission(null, true);
        } else {
            requestPermission();
        }
    }

    /**
     * {@link Animation.AnimationListener}
     */

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        requestPermission();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }
}