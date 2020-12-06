package com.lia.yilirestaurant.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.lia.yilirestaurant.adapter.CommunicationListViewAdapter;
import com.lia.yilirestaurant.bean.FoodBean;
import com.lia.yilirestaurant.dialog.LoadingDialog;
import com.lia.yilirestaurant.listener.FragmentListener;
import com.lia.yilirestaurant.adapter.MainPagerAdapter;
import com.lia.yilirestaurant.R;
import com.lia.yilirestaurant.bean.MomentBean;
import com.lia.yilirestaurant.bean.MomentListBean;
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
import okhttp3.Response;

public class CommunicationFragment extends Fragment {
    FragmentListener fragmentListener;
    MainPagerAdapter adapter;
    Handler handler;
    ListView pCommunicationLv;
    List<MomentBean> list = new ArrayList<>();
    CommunicationListViewAdapter communicationListViewAdapter;
    SwipeRefreshLayout pSwipeRefreshLayout;

    int pPage = 1;
    int a = 0;

    public CommunicationFragment(MainPagerAdapter adapter, FragmentListener fragmentListener) {
        this.adapter = adapter;
        this.fragmentListener = fragmentListener;
    }

    public void updata() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_communication, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pCommunicationLv = view.findViewById(R.id.lv_communication);
        pSwipeRefreshLayout = view.findViewById(R.id.srl_communication);
        communicationListViewAdapter = new CommunicationListViewAdapter(list, getContext());
        pCommunicationLv.setAdapter(communicationListViewAdapter);
        handler = new CommunicationHandler(this);

        pSwipeRefreshLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
        pSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLACK, Color.GRAY);
        pSwipeRefreshLayout.setOnRefreshListener(new SwipeRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉

                pPage = 1;
                communicationListViewAdapter.clearlist();
                getCommunictation(pPage++, 5);
                pSwipeRefreshLayout.setRefreshing(false);
            }
        });
        pSwipeRefreshLayout.setOnPullUpRefreshListener(new SwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                //上拉

                getCommunictation(pPage++, 5);
                pSwipeRefreshLayout.setPullUpRefreshing(false);

            }
        });
        getCommunictation(1, 5);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getCommunictation(int page, int limit) {
        OkHttpUtil.sendGetRequest(Api.GET_MomentList + String.valueOf(page) + "&limit=" + String.valueOf(limit) + "&username=" + BaseApplication.getgUsername(), new Callback() {
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

    static class CommunicationHandler extends Handler {
        WeakReference<CommunicationFragment> mWeakReference;

        public CommunicationHandler(CommunicationFragment fragment) {
            mWeakReference = new WeakReference<CommunicationFragment>(fragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            final CommunicationFragment fragment = mWeakReference.get();
            if (fragment != null) {

                if (msg.what == 0) {
                    Toast.makeText(fragment.getContext(), "网络错误", Toast.LENGTH_SHORT).show();
                } else {
                    String str = msg.getData().getString("first");
                    Gson gson = new Gson();
                    MomentListBean momentListBean = gson.fromJson(str, MomentListBean.class);
                    fragment.list = momentListBean.getMomentList();
                    if (fragment.list != null && fragment.list.size() > 0) {
                        fragment.communicationListViewAdapter.addlist(fragment.list);
                    } else {
                        Toast.makeText(fragment.getContext(), "没有", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
    }

}
