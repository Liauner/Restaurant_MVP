package com.lia.yilirestaurant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lia.yilirestaurant.R;
import com.lia.yilirestaurant.bean.OrderItemBean;

import java.util.List;

public class OrderDetailAdapter extends BaseAdapter {
    List<OrderItemBean> pList;
    Context pContext;

    public OrderDetailAdapter(List<OrderItemBean> list, Context context) {
        this.pList = list;
        this.pContext = context;
    }


    @Override
    public int getCount() {
        return pList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(pContext);
        OrderDetailViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new OrderDetailViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_orderdetail, parent, false);
            mViewHolder.pNameTv = convertView.findViewById(R.id.tv_itemorderdetail_name);
            mViewHolder.pCountTv = convertView.findViewById(R.id.tv_itemorderdetail_count);
            mViewHolder.pPriceTv = convertView.findViewById(R.id.tv_itemorderdetail_price);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (OrderDetailViewHolder) convertView.getTag();
        }
        mViewHolder.pNameTv.setText(pList.get(position).getFoodName());
        mViewHolder.pCountTv.setText(pList.get(position).getCount() + "份");
        mViewHolder.pPriceTv.setText(pList.get(position).getPrice() + "元/份");
        return convertView;
    }

    static class OrderDetailViewHolder {
        TextView pNameTv;
        TextView pPriceTv;
        TextView pCountTv;
    }


}
