package com.lia.yilirestaurant.bean;

import java.util.List;

public class MomentListBean {
    InfoBean info;
    List<MomentBean> momentList;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public List<MomentBean> getMomentList() {
        return momentList;
    }

    public void setMomentList(List<MomentBean> momentList) {
        this.momentList = momentList;
    }
}
