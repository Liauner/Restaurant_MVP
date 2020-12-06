package com.lia.yilirestaurant.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lia.yilirestaurant.adapter.OrderDetailAdapter;
import com.lia.yilirestaurant.R;
import com.lia.yilirestaurant.bean.FoodOrderDetailBean;
import com.lia.yilirestaurant.dialog.LoadingDialog;
import com.lia.yilirestaurant.util.Api;
import com.lia.yilirestaurant.util.OkHttpUtil;
import com.lia.yilirestaurant.util.BaseApplication;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OrderDetailActivity extends AppCompatActivity {

    OrderDetailAdapter pOrderDetailAdapter;
    TextView pPriceTv;
    ListView pListView;
    Handler handler;
    String orderId;
    LoadingDialog loadingDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_order_detail);
        pPriceTv = findViewById(R.id.tv_orderdetail_price);
        pListView = findViewById(R.id.lv_orderdetail);
        if (getIntent() != null) {
            orderId = getIntent().getStringExtra("orderId");
        }

        handler = new OrderDetailHandler(this);
        getOrdersDetail(orderId, BaseApplication.getgUsername());


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

    private void getOrdersDetail(String orderId, String username) {
        loadingDialog = LoadingDialog.Build(this);
        loadingDialog.show();
        OkHttpUtil.sendGetRequest(Api.GET_OrderDetail + username + "&orderId=" + orderId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(0);
                Log.e("TAG", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                OkHttpUtil.handleResponse(call, response, 1, handler);

            }
        });
    }


    static class OrderDetailHandler extends Handler {
        WeakReference<OrderDetailActivity> mWeakReference;

        public OrderDetailHandler(OrderDetailActivity activity) {
            mWeakReference = new WeakReference<OrderDetailActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final OrderDetailActivity activity = mWeakReference.get();
            if (activity != null) {
                if (activity.loadingDialog != null) {
                    activity.loadingDialog.dismiss();
                }
                if (msg.what == 0) {
                    Toast.makeText(activity, "网络错误", Toast.LENGTH_SHORT).show();
                } else if (msg.what == 1) {
                    String str = msg.getData().getString("first");
                    Gson gson = new Gson();
                    FoodOrderDetailBean foodOrderDetailBean = gson.fromJson(str, FoodOrderDetailBean.class);
                    if (foodOrderDetailBean.getInfo().getCode().equals("0")) {
                        activity.pPriceTv.setText(foodOrderDetailBean.getOrders().getPrice() + "元");
                        activity.pOrderDetailAdapter = new OrderDetailAdapter(foodOrderDetailBean.getOrders().getOrderItem(), activity);
                        activity.pListView.setAdapter(activity.pOrderDetailAdapter);
                    }
                }
            }
        }
    }


}