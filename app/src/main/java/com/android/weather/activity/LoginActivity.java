package com.android.weather.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.android.weather.R;
import com.android.weather.db.UserDao;
import com.android.weather.helper.InputTextHelper;
import com.android.weather.helper.SPUtils;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * desc   : 登录界面
 */
public final class LoginActivity extends AppCompatActivity {

    @BindView(R.id.iv_login_logo)
    ImageView mLogoView;

    @BindView(R.id.ll_login_body)
    LinearLayout mBodyLayout;
    @BindView(R.id.et_login_phone)
    EditText mPhoneView;
    @BindView(R.id.et_login_password)
    EditText mPasswordView;

    @BindView(R.id.btn_login_commit)
    Button mCommitView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }


    protected void initView() {
        InputTextHelper.with(this)
                .addView(mPhoneView)
                .addView(mPasswordView)
                .setMain(mCommitView)
                .build();
        TitleBar titleBar = findViewById(R.id.title);
        titleBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {

            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }


    @OnClick({R.id.btn_login_commit})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_login_commit:
                login();
                break;
            default:
                break;
        }
    }

    public void login() {

        UserDao userdao = new UserDao(LoginActivity.this);
        Cursor cursor = userdao.query(mPhoneView.getText().toString(), mPasswordView.getText().toString());
        if (cursor.moveToNext()) {
            SPUtils.put(LoginActivity.this, "isLogin", true);
            SPUtils.put(LoginActivity.this, "name", mPhoneView.getText().toString());
            // 处理登录
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            cursor.close();
            finish();
        } else {
            Toast.makeText(LoginActivity.this, "密码验证失败，请重新验证登录", Toast.LENGTH_SHORT).show();
        }

    }


}