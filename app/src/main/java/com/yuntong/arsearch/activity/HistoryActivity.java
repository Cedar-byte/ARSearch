package com.yuntong.arsearch.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gxy.recycler.ProgressStyle;
import com.gxy.recycler.XRecyclerView;
import com.yuntong.arsearch.R;
import com.yuntong.arsearch.adapter.HistoryAdapter;
import com.yuntong.arsearch.app.MyApplication;
import com.yuntong.arsearch.base.BaseActivity;
import com.yuntong.arsearch.bean.HistoryBean;
import com.yuntong.arsearch.bean.SceneListData;
import com.yuntong.arsearch.callback.ItemClickListener;
import com.yuntong.arsearch.callback.OnItemClickListener;
import com.yuntong.arsearch.itemspace.SpaceItemDecoration;
import com.yuntong.arsearch.util.Constants;
import com.yuntong.arsearch.util.JsonUtil;
import com.yuntong.arsearch.util.NormalPostRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 浏览记录
 */
public class HistoryActivity extends BaseActivity{

    private XRecyclerView recyclerView;
    private HistoryAdapter adapter;
    private List<HistoryBean> list = new ArrayList<>();
    private int page, page_total;
    private boolean isRefresh, isLoadingMore = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_history;
    }

    @Override
    protected void initData() {
        adapter = new HistoryAdapter(this, list);
    }

    @Override
    protected void initView() {
        initToolbar("历史记录", R.color.color_green);
        recyclerView = (XRecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);// 如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setArrowImageView(R.drawable.icon_recyclerview_down);
        recyclerView.addItemDecoration(new SpaceItemDecoration((int) getResources().getDimension(R.dimen.item_space)));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void bindView() {
        // 第一次获取数据
        page = 1;
        getHistoryData(page);

        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {

            @Override
            public void onRefresh() {
                isRefresh = true;// 表示当前操作是下拉刷新
                page = 1;
                recyclerView.setNoMore(false);// 刷新时设置当前可加载更多
                getHistoryData(page);
            }

            @Override
            public void onLoadMore() {
                isLoadingMore = true;// 表示当前的操作是正在加载更多
                if (page == page_total) {
                    recyclerView.noMoreLoading();
                } else {
                    page ++;
                    getHistoryData(page);
                }
            }
        });

        recyclerView.addOnItemTouchListener(new ItemClickListener(recyclerView, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showToast("单机");
            }

            @Override
            public void onItemLongClick(View view, int position) {
                showToast("长按");
            }
        }));
    }

    private void getHistoryData(int page) {
        RequestQueue requestQueue = MyApplication.getInstance().requestQueue;
        Map<String, String> map = new HashMap<>();
        map.put("pageSize", "20");
        map.put("currentPage",String.valueOf(page));
        Request<JSONObject> request = new NormalPostRequest(Constants.HISTORYDATA_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                Log.i("--------------------", "onResponse: " + object.toString());
                if (JsonUtil.getStr(object, "status").equals("1")){
                    if(isRefresh){
                        list.clear();
                    }
                    JSONObject object1 = JsonUtil.getJsonObj(object, "model");
                    // 获取总页数
                    JSONObject object2 = JsonUtil.getJsonObj(object1, "page");
                    String total = JsonUtil.getStr(object2, "totalPage");
                    page_total = Integer.valueOf(total);
                    // 获取全景数据
                    Gson gson = new Gson();
                    List<HistoryBean> type = gson.fromJson(JsonUtil.convertJsonArry(object1,"list").toString(),
                            new TypeToken<ArrayList<HistoryBean>>(){}.getType());
                    list.addAll(type);
                    adapter.notifyDataSetChanged();
                    // 判断加载完成
                    if(isRefresh){
                        recyclerView.refreshComplete();
                        isRefresh = false;
                    }
                    if(isLoadingMore){
                        recyclerView.loadMoreComplete();
                        isLoadingMore = false;
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showImgToast(HistoryActivity.this, "服务器异常！", R.drawable.icon_not_net);
            }
        }, map);
        requestQueue.add(request);
    }

    @Override
    protected void widgetClick(View v) {

    }
}
