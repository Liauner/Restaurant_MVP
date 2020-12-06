package com.lia.yilirestaurant.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class BaseApplication extends Application {
     static Context gGontext;
    private static String gUsername;
    private static String gPassword;

    @Override
    public void onCreate() {
        super.onCreate();
        gGontext=getApplicationContext();
        SharedPreferences sharedPreferences=getSharedPreferences("userinfo",MODE_PRIVATE);
        gUsername=sharedPreferences.getString("userName","");
        gPassword=sharedPreferences.getString("passWord","");
    }

    public static void saveUser(String username, String password){
        SharedPreferences sharedPreferences= gGontext.getSharedPreferences("userinfo",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("userName",username);
        editor.putString("passWord",password);
        editor.commit();
        gUsername=username;
        gPassword=password;
    }

    public static String getgUsername() {
        return gUsername;
    }

    public static void setgUsername(String gUsername) {
        BaseApplication.gUsername = gUsername;
    }

    public static String getgPassword() {
        return gPassword;
    }

    public static void setgPassword(String gPassword) {
        BaseApplication.gPassword = gPassword;
    }
}
