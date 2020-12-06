package com.lia.yilirestaurant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lia.yilirestaurant.R;
import com.lia.yilirestaurant.bean.MomentBean;

import java.util.List;

public class CommunicationListViewAdapter extends BaseAdapter {

    List<MomentBean> pList;
    Context pContext;

    public CommunicationListViewAdapter(List<MomentBean> pList, Context pContext) {
        this.pList = pList;
        this.pContext = pContext;
    }

    public void addlist(List<MomentBean> list) {
        this.pList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearlist() {
        this.pList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return pList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(pContext);
        final int viewPosition = position;
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_communication, parent, false);
            mViewHolder = new ViewHolder();
            mViewHolder.pContentTv = convertView.findViewById(R.id.tv_communication_content);
            mViewHolder.pShareCountTv = convertView.findViewById(R.id.tv_communication_shareCount);
            mViewHolder.pLikeCountTv = convertView.findViewById(R.id.tv_communication_likeCount);
            mViewHolder.pCommunicationTv = convertView.findViewById(R.id.tv_communication_commentCount);
            mViewHolder.pPicturesRv = convertView.findViewById(R.id.rv_communication_pictures);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.pContentTv.setText(pList.get(position).getContent());
        mViewHolder.pShareCountTv.setText(pList.get(position).getShareCount());
        mViewHolder.pLikeCountTv.setText(pList.get(position).getLikeCount());
        mViewHolder.pCommunicationTv.setText(pList.get(position).getCommentCount());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(pContext, 6);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int result = 2;
                switch (pList.get(viewPosition).getImgUrls().size()) {
                    case 1:
                        result = 6;
                        break;
                    case 2:
                    case 4:
                        result = 3;
                        break;
                    case 3:
                    default:
                        result = 2;
                        break;
                }
                return result;
            }
        });
        CommunicationPicturesAdapter communicationPicturesAdapter = new CommunicationPicturesAdapter(pList.get(position).getImgUrls(), pContext);
        mViewHolder.pPicturesRv.setLayoutManager(gridLayoutManager);
        mViewHolder.pPicturesRv.setAdapter(communicationPicturesAdapter);
        return convertView;
    }

    static class ViewHolder {
        TextView pContentTv, pShareCountTv, pLikeCountTv, pCommunicationTv;
        RecyclerView pPicturesRv;
    }

}
