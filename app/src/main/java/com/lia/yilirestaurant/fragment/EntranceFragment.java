package com.lia.yilirestaurant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lia.yilirestaurant.listener.FragmentListener;
import com.lia.yilirestaurant.R;
import com.lia.yilirestaurant.activity.SignInActivity;
import com.lia.yilirestaurant.util.BaseApplication;

public class EntranceFragment extends Fragment {
    FragmentListener fragmentListener;
    Button pPostBtn;
    int pFragmentFlag;

    public EntranceFragment(FragmentListener fragmentListener, int pFragmentFlag) {
        this.fragmentListener = fragmentListener;
        this.pFragmentFlag = pFragmentFlag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_entrance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pPostBtn = view.findViewById(R.id.btn_entrance_post);
        pPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                startActivityForResult(intent, pFragmentFlag);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!BaseApplication.getgUsername().isEmpty()) {
            if (requestCode == 0) {
                fragmentListener.onSwitchToAnotherFragment(0, 0);
            } else if (requestCode == 1) {
                fragmentListener.onSwitchToAnotherFragment(0, 1);
            }

        }
    }
}
