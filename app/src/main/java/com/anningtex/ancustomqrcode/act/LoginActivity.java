package com.anningtex.ancustomqrcode.act;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.anningtex.ancustomqrcode.R;
import com.anningtex.ancustomqrcode.weight.TitleEditText;

public class LoginActivity extends AppCompatActivity {
    private TitleEditText mLoginEditUsername;
    private TitleEditText mLoginEditPassword;
    private TextView mLoginTvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        mLoginEditUsername = findViewById(R.id.login_edit_username);
        mLoginEditPassword = findViewById(R.id.login_edit_password);
        mLoginTvLogin = findViewById(R.id.login_tv_login);
    }

    public void login(View view) {

    }
}