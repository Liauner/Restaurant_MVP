package com.lia.yilirestaurant.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;

public class LoadingDialog extends ProgressDialog {

    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    public static LoadingDialog Build( Context context){
        LoadingDialog loadingDialog=new LoadingDialog(context);
        loadingDialog.setMessage("加载中...");
        loadingDialog.setCancelable(false);
        return loadingDialog;
    }
}
