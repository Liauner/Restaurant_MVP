package com.lia.yilirestaurant.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lia.yilirestaurant.R;
import com.lia.yilirestaurant.fragment.SplashImageFragment;
import com.lia.yilirestaurant.util.OkHttpUtil;
import com.lia.yilirestaurant.util.BaseApplication;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    private Button pSkipBtn;
    private ViewPager pImagesVp;
    private TextView pPageNubmerTv;
    private List<Fragment> pFragmentList = new ArrayList<>();
    private SplashViewPager pSplashViewPager;
    private TimeUtils pCountDownTimer;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        handler = new SplashActivityHandler(this);
        login(BaseApplication.getgUsername(), BaseApplication.getgPassword());
        pFragmentList.add(new SplashImageFragment(R.mipmap.splash1));
        pFragmentList.add(new SplashImageFragment(R.mipmap.splash2));
        pFragmentList.add(new SplashImageFragment(R.mipmap.splash3));
        pSplashViewPager = new SplashViewPager(getSupportFragmentManager(), pFragmentList);
        pImagesVp.setAdapter(pSplashViewPager);
        pImagesVp.setCurrentItem(0);
        pImagesVp.addOnPageChangeListener(new SplashViewPagerChangeListener());
        pPageNubmerTv.setText("1/3");
        pSkipBtn.setOnClickListener(this);
        pCountDownTimer = new TimeUtils(this);
        pCountDownTimer.RunTimer();
    }

    private void login(String username, String password) {
        OkHttpUtil.sendGetRequest(Api.GET_login + username + "&password=" + password, new Callback() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pCountDownTimer != null) {
            pCountDownTimer.timer.cancel();
        }
    }

    private void initView() {
        pSkipBtn = findViewById(R.id.btn_splash_skip);
        pPageNubmerTv = findViewById(R.id.tv_splash_pagenumber);
        pImagesVp = findViewById(R.id.vp_splash_images);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_splash_skip:
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                if (pCountDownTimer != null) {
                    pCountDownTimer.timer.cancel();
                }
                finish();
                break;
            default:
                break;
        }
    }

    static class SplashActivityHandler extends Handler {
        WeakReference<SplashActivity> mWeakReference;

        public SplashActivityHandler(SplashActivity activity) {
            mWeakReference = new WeakReference<SplashActivity>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            final SplashActivity activity = mWeakReference.get();
            if (activity != null) {
                if (msg.what == 1) {
                    Gson gson = new Gson();
                    String str = msg.getData().getString("first");
                    SignInActivity.LoginBean loginBean = gson.fromJson(str, SignInActivity.LoginBean.class);
                    if (loginBean != null && loginBean.getInfo().getCode().equals("0")) {
                        BaseApplication.saveUser(BaseApplication.getgUsername(), BaseApplication.getgPassword());
                    } else {
                        BaseApplication.saveUser("", "");
                    }
                }else if (msg.what==0){
                    BaseApplication.saveUser("", "");
                }
            }

        }
    }

    static class SplashViewPager extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;

        public SplashViewPager(@NonNull FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    class SplashViewPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            pPageNubmerTv.setText(String.valueOf(position + 1) + "/3");
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    static class TimeUtils {
        WeakReference<SplashActivity> pSplashActivity;
        private CountDownTimer timer;

        public TimeUtils(SplashActivity pSplashActivity) {
            this.pSplashActivity = new WeakReference(pSplashActivity);
        }

        public void RunTimer() {
            //+500毫秒是为了补回程序执行代码时所消耗得时间，不然实际倒数时9.XX秒
            timer = new CountDownTimer(10 * 1000 + 500, 1000) {
                @Override
                public void onTick(long l) {
                    if (pSplashActivity.get() != null) {
                        pSplashActivity.get().pSkipBtn.setText("倒数" + String.valueOf(l / 1000));
                    }
                }

                @Override
                public void onFinish() {
                    if (pSplashActivity.get() != null) {
                        Intent intent = new Intent(pSplashActivity.get(), MainActivity.class);
                        pSplashActivity.get().startActivity(intent);
                        pSplashActivity.get().finish();
                    }

                }
            }.start();
        }

    }
}