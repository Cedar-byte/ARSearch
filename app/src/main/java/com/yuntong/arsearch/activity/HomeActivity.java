package com.yuntong.arsearch.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wikitude.architect.ArchitectView;
import com.yuntong.arsearch.R;
import com.yuntong.arsearch.app.AppManager;
import com.yuntong.arsearch.app.MyApplication;
import com.yuntong.arsearch.util.Constants;
import com.yuntong.arsearch.util.JsonUtil;
import com.yuntong.arsearch.util.NormalPostRequest;
import com.yuntong.arsearch.util.ToastUtil;
import com.yuntong.arsearch.wikitude.AbstractArchitectCamActivity;
import com.yuntong.arsearch.wikitude.ArchitectViewHolderInterface;
import com.yuntong.arsearch.wikitude.LocationProvider;
import com.yuntong.arsearch.wikitude.SamplePoiDetailActivity;
import com.yuntong.arsearch.wikitude.WikitudeSDKConstants;
import com.wikitude.architect.StartupConfiguration.CameraPosition;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 首页
 */
public class HomeActivity extends AbstractArchitectCamActivity implements View.OnClickListener {

    private String ATTR_ID = "id";
    private String ATTR_NAME = "name";
    private String ATTR_DESCRIPTION = "description";
    private String ATTR_LATITUDE = "latitude";
    private String ATTR_LONGITUDE = "longitude";
    private String ATTR_ALTITUDE = "altitude";

