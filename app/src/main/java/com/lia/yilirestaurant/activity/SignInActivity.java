package com.lia.yilirestaurant.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lia.yilirestaurant.R;
import com.lia.yilirestaurant.bean.InfoBean;
import com.lia.yilirestaurant.dialog.LoadingDialog;
import com.lia.yilirestaurant.util.Api;
import com.lia.yilirestaurant.util.OkHttpUtil;
import com.lia.yilirestaurant.util.BaseApplication;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    EditText pAccountEt, pPasswordEt;
    Button pPostBtn, pSignUpBtn;
    Handler handler;
    LoadingDialog loadingDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ActionBar actionBar = getSupportActionBar();
        initView();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        handler = new SignInHandler(this);
        pPostBtn.setOnClickListener(this);
        pSignUpBtn.setOnClickListener(this);
    }

    private void initView() {
        pAccountEt = findViewById(R.id.tv_signin_account);
        pPasswordEt = findViewById(R.id.tv_signin_password);
        pPostBtn = findViewById(R.id.btn_signin_post);
        pSignUpBtn = findViewById(R.id.btn_signin_signup);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signin_post:
                if (null == pAccountEt.getText().toString() || null == pPasswordEt.getText().toString() || pAccountEt.getText().toString().isEmpty() || pPasswordEt.getText().toString().isEmpty()) {
                    Toast.makeText(this, "输入内容有误", Toast.LENGTH_SHORT).show();
                } else {
                    loadingDialog = LoadingDialog.Build(this);
                    loadingDialog.show();
                    OkHttpUtil.sendGetRequest(Api.GET_login + pAccountEt.getText().toString() + "&password=" + pPasswordEt.getText().toString(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("TAG", "onResponse:fail");
                            handler.sendEmptyMessage(0);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            OkHttpUtil.handleResponse(call, response, 1, handler);

                        }
                    });
                }
                break;
            case R.id.btn_signin_signup:
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivityForResult(intent, 1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            pAccountEt.setText(data.getStringExtra("account"));
        }
    }

    public static class LoginBean {
        InfoBean info;

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }
    }

    static class SignInHandler extends Handler {
        WeakReference<SignInActivity> mWeakReference;

        public SignInHandler(SignInActivity activity) {
            mWeakReference = new WeakReference<SignInActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final SignInActivity activity = mWeakReference.get();
            if (activity != null) {
                activity.loadingDialog.dismiss();
                if (msg.what == 0) {
                    Toast.makeText(activity, "网络错误", Toast.LENGTH_SHORT).show();
                } else if (msg.what == 1) {
                    Gson gson = new Gson();
                    String str = msg.getData().getString("first");
                    LoginBean loginBean = gson.fromJson(str, LoginBean.class);
                    if (loginBean!=null&&loginBean.getInfo().getCode().equals("100")){
                        Toast.makeText(activity, "账号密码错误", Toast.LENGTH_SHORT).show();
                    }
                    else if (loginBean != null && loginBean.getInfo().getCode().equals("0")) {
                        BaseApplication.saveUser(activity.pAccountEt.getText().toString(), activity.pPasswordEt.getText().toString());
                        activity.setResult(1);
                        activity.finish();
                    }
                }
            }
        }
    }


}