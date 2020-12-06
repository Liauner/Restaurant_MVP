package com.lia.yilirestaurant.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.lia.yilirestaurant.R;

public class DepositDialog extends Dialog implements View.OnClickListener {

    Button pPostBtn;
    EditText pValueEt;
    DepositButtonListener depositButtonListener;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_deposit_post) {
            depositButtonListener.onClick(pValueEt.getText().toString(), this);
        }
    }

    public interface DepositButtonListener {
        void onClick(String depositValue, DepositDialog depositDialog);
    }


    public DepositDialog(@NonNull Context context, DepositButtonListener depositButtonListener) {
        super(context);
        this.depositButtonListener = depositButtonListener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_deposit);
        pPostBtn = findViewById(R.id.btn_deposit_post);
        pValueEt = findViewById(R.id.et_deposit_value);
        pPostBtn.setOnClickListener(this);
    }
}