    private long lastCalibrationToastShownTimeMillis = System.currentTimeMillis();
    private static final int WIKITUDE_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 3;
    private long exitTime = 0;// 退出时间
    private DrawerLayout drawerLayout;// 侧滑
    private LinearLayout linear_history, linear_set;

    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        linear_history = (LinearLayout) findViewById(R.id.linear_history);
        linear_history.setOnClickListener(this);
        linear_set = (LinearLayout) findViewById(R.id.linear_set);
        linear_set.setOnClickListener(this);
        initToolbar(R.color.color_transparent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_history:
                startActivity(new Intent(HomeActivity.this, HistoryActivity.class));
                break;
            case R.id.linear_set:
                startActivity(new Intent(HomeActivity.this, SettingUpActivity.class));
                break;
        }
    }

    /**
     * 引入Js代码文件
     */
    @Override
    public String getARchitectWorldPath() {
        return "6_Browsing$Pois_2_Adding$Radar" + File.separator + "index.html";
    }

    @Override
    public String getActivityTitle() {
        return null;
    }

    @Override
    public int getContentViewId() {
        AppManager.getInstance().addActivity(this);// 将本Activity添加到堆栈中
        return R.layout.activity_home;
    }

    @Override
    public int getArchitectViewId() {
        initView();
        return R.id.architectView;
    }

    @Override
    public String getWikitudeSDKLicenseKey() {
        return WikitudeSDKConstants.WIKITUDE_SDK_KEY;
    }

    @Override
    public ArchitectView.SensorAccuracyChangeListener getSensorAccuracyListener() {
        return new ArchitectView.SensorAccuracyChangeListener() {
            @Override
            public void onCompassAccuracyChanged( int accuracy ) {
				/* UNRELIABLE = 0, LOW = 1, MEDIUM = 2, HIGH = 3 */
                if ( accuracy < SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM
                        && HomeActivity.this != null
                        && !HomeActivity.this.isFinishing()
                        && System.currentTimeMillis() - HomeActivity.this.lastCalibrationToastShownTimeMillis > 5 * 1000) {
                    Toast.makeText( HomeActivity.this, R.string.compass_accuracy_low, Toast.LENGTH_LONG ).show();
                    HomeActivity.this.lastCalibrationToastShownTimeMillis = System.currentTimeMillis();
                }
            }
        };
    }

    @Override
    public ArchitectView.ArchitectUrlListener getUrlListener() {
        return new ArchitectView.ArchitectUrlListener() {

            @Override
            public boolean urlWasInvoked(String uriString) {
                Uri invokedUri = Uri.parse(uriString);
                // pressed "More" button on POI-detail panel
                if ("markerselected".equalsIgnoreCase(invokedUri.getHost())) {
                    final Intent poiDetailIntent = new Intent(HomeActivity.this, SceneActivity.class);
                    poiDetailIntent.putExtra(Constants.EXTRAS_KEY_POI_ID, String.valueOf(invokedUri.getQueryParameter("id")) );
                    poiDetailIntent.putExtra(Constants.EXTRAS_KEY_POI_TITILE, String.valueOf(invokedUri.getQueryParameter("title")) );
                    poiDetailIntent.putExtra(Constants.EXTRAS_KEY_POI_DESCR, String.valueOf(invokedUri.getQueryParameter("description")) );
                    HomeActivity.this.startActivity(poiDetailIntent);
                    return true;
                }
                // pressed snapshot button. check if host is button to fetch e.g. 'architectsdk://button?action=captureScreen', you may add more checks if more buttons are used inside AR scene
                else if ("button".equalsIgnoreCase(invokedUri.getHost())) {
                    HomeActivity.this.architectView.captureScreen(ArchitectView.CaptureScreenCallback.CAPTURE_MODE_CAM_AND_WEBVIEW, new ArchitectView.CaptureScreenCallback() {

                        @Override
                        public void onScreenCaptured(final Bitmap screenCapture) {
//                            if ( ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
//                                HomeActivity.this.screenCapture = screenCapture;
//                                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WIKITUDE_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
//                            } else {
//                                HomeActivity.this.saveScreenCaptureToExternalStorage(screenCapture);
//                            }
                        }
                    });
                }
                return true;
            }
        };
    }

    @Override
    protected boolean hasGeo() {
        return true;
    }

    @Override
    protected boolean hasIR() {
        return false;
    }

    @Override
    public ILocationProvider getLocationProvider(LocationListener locationListener) {
        return new LocationProvider(this, locationListener);
    }

    @Override
    public float getInitialCullingDistanceMeters() {
        return ArchitectViewHolderInterface.CULLING_DISTANCE_DEFAULT_METERS;
    }

    @Override
    protected CameraPosition getCameraPosition() {
        return CameraPosition.DEFAULT;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getScenes();// 请求全景数据
    }

    /** 请求全景数据 */
    private void getScenes() {
        RequestQueue requestQueue = MyApplication.getInstance().requestQueue;
        Map<String, String> map = new HashMap<>();
        map.put("cityCode","hangzhou");
        map.put("lng","120.25332");
        map.put("lat","30.209424");
        map.put("distance","1000");
        Request<JSONObject> request = new NormalPostRequest(Constants.SCENELIST_URL,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                if (JsonUtil.getStr(object, "status").equals("1")){
                    JSONObject model = JsonUtil.getJsonObj(object, "model");
                    JSONObject sceneModel = JsonUtil.getJsonObj(model, "sceneModel");
                    JSONArray data = JsonUtil.convertJsonArry(sceneModel, "data");
                    JSONArray pois = new JSONArray();
                    for (int i = 0;i <= data.length(); i++) {
                        HashMap<String, String> poiInformation = new HashMap<>();
                        JSONObject json=JsonUtil.convertJsonObj(data,i);
                        String id = JsonUtil.getStr(json, "sceneId") + "/" + JsonUtil.getStr(json, "panoId");
                        poiInformation.put(ATTR_ID, id);
                        poiInformation.put(ATTR_NAME, JsonUtil.getStr(json, "name"));
                        poiInformation.put(ATTR_DESCRIPTION, JsonUtil.getStr(json, "fisheyeThumb"));
                        poiInformation.put(ATTR_LATITUDE, JsonUtil.getStr(json, "lat"));
                        poiInformation.put(ATTR_LONGITUDE, JsonUtil.getStr(json, "lng"));
                        float UNKNOWN_ALTITUDE = -32768f;
                        poiInformation.put(ATTR_ALTITUDE, String.valueOf(UNKNOWN_ALTITUDE));
                        pois.put(new JSONObject(poiInformation));
                    }
                    injectData(pois);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showImgToast(HomeActivity.this, "服务器异常！", R.drawable.icon_not_net);
            }
        }, map);
        requestQueue.add(request);
    }

    /** 图文Toast */
    protected void showImgToast(Context c, String s, int imgId){
        View view = getLayoutInflater().inflate(R.layout.toast_bg_layout, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_toast);
        tv.setText(s);
        ImageView iv = (ImageView) view.findViewById(R.id.img_toast);
        iv.setImageResource(imgId);
        Toast toast = new Toast(c);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    /** Toolbar */
    private void initToolbar(int colorId){
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool);
        toolbar.setBackgroundColor(getResources().getColor(colorId));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                this.exitApp();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exitApp() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            ToastUtil.showToast(HomeActivity.this, getString(R.string.again_according_to_the_exit));
        } else {
            AppManager.getInstance().appExit(HomeActivity.this);
        }
        exitTime = System.currentTimeMillis();
    }
}
