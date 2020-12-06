package com.lia.yilirestaurant.listener;

import java.util.List;

public interface PermissionListener {
    void onGranted();//已授权

    void onDenied(List<String> deniedPermission);//未授权
}
