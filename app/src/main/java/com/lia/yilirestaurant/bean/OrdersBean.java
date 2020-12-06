package com.lia.yilirestaurant.bean;

import java.util.List;

public class OrdersBean {
    String orderId;
    String name;
    String imgUrl;
    List<OrderItemBean> orderItem;
    String createTime;
    String price;
    String des;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public List<OrderItemBean> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(List<OrderItemBean> orderItem) {
        this.orderItem = orderItem;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
