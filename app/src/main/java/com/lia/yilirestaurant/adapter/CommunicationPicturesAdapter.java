package com.lia.yilirestaurant.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lia.yilirestaurant.R;
import com.lia.yilirestaurant.util.Utils;

import java.util.List;

public class CommunicationPicturesAdapter extends RecyclerView.Adapter<CommunicationPicturesAdapter.CommunicationPicturesViewHolder> {

    List<String> pPictureList;
    Context pContext;
    Activity pActivity;

    public CommunicationPicturesAdapter(List<String> pPictureList, Context pContext) {
        this.pPictureList = pPictureList;
        this.pContext = pContext;
        this.pActivity = (Activity) pContext;
    }


    @NonNull
    @Override
    public CommunicationPicturesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(pContext);
        View view = layoutInflater.inflate(R.layout.item_communication_pictures, parent, false);
        CommunicationPicturesViewHolder viewHolder = new CommunicationPicturesViewHolder(view);
        ViewGroup.LayoutParams params = viewHolder.pPictuerIv.getLayoutParams();
        int inSize = 0;
        int with = 0;
        int space = 0;
        int numb = 0;
        switch (pPictureList.size()) {
            case 1:
                inSize = Utils.getScreenWidth(pActivity);
                break;
            case 2:
            case 4:
                with = Utils.getScreenWidth(pActivity);
                space = Utils.dip2px(pActivity, 32);
                numb = 2;//Utils.dip2px(pActivity,2.2f);
                inSize = (with - space) / numb;
                break;
            case 3:
            default:
                with = Utils.getScreenWidth(pActivity);
                space = Utils.dip2px(pActivity, 34);
                numb = 3;//Utils.dip2px(pActivity,3.2f);
                inSize = (with - space) / numb;
                break;
        }
        params.height = inSize;
        params.width = inSize;
        viewHolder.pPictuerIv.setLayoutParams(params);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommunicationPicturesViewHolder holder, int position) {
        Glide.with(pContext).load(pPictureList.get(position)).placeholder(R.mipmap.placeholder).error(R.mipmap.error).into(holder.pPictuerIv);
    }


    @Override
    public int getItemCount() {
        return pPictureList.size();
    }

    static class CommunicationPicturesViewHolder extends RecyclerView.ViewHolder {
        ImageView pPictuerIv;


        public CommunicationPicturesViewHolder(@NonNull View itemView) {
            super(itemView);
            pPictuerIv = itemView.findViewById(R.id.iv_communitcationitem_picture);
        }
    }

}
