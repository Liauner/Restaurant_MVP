package com.lia.yilirestaurant.activity;

import androidx.annotation.NonNull;
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
import com.lia.yilirestaurant.bean.UserBean;
import com.lia.yilirestaurant.dialog.LoadingDialog;
import com.lia.yilirestaurant.util.Api;
import com.lia.yilirestaurant.util.OkHttpUtil;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    EditText pAccountEt, pPasswordEt, pRepeatPasswordEt;
    Button pPostBtn;
    Handler handler;
    LoadingDialog loadingDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ActionBar actionBar = getSupportActionBar();
        initView();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        handler = new SignUpHandler(this);
        pPostBtn.setOnClickListener(this);
    }

    private void initView() {
        pAccountEt = findViewById(R.id.tv_signup_account);
        pPasswordEt = findViewById(R.id.tv_signup_password);
        pRepeatPasswordEt = findViewById(R.id.tv_signup_repeatpassword);
        pPostBtn = findViewById(R.id.btn_signup_post);
    }


    @Override
    public void onClick(View v) {
        if (null == pAccountEt.getText().toString() || null == pPasswordEt.getText().toString() || null == pRepeatPasswordEt.getText().toString() || pAccountEt.getText().toString().isEmpty() || pPasswordEt.getText().toString().isEmpty() || pRepeatPasswordEt.getText().toString().isEmpty()) {
            Toast.makeText(this, "输入内容有误", Toast.LENGTH_SHORT).show();
        } else {
            if (pPasswordEt.getText().toString().equals(pRepeatPasswordEt.getText().toString())) {
                final FormBody.Builder builder = new FormBody.Builder();
                builder.add("username", pAccountEt.getText().toString());
                builder.add("password", pPasswordEt.getText().toString());
                loadingDialog = LoadingDialog.Build(this);
                loadingDialog.show();
                OkHttpUtil.sendPostRequest(Api.PST_Reg, builder.build(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("TAG", "onResponse:fail ");
                        handler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        OkHttpUtil.handleResponse(call, response, 1, handler);
                    }
                });
            } else {
                Toast.makeText(this, "密码不一致", Toast.LENGTH_SHORT).show();
            }
        }

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

    static class SignUpHandler extends Handler {
        WeakReference<SignUpActivity> mWeakReference;

        public SignUpHandler(SignUpActivity activity) {
            mWeakReference = new WeakReference<SignUpActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final SignUpActivity activity = mWeakReference.get();
            if (activity != null) {
                if (activity.loadingDialog != null) {
                    activity.loadingDialog.dismiss();
                }
                if (msg.what == 0) {
                    Toast.makeText(activity, "网络错误", Toast.LENGTH_SHORT).show();
                } else if (msg.what == 1) {
                    String str = msg.getData().getString("first");
                    Gson gson = new Gson();
                    UserBean userBean = gson.fromJson(str, UserBean.class);
                    if (userBean != null && userBean.getInfo().getCode().equals("0")) {
                        activity.loadingDialog.dismiss();
                        Intent intent = new Intent();
                        intent.putExtra("account", activity.pAccountEt.getText().toString());
                        activity.setResult(1, intent);
                        Toast.makeText(activity, "注册成功,您的账号是" + activity.pAccountEt.getText().toString(), Toast.LENGTH_SHORT).show();
                        activity.finish();
                    } else if (userBean != null && userBean.getInfo().getCode().equals("100")) {
                        activity.loadingDialog.dismiss();
                        activity.pAccountEt.setText("");
                        activity.pPasswordEt.setText("");
                        activity.pRepeatPasswordEt.setText("");
                        Toast.makeText(activity, "账号已存在", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }


}