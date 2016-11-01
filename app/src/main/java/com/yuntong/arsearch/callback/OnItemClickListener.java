package com.yuntong.arsearch.callback;

import android.view.View;

/**
 * RecyclerView的item点击事件接口
 */
public interface OnItemClickListener {

    void onItemClick(View view, int position);

    void onItemLongClick(View view , int position);
}
