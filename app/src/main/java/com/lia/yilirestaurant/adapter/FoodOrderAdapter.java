package com.lia.yilirestaurant.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lia.yilirestaurant.activity.OrderDetailActivity;
import com.lia.yilirestaurant.R;
import com.lia.yilirestaurant.bean.OrdersBean;

import java.util.ArrayList;
import java.util.List;

public class FoodOrderAdapter extends RecyclerView.Adapter<FoodOrderAdapter.FoodOrderViewHolder> {

    Context pContext;
    List<OrdersBean> pOrdersList = new ArrayList<>();

    public FoodOrderAdapter(Context pContext) {
        this.pContext = pContext;
    }

    public void addOrdersList(List<OrdersBean> ordersList) {
        pOrdersList.addAll(ordersList);
        notifyDataSetChanged();
    }

    public void clearOrdersList() {
        pOrdersList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_me, parent, false);
        return new FoodOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodOrderViewHolder holder, final int position) {
        holder.pNnameTv.setText(pOrdersList.get(position).getName());
        holder.pDesTv.setText(pOrdersList.get(position).getDes());
        holder.pPriceTv.setText(pOrdersList.get(position).getPrice() + "元");
        holder.pMeitemRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(pContext, OrderDetailActivity.class);
                intent.putExtra("orderId", pOrdersList.get(position).getOrderId());
                pContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pOrdersList.size();
    }

    public static class FoodOrderViewHolder extends RecyclerView.ViewHolder {

        TextView pNnameTv, pDesTv, pPriceTv;
        RelativeLayout pMeitemRl;

        public FoodOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            pNnameTv = itemView.findViewById(R.id.tv_meitem_name);
            pDesTv = itemView.findViewById(R.id.tv_meitem_des);
            pPriceTv = itemView.findViewById(R.id.tv_meitem_price);
            pMeitemRl = itemView.findViewById(R.id.rl_meitem);
        }
    }
}
