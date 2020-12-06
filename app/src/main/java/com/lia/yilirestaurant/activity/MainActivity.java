package com.lia.yilirestaurant.activity;

import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lia.yilirestaurant.adapter.MainPagerAdapter;
import com.lia.yilirestaurant.R;
import com.lia.yilirestaurant.listener.PermissionListener;


import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    Button pFoodBtn, pCommunicationBtn, pMeBtn;
    ViewPager viewPager;
    MainPagerAdapter mainPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        getPermission();
    }

    private void getPermission() {
        requestRunPermisssion(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
            @Override
            public void onGranted() {
                mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
                viewPager.setAdapter(mainPagerAdapter);
                viewPager.setCurrentItem(0);
                viewPager.setOffscreenPageLimit(2);
                pFoodBtn.setTextColor(Color.parseColor("#55AD5B"));
                pFoodBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.food_select), null, null);
            }

            @Override
            public void onDenied(List<String> deniedPermission) {
                for (String permission : deniedPermission) {
                    Toast.makeText(MainActivity.this, "权限不足,退出应用,被拒绝的权限：" + permission, Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }

    private void initListener() {
        viewPager.addOnPageChangeListener(new PagerChangeListener());
        pFoodBtn.setOnClickListener(this);
        pCommunicationBtn.setOnClickListener(this);
        pMeBtn.setOnClickListener(this);
    }

    private void initView() {
        viewPager = findViewById(R.id.vp_main_fragments);
        pFoodBtn = findViewById(R.id.btn_main_food);
        pCommunicationBtn = findViewById(R.id.btn_main_communication);
        pMeBtn = findViewById(R.id.btn_main_me);
    }

    private void setTextColor(int index) {
        pFoodBtn.setTextColor(Color.parseColor("#000000"));
        pCommunicationBtn.setTextColor(Color.parseColor("#000000"));
        pMeBtn.setTextColor(Color.parseColor("#000000"));
        switch (index) {
            case 0:
                pFoodBtn.setTextColor(Color.parseColor("#55AD5B"));
                break;
            case 1:
                pCommunicationBtn.setTextColor(Color.parseColor("#66CD00"));

                break;

            case 2:
                pMeBtn.setTextColor(Color.parseColor("#66CD00"));
                break;
        }

    }

    private void setButtonBackground(int index) {
        pFoodBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.food_unselect), null, null);
        pCommunicationBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.communication_unselected), null, null);
        pMeBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.me_unselected), null, null);
        switch (index) {
            case 0:
                pFoodBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.food_select), null, null);
                break;
            case 1:

                pCommunicationBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.communication_select), null, null);
                break;

            case 2:
                pMeBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.me_select), null, null);
                break;
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_food:
                viewPager.setCurrentItem(0);
                setTextColor(0);
                setButtonBackground(0);
                break;
            case R.id.btn_main_communication:
                viewPager.setCurrentItem(1);
                setTextColor(1);
                setButtonBackground(1);
                break;
            case R.id.btn_main_me:
                viewPager.setCurrentItem(2);
                setTextColor(2);
                setButtonBackground(2);
                break;
            default:
                break;
        }
    }

    public class PagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mainPagerAdapter.setpCurrentPosition(position);
            viewPager.getAdapter().notifyDataSetChanged();
            setTextColor(position);
            setButtonBackground(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}