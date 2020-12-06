package com.lia.yilirestaurant.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lia.yilirestaurant.R;
import com.lia.yilirestaurant.adapter.FoodOrderAdapter;
import com.lia.yilirestaurant.bean.DepositBean;
import com.lia.yilirestaurant.bean.FoodOrderListBean;
import com.lia.yilirestaurant.bean.MomentListBean;
import com.lia.yilirestaurant.bean.OrdersBean;
import com.lia.yilirestaurant.dialog.DepositDialog;
import com.lia.yilirestaurant.dialog.LoadingDialog;
import com.lia.yilirestaurant.listener.FragmentListener;
import com.lia.yilirestaurant.util.Api;
import com.lia.yilirestaurant.util.OkHttpUtil;
import com.lia.yilirestaurant.refresh.SwipeRefresh;
import com.lia.yilirestaurant.refresh.SwipeRefreshLayout;
import com.lia.yilirestaurant.util.BaseApplication;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class MeFragment extends Fragment implements View.OnClickListener {
    Button pLogoutBtn, pDepositBtn;
    TextView pUsernameTv, pBalanceTv;
    FragmentListener fragmentListener;
    SwipeRefreshLayout pSwipeRefreshLayout;
    RecyclerView pRecyclerView;
    RecyclerView.LayoutManager pLayoutManager;
    FoodOrderAdapter pFoodOrderAdapter;
    Handler handler = null;
    LoadingDialog loadingDialog;
    int pPage = 1;
    int a = 0;

    public MeFragment(FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }

    public void updata() {
        pPage = 1;
        getUserDetail();
    }

    private void getUserDetail() {
        OkHttpUtil.sendGetRequest(Api.GET_GetUserDetail + BaseApplication.getgUsername(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                OkHttpUtil.handleResponse(call, response, 2, handler);
            }
        });

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        pLogoutBtn.setOnClickListener(this);
        pDepositBtn.setOnClickListener(this);
        handler = new MeHandler(this);
        pFoodOrderAdapter = new FoodOrderAdapter(getContext());
        pLayoutManager = new LinearLayoutManager(getActivity());
        pRecyclerView.setLayoutManager(pLayoutManager);
        pRecyclerView.setAdapter(pFoodOrderAdapter);
        pSwipeRefreshLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
        pSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLACK, Color.GRAY);
        pSwipeRefreshLayout.setOnRefreshListener(new SwipeRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉
                pPage = 1;
                pFoodOrderAdapter.clearOrdersList();
                getOrdersList(pPage++, 5);
                pSwipeRefreshLayout.setRefreshing(false);
            }
        });
        pSwipeRefreshLayout.setOnPullUpRefreshListener(new SwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                //上拉
                getOrdersList(pPage++, 5);
                pSwipeRefreshLayout.setPullUpRefreshing(false);
            }
        });
        getUserDetail();
        getOrdersList(pPage++, 5);
    }

    private void getOrdersList(int page, int limit) {

        OkHttpUtil.sendGetRequest(Api.GET_GetOrderList + BaseApplication.getgUsername() + "&page=" + String.valueOf(page) + "&limit=" + String.valueOf(limit), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(0);
                Log.e("TAG", e.toString()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                    OkHttpUtil.handleResponse(call, response, 3, handler);

            }
        });

    }


    private void initView(View view) {
        pLogoutBtn = view.findViewById(R.id.btn_me_logout);
        pDepositBtn = view.findViewById(R.id.btn_me_deposit);
        pUsernameTv = view.findViewById(R.id.tv_me_username);
        pBalanceTv = view.findViewById(R.id.tv_me_balance);
        pRecyclerView = view.findViewById(R.id.rv_food_lsitview);
        pSwipeRefreshLayout = view.findViewById(R.id.srl_me);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, container, false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_me_logout:
                new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("确定退出么？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BaseApplication.saveUser("", "");
                                dialog.dismiss();
                                fragmentListener.onSwitchToAnotherFragment(1, 1);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case R.id.btn_me_deposit:
                DepositDialog depositDialog = new DepositDialog(getContext(), new DepositDialog.DepositButtonListener() {
                    @Override
                    public void onClick(String depositValue, final DepositDialog dialog) {
                        FormBody.Builder builder = new FormBody.Builder();
                        builder.add("username", BaseApplication.getgUsername());
                        builder.add("balance", depositValue);
                        loadingDialog = LoadingDialog.Build(getContext());
                        loadingDialog.show();
                        OkHttpUtil.sendPostRequest(Api.POST_Deposit, builder.build(), new Callback() {
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
                });
                depositDialog.show();
                break;
            default:
                break;
        }
    }

    static class MeHandler extends Handler {
        WeakReference<MeFragment> mWeakReference;

        public MeHandler(MeFragment fragment) {
            mWeakReference = new WeakReference<MeFragment>(fragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            final MeFragment fragment = mWeakReference.get();
            if (fragment != null) {
                if (fragment.loadingDialog != null) {
                    fragment.loadingDialog.dismiss();
                }
                String str = msg.getData().getString("first");
                Gson gson = new Gson();
                switch (msg.what) {
                    case 0:
                        Toast.makeText(fragment.getContext(), "网络错误", Toast.LENGTH_SHORT).show();
                    case 1:

                        DepositBean depositBean1 = gson.fromJson(str, DepositBean.class);
                        if (depositBean1.getInfo().getCode().equals("0")) {
                            fragment.pUsernameTv.setText(depositBean1.getUser().getUsername());
                            fragment.pBalanceTv.setText("余额:" + depositBean1.getUser().getBalance() + "元");
                            Toast.makeText(fragment.getContext(), "充值成功，你的余额为" + depositBean1.getUser().getBalance(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(fragment.getContext(), "充值失败", Toast.LENGTH_SHORT).show();

                        }
                        break;
                    case 2:
                        DepositBean depositBean2 = gson.fromJson(str, DepositBean.class);
                        if (depositBean2.getInfo().getCode().equals("0")) {
                            fragment.pUsernameTv.setText(depositBean2.getUser().getUsername());
                            fragment.pBalanceTv.setText("余额:" + depositBean2.getUser().getBalance() + "元");
                        }
                        break;
                    case 3:
                        FoodOrderListBean foodOrderListBean = gson.fromJson(str, FoodOrderListBean.class);
                        List<OrdersBean> foodOrderList = foodOrderListBean.getOrders();
                        if (foodOrderList != null && foodOrderList.size() > 0) {
                            Toast.makeText(fragment.getContext(), "为你找到" + foodOrderList.size() + "个新订单", Toast.LENGTH_SHORT).show();
                            fragment.pFoodOrderAdapter.addOrdersList(foodOrderList);
                        } else {
                            Toast.makeText(fragment.getContext(), "没有新订单了", Toast.LENGTH_SHORT).show();
                        }
                    default:
                        break;
                }
            }

        }
    }

}
