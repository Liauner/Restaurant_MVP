package com.lia.yilirestaurant.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lia.yilirestaurant.R;
import com.lia.yilirestaurant.bean.FoodBean;
import com.lia.yilirestaurant.bean.SingleFoodBean;
import com.lia.yilirestaurant.dialog.LoadingDialog;
import com.lia.yilirestaurant.util.Api;
import com.lia.yilirestaurant.util.OkHttpUtil;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FoodDetailActivity extends AppCompatActivity {
    ImageView pPicIv;
    TextView pNameTv;
    TextView pContentTv;
    TextView pPriceTv;

    LoadingDialog loadingDialog = null;
    Handler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
        handler = new FoodDetailHandler(this);
        if (getIntent() != null) {
            String foodId = getIntent().getStringExtra("foodId");
            loadingDialog = LoadingDialog.Build(this);
            loadingDialog.show();
            OkHttpUtil.sendGetRequest(Api.GET_FoodId + foodId, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    handler.sendEmptyMessage(0);

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    OkHttpUtil.handleResponse(call, response, 1, handler);
                }
            });
        }


    }

    private void initView() {
        pPicIv = findViewById(R.id.iv_fooddetail_pic);
        pNameTv = findViewById(R.id.tv_fooddetail_name);
        pContentTv = findViewById(R.id.tv_fooddetail_content);
        pPriceTv = findViewById(R.id.tv_fooddetail_price);
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

    static class FoodDetailHandler extends Handler {
        WeakReference<FoodDetailActivity> mWeakReference;

        public FoodDetailHandler(FoodDetailActivity activity) {
            mWeakReference = new WeakReference<FoodDetailActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final FoodDetailActivity activity = mWeakReference.get();
            if (activity != null) {
                if (activity.loadingDialog != null) {
                    activity.loadingDialog.dismiss();
                }
                if (msg.what == 0) {
                    Toast.makeText(activity, "网络错误", Toast.LENGTH_SHORT).show();
                } else if (msg.what == 1) {
                    String str = msg.getData().getString("first");
                    Gson gson = new Gson();
                    SingleFoodBean singleFoodBean = gson.fromJson(str, SingleFoodBean.class);
                    FoodBean.Food food = singleFoodBean.getFood();
                    Glide.with(activity).load(food.getImgUrl()).placeholder(R.mipmap.placeholder).error(R.mipmap.error).into(activity.pPicIv);
                    activity.pNameTv.setText(food.getName());
                    activity.pContentTv.setText(food.getDes());
                    activity.pPriceTv.setText(food.getPrice() + "元/份");
                }
            }
        }
    }


}