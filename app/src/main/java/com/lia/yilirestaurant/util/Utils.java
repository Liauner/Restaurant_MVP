package com.lia.yilirestaurant.util;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.Display;
import android.view.Window;

public class Utils {
    //获取导航栏高度
    static int getStatusBar(Activity activity){
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen","android");
        return resources.getDimensionPixelSize(resourceId);
    }
    //获取ActionBar高度
    static int getActionBar(Activity activity){
        int contentTop =activity. getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        return contentTop - getStatusBar(activity);
    }

    static int getScreenHeight(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        return display.getHeight();
    }
    public static int getScreenWidth(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        return display.getWidth();
    }
    public static int dip2px(Activity activity, float dpValue) {
        float scale = activity.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
