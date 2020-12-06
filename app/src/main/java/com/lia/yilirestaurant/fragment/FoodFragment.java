package com.lia.yilirestaurant.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.lia.yilirestaurant.activity.SplashActivity;
import com.lia.yilirestaurant.bean.InfoBean;
import com.lia.yilirestaurant.bean.PayBean;
import com.lia.yilirestaurant.dialog.LoadingDialog;
import com.lia.yilirestaurant.listener.FragmentListener;
import com.lia.yilirestaurant.R;
import com.lia.yilirestaurant.activity.SignInActivity;
import com.lia.yilirestaurant.adapter.FoodAdapter;
import com.lia.yilirestaurant.bean.FoodBean;
import com.lia.yilirestaurant.util.Api;
import com.lia.yilirestaurant.util.OkHttpUtil;
import com.lia.yilirestaurant.refresh.SwipeRefresh;
import com.lia.yilirestaurant.refresh.SwipeRefreshLayout;
import com.lia.yilirestaurant.util.BaseApplication;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class FoodFragment extends Fragment implements FoodAdapter.FoodItemButtononClick {
    int pSumPrice = 0;
    int pSumCount = 0;
    int pPage = 1;
    FoodAdapter pFoodAdapter;
    SwipeRefreshLayout pSwipeRefreshLayout;
    RecyclerView recyclerView;
    TextView pCountTv;
    TextView pPriceTv;
    Button pPayBtn;
    Handler handler;
    List<FoodBean.Food> foodList = new ArrayList<>();
    RecyclerView.LayoutManager mLayoutManager;
    FragmentListener fragmentListener;

    public FoodFragment(FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        handler = new FoodHandler(this);
        pFoodAdapter = new FoodAdapter(foodList, getActivity(), this);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(pFoodAdapter);

        pSwipeRefreshLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
        pSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLACK, Color.GRAY);
        pSwipeRefreshLayout.setOnRefreshListener(new SwipeRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉
                if (pSumCount > 0) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("提示")
                            .setMessage("刷新会清空已选择得食物。确定继续么？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    pPage = 1;
                                    pSumPrice = 0;
                                    pSumCount = 0;
                                    pPriceTv.setText(String.valueOf(pSumPrice) + "元");
                                    pCountTv.setText("数量:" + pSumCount);
                                    pFoodAdapter.clearFood();
                                    getFoodList(pPage++, 5);
                                    pSwipeRefreshLayout.setRefreshing(false);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    pSwipeRefreshLayout.setRefreshing(false);
                                }
                            })
                            .show();
                } else {
                    pPage = 1;
                    pFoodAdapter.clearFood();
                    getFoodList(pPage++, 5);
                    pSwipeRefreshLayout.setRefreshing(false);
                }

            }
        });
        pSwipeRefreshLayout.setOnPullUpRefreshListener(new SwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                //上拉
                getFoodList(pPage++, 5);
                pSwipeRefreshLayout.setPullUpRefreshing(false);

            }
        });

        pPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BaseApplication.getgUsername().isEmpty()) {
                    Intent intent = new Intent(getActivity(), SignInActivity.class);
                    startActivityForResult(intent, 2);
                } else if (pSumCount > 0) {
                    FormBody.Builder builder = new FormBody.Builder();
                    builder.add("username", BaseApplication.getgUsername());
                    builder.add("orderItems", pFoodAdapter.getOrderItems());
                    OkHttpUtil.sendPostRequest(Api.POST_CreateOrder, builder.build(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("TAG", "onResponse:fail ");
                            handler.sendEmptyMessage(0);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            OkHttpUtil.handleResponse(call, response, 2, handler);
                            //handler.sendEmptyMessage(2);
                            Log.e("TAG", pFoodAdapter.getOrderItems());
                        }
                    });

                } else {
                    Toast.makeText(getContext(), "请选择菜品", Toast.LENGTH_SHORT).show();
                }


            }
        });
        getFoodList(pPage++, 5);
    }

    private void initView(@NonNull View view) {
        pCountTv = view.findViewById(R.id.tv_food_count);
        pPriceTv = view.findViewById(R.id.tv_food_price);
        pPayBtn = view.findViewById(R.id.btn_food_pay);
        pSwipeRefreshLayout = view.findViewById(R.id.srl_food);
        recyclerView = view.findViewById(R.id.rv_food_lsitview);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!BaseApplication.getgUsername().isEmpty()) {
            fragmentListener.onSwitchToAnotherFragment(2, 0);
            if (pSumCount > 0) {
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("username", BaseApplication.getgUsername());
                builder.add("orderItems", pFoodAdapter.getOrderItems());
                OkHttpUtil.sendPostRequest(Api.POST_CreateOrder, builder.build(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("TAG", "onResponse:fail ");
                        handler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        OkHttpUtil.handleResponse(call, response, 2, handler);
                       // handler.sendEmptyMessage(2);
                        Log.e("TAG", pFoodAdapter.getOrderItems());
                    }
                });

            } else {
                Toast.makeText(getContext(), "请选择菜品", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void getFoodList(int page, int limit) {

        OkHttpUtil.sendGetRequest(Api.GET_FoodList + String.valueOf(page) + "&limit=" + String.valueOf(limit), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", e.toString());
                handler.sendEmptyMessage(0);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                OkHttpUtil.handleResponse(call, response, 1, handler);
            }
        });

    }

    @Override
    public void buttonAddClick(FoodBean.Food food, int Count) {
        pSumPrice += Integer.parseInt(food.getPrice());
        pSumCount++;
        pPriceTv.setText(String.valueOf(pSumPrice) + "元");
        pCountTv.setText("数量:" + pSumCount);
    }

    @Override
    public void buttonSubClick(FoodBean.Food food, int count) {
        pSumPrice -= Integer.parseInt(food.getPrice());
        pSumCount--;
        pPriceTv.setText(String.valueOf(pSumPrice) + "元");
        pCountTv.setText("数量:" + pSumCount);
    }

    static class FoodHandler extends Handler {
        WeakReference<FoodFragment> mWeakReference;

        public FoodHandler(FoodFragment fragment) {
            mWeakReference = new WeakReference<FoodFragment>(fragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            final FoodFragment fragment = mWeakReference.get();
            if (fragment != null) {
                if (msg.what == 0) {
                    Toast.makeText(fragment.getContext(), "网络错误", Toast.LENGTH_SHORT).show();
                }
                if (msg.what == 1) {
                    String str = msg.getData().getString("first");
                    Gson gson = new Gson();
                    FoodBean foodBean = gson.fromJson(str, FoodBean.class);
                    fragment.foodList = foodBean.getFoodList();
                    if (fragment.foodList != null && fragment.foodList.size() > 0) {
                        Toast.makeText(fragment.getContext(), "为你找到" + fragment.foodList.size() + "个新食物", Toast.LENGTH_SHORT).show();
                        fragment.pFoodAdapter.addFood(fragment.foodList);

                    } else {
                        Toast.makeText(fragment.getContext(), "没有新的食物了", Toast.LENGTH_SHORT).show();
                    }
                } else if (msg.what == 2) {
                    String str = msg.getData().getString("first");
                    Gson gson = new Gson();
                    PayBean payBean = gson.fromJson(str, PayBean.class);
                    if (payBean!=null&&payBean.getInfo().getCode().equals("0")){
                        fragment.pSumPrice = 0;
                        fragment.pSumCount = 0;
                        fragment.pPriceTv.setText(String.valueOf(fragment.pSumPrice) + "元");
                        fragment.pCountTv.setText("数量:" + fragment.pSumCount);
                        fragment.pFoodAdapter.clearFoodItemsCount();
                        Toast.makeText(fragment.getContext(), "支付成功,可去订单中心查询", Toast.LENGTH_SHORT).show();
                    }
                    else if (payBean!=null&&payBean.getInfo().getCode().equals("118"))
                    {
                        Toast.makeText(fragment.getContext(), "余额不足，支付失败", Toast.LENGTH_SHORT).show();
                    }

                }

            }

        }
    }

}
