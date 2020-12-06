package com.lia.yilirestaurant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lia.yilirestaurant.R;

public class SplashImageFragment extends Fragment {
    private ImageView pImageIv;
    private int pImageId;

    public SplashImageFragment(int pImageId) {
        super();
        this.pImageId = pImageId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pImageIv = view.findViewById(R.id.im_fragment_splash_image);
        pImageIv.setImageResource(pImageId);

    }
}
