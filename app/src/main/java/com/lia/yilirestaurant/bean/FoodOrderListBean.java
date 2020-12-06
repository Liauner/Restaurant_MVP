package com.lia.yilirestaurant.bean;

import java.util.List;

public class FoodOrderListBean {
    InfoBean info;
    List<OrdersBean> orders;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public List<OrdersBean> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersBean> orders) {
        this.orders = orders;
    }
}
