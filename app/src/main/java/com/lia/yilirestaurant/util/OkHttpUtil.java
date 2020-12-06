package com.lia.yilirestaurant.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtil  {

    public static void sendGetRequest(String address,okhttp3.Callback callback){
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder().url(address).addHeader("Content-Type","application/x-www-form-urlencoded").get().build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void sendPostRequest(String address, RequestBody body, okhttp3.Callback callback){
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder().url(address).addHeader("Content-Type","application/x-www-form-urlencoded").post(body).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void handleResponse(Call call, Response response, int msgWhat, Handler handler) throws IOException {
        if (response.isSuccessful()){
            String str=response.body().string();
            Message message=new Message();
            Bundle bundle=new Bundle();
            bundle.putString("first",str);
            message.setData(bundle);
            message.what=msgWhat;
            handler.sendMessage(message);
        }
    }


}
