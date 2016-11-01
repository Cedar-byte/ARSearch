package com.yuntong.arsearch.itemspace;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2016/6/5.
 * RecyclerView 项间距
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration{

    int mSpace;

    /**
     * @param mSpace 传入的值，其单位视为dp
     */
    public SpaceItemDecoration(int mSpace) {
        this.mSpace = mSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if(parent.getChildAdapterPosition(view) != 0)
            outRect.top = mSpace;
    }
}

