package com.yuntong.arsearch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuntong.arsearch.R;
import com.yuntong.arsearch.activity.HistoryActivity;
import com.yuntong.arsearch.bean.HistoryBean;
import com.yuntong.arsearch.util.CommonUtil;
import com.yuntong.arsearch.util.TimeUtil;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{

    private Context context;
    private List<HistoryBean> list = null;

    public HistoryAdapter(Context context, List<HistoryBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_history,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HistoryAdapter.ViewHolder holder, int position) {
        HistoryBean bean = list.get(position);
        Glide.with(context).load(bean.getThumbUrl()).into(holder.bgImg);
        holder.nameTv.setText(bean.getSceneName());
        holder.timeTv.setText(TimeUtil.getTimeto(Long.valueOf(bean.getCreateTime())));
        holder.addressTv.setText(bean.getSceneAdress());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView bgImg;

        public TextView nameTv, timeTv, addressTv;

        public ViewHolder(View view){
            super(view);
            bgImg = (ImageView) view.findViewById(R.id.img_item_bg);
            nameTv = (TextView) view.findViewById(R.id.tv_item_name);
            timeTv = (TextView) view.findViewById(R.id.tv_item_time);
            addressTv = (TextView) view.findViewById(R.id.tv_item_address);
        }
    }
}
