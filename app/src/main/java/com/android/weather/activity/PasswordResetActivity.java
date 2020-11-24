package com.android.weather.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.android.weather.R;
import com.android.weather.db.UserDao;
import com.android.weather.helper.InputTextHelper;
import com.android.weather.helper.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * desc   : 重置密码
 */
public final class PasswordResetActivity extends AppCompatActivity {

    @BindView(R.id.et_password_reset_password1)
    EditText mPasswordView1;
    @BindView(R.id.et_password_reset_password2)
    EditText mPasswordView2;
    @BindView(R.id.et_password_old)
    EditText mPasswordViewOld;
    @BindView(R.id.btn_password_reset_commit)
    Button mCommitView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        ButterKnife.bind(this);
        initView();
    }


    protected void initView() {
        InputTextHelper.with(this)
                .addView(mPasswordView1)
                .addView(mPasswordView2)
                .addView(mPasswordViewOld)
                .setMain(mCommitView)
                .setListener(new InputTextHelper.OnInputTextListener() {

                    @Override
                    public boolean onInputChange(InputTextHelper helper) {
                        return mPasswordView1.getText().toString().length() >= 6 &&
                                mPasswordView1.getText().toString().equals(mPasswordView2.getText().toString());
                    }
                })
                .build();
    }



    @OnClick({R.id.btn_password_reset_commit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_password_reset_commit:
                UserDao userDao = new UserDao(this);
                String name = (String) SPUtils.get(this, "name", "");
                int i = userDao.updatePw(name, mPasswordViewOld.getText().toString(), mPasswordView1.getText().toString());
                if (i==1){
                    Toast.makeText(this, "密码重置成功", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(this, "修改失败请重试", Toast.LENGTH_SHORT).show();

                }


                break;
            default:
                break;
        }
    }
}