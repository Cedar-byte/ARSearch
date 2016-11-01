package com.yuntong.arsearch.wikitude;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.location.Location;
import android.location.LocationListener;
import android.media.AudioManager;
import android.opengl.GLES20;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wikitude.architect.ArchitectView;
import com.wikitude.architect.ArchitectView.ArchitectUrlListener;
import com.wikitude.architect.ArchitectView.SensorAccuracyChangeListener;
import com.wikitude.architect.StartupConfiguration;
import com.wikitude.architect.StartupConfiguration.CameraPosition;
import com.yuntong.arsearch.R;
import com.yuntong.arsearch.activity.HomeActivity;
import com.yuntong.arsearch.app.MyApplication;
import com.yuntong.arsearch.bean.SceneListData;
import com.yuntong.arsearch.util.Constants;
import com.yuntong.arsearch.util.JsonUtil;
import com.yuntong.arsearch.util.NormalPostRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract activity which handles live-cycle events.
 * 抽象的活动处理live-cycle事件
 * Feel free to extend from this activity when setting up your own AR-Activity
 * 随意扩展设置自己的AR-Activity时从这个活动
 */
public abstract class AbstractArchitectCamActivity extends AppCompatActivity implements ArchitectViewHolderInterface{

	protected ArchitectView architectView;
	protected SensorAccuracyChangeListener sensorAccuracyListener;
	protected Location lastKnownLocaton;
	protected ILocationProvider				locationProvider;
	protected LocationListener locationListener;
	protected ArchitectUrlListener urlListener;
	protected JSONArray poiData;
	protected boolean isLoading = false;
	protected List<SceneListData> list = new ArrayList<>();

	@SuppressLint("NewApi")
	@Override
	public void onCreate( final Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		/* pressing volume up/down should cause music volume changes */
		this.setVolumeControlStream( AudioManager.STREAM_MUSIC );

		/* set samples content view */
		this.setContentView( this.getContentViewId() );
		
		this.setTitle( this.getActivityTitle() );
		
		/*  
		 *	this enables remote debugging of a WebView on Android 4.4+ when debugging = true in AndroidManifest.xml
		 *	If you get a compile time error here, ensure to have SDK 19+ used in your ADT/Eclipse.
		 *	You may even delete this block in case you don't need remote debugging or don't have an Android 4.4+ device in place.
		 *	Details: https://developers.google.com/chrome-developer-tools/docs/remote-debugging
		 */
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
		    if ( 0 != ( getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE ) ) {
		        WebView.setWebContentsDebuggingEnabled(true);
		    }
		}

		/* set AR-view for life-cycle notifications etc. */
		this.architectView = (ArchitectView)this.findViewById( this.getArchitectViewId()  );

		/* pass SDK key if you have one, this one is only valid for this package identifier and must not be used somewhere else */
		final StartupConfiguration config = new StartupConfiguration( this.getWikitudeSDKLicenseKey(), this.getFeatures(), this.getCameraPosition() );

		try {
			/* first mandatory life-cycle notification */
			this.architectView.onCreate( config );
		} catch (RuntimeException rex) {
			this.architectView = null;
			Toast.makeText(getApplicationContext(), "can't create Architect View", Toast.LENGTH_SHORT).show();
			Log.e(this.getClass().getName(), "Exception in ArchitectView.onCreate()", rex);
		}

		// set accuracy listener if implemented, you may e.g. show calibration prompt for compass using this listener
		this.sensorAccuracyListener = this.getSensorAccuracyListener();
		
		// set urlListener, any calls made in JS like "document.location = 'architectsdk://foo?bar=123'" is forwarded to this listener, use this to interact between JS and native Android activity/fragment
		this.urlListener = this.getUrlListener();  
		
		// register valid urlListener in architectView, ensure this is set before content is loaded to not miss any event
		if (this.urlListener != null && this.architectView != null) {
			this.architectView.registerUrlListener( this.getUrlListener() );
		}
		
		if (hasGeo()) {

			// listener passed over to locationProvider, any location update is handled here
			this.locationListener = new LocationListener() {

				@Override
				public void onStatusChanged(String provider, int status, Bundle extras ) {
				}

				@Override
				public void onProviderEnabled( String provider ) {
				}

				@Override
				public void onProviderDisabled( String provider ) {
				}

				@Override
				public void onLocationChanged( final Location location ) {
					// forward location updates fired by LocationProvider to architectView, you can set lat/lon from any location-strategy
					if (location!=null) {
					// sore last location as member, in case it is needed somewhere (in e.g. your adjusted project)
						AbstractArchitectCamActivity.this.lastKnownLocaton = location;
						if ( AbstractArchitectCamActivity.this.architectView != null ) {
							// check if location has altitude at certain accuracy level & call right architect method (the one with altitude information)
							if ( location.hasAltitude() && location.hasAccuracy() && location.getAccuracy()<7) {
								AbstractArchitectCamActivity.this.architectView.setLocation( location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getAccuracy() );
							} else {
								AbstractArchitectCamActivity.this.architectView.setLocation( location.getLatitude(), location.getLongitude(), location.hasAccuracy() ? location.getAccuracy() : 1000 );
							}
						}
					}
				}
			};

			// locationProvider used to fetch user position
			this.locationProvider = getLocationProvider( this.locationListener );
		} else {
			this.locationProvider = null;
			this.locationListener = null;
		}
	}

	protected abstract CameraPosition getCameraPosition();

	private int getFeatures() {
		int features = (hasGeo() ? StartupConfiguration.Features.Geo : 0) | (hasIR() ? StartupConfiguration.Features.Tracking2D : 0);
		return features;
	}

	protected abstract boolean hasGeo();
	protected abstract boolean hasIR();

	@Override
	protected void onPostCreate( final Bundle savedInstanceState ) {
		super.onPostCreate( savedInstanceState );
		
		if ( this.architectView != null ) {
			
			// call mandatory live-cycle method of architectView
			this.architectView.onPostCreate();
			
			try {
				// load content via url in architectView, ensure '<script src="architect://architect.js"></script>' is part of this HTML file, have a look at wikitude.com's developer section for API references
				this.architectView.load( this.getARchitectWorldPath() );

				if (this.getInitialCullingDistanceMeters() != ArchitectViewHolderInterface.CULLING_DISTANCE_DEFAULT_METERS) {
					// set the culling distance - meaning: the maximum distance to render geo-content
					this.architectView.setCullingDistance( this.getInitialCullingDistanceMeters() );
				}
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		// call mandatory live-cycle method of architectView
		if ( this.architectView != null ) {
			this.architectView.onResume();
			// register accuracy listener in architectView, if set
			if (this.sensorAccuracyListener!=null) {
				this.architectView.registerSensorAccuracyChangeListener( this.sensorAccuracyListener );
			}
		}

		// tell locationProvider to resume, usually location is then (again) fetched, so the GPS indicator appears in status bar
		if ( this.locationProvider != null ) {
			this.locationProvider.onResume();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		// call mandatory live-cycle method of architectView
		if ( this.architectView != null ) {
			this.architectView.onPause();
			
			// unregister accuracy listener in architectView, if set
			if ( this.sensorAccuracyListener != null ) {
				this.architectView.unregisterSensorAccuracyChangeListener( this.sensorAccuracyListener );
			}
		}
		
		// tell locationProvider to pause, usually location is then no longer fetched, so the GPS indicator disappears in status bar
		if ( this.locationProvider != null ) {
			this.locationProvider.onPause();
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// call mandatory live-cycle method of architectView
		if ( this.architectView != null ) {
			this.architectView.onDestroy();
		}
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		if ( this.architectView != null ) {
			this.architectView.onLowMemory();
		}
	}

	public abstract String getActivityTitle();

	@Override
	public abstract String getARchitectWorldPath();

	@Override
	public abstract ArchitectUrlListener getUrlListener();

	@Override
	public abstract int getContentViewId();

	@Override
	public abstract String getWikitudeSDKLicenseKey();

	@Override
	public abstract int getArchitectViewId();

	@Override
	public abstract ILocationProvider getLocationProvider(final LocationListener locationListener);

	@Override
	public abstract ArchitectView.SensorAccuracyChangeListener getSensorAccuracyListener();

	public static final boolean isVideoDrawablesSupported() {
		String extensions = GLES20.glGetString( GLES20.GL_EXTENSIONS );
		return extensions != null && extensions.contains( "GL_OES_EGL_image_external" );
	}

	protected void injectData(JSONArray pois) {
		poiData = pois;
		if (!isLoading) {
			final Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {

					isLoading = true;

					final int WAIT_FOR_LOCATION_STEP_MS = 2000;

//					while (lastKnownLocaton == null && !isFinishing()) {
//
//						runOnUiThread(new Runnable() {
//
//							@Override
//							public void run() {
//									Toast.makeText(AbstractArchitectCamActivity.this, R.string.location_fetching, Toast.LENGTH_SHORT).show();
//							}
//						});
//						try {
//							Thread.sleep(WAIT_FOR_LOCATION_STEP_MS);
//						} catch (InterruptedException e) {
//							break;
//						}
//					}
					
					if (lastKnownLocaton!=null && !isFinishing()) {
						Log.i("....................", "run: " + lastKnownLocaton);
						// TODO: you may replace this dummy implementation and instead load POI information e.g. from your database
//						poiData = getPoiInformation(lastKnownLocaton, 5);
						/**
                         * 与js交互，将poiData传递过去
						 */
						callJavaScript("World.loadPoisFromJsonData", new String[] { poiData.toString() });
					}
					isLoading = false;
				}
			});
			t.start();
		}
	}

	/** call JacaScript in architectView */
	private void callJavaScript(final String methodName, final String[] arguments) {
		final StringBuilder argumentsString = new StringBuilder("");
		for (int i= 0; i<arguments.length; i++) {
			argumentsString.append(arguments[i]);
			if (i<arguments.length-1) {
				argumentsString.append(", ");
			}
		}
		if (this.architectView!=null) {
			final String js = ( methodName + "( " + argumentsString.toString() + " );" );
			this.architectView.callJavascript(js);
		}
	}
	
	/**
	 * 获取poi信息
	 * loads poiInformation and returns them as JSONArray. Ensure attributeNames of JSON POIs are well known in JavaScript, so you can parse them easily
	 */
	public JSONArray getPoiInformation(final Location userLocation, final int numberOfPlaces) {
		
		if (userLocation==null) {
			return null;
		}

//		RequestQueue requestQueue = MyApplication.getInstance().requestQueue;
//		Map<String, String> map = new HashMap<>();
//		map.put("cityCode", "hangzhou");
//		map.put("lng", "120.253558");
//		map.put("lat", "30.208981");
//		map.put("distance", "50");
//		Request<JSONObject> request = new NormalPostRequest(Constants.SCENE_URL, new Response.Listener<JSONObject>() {
//			@Override
//			public void onResponse(JSONObject object) {
//				if(JsonUtil.getStr(object, "status").equals("1")){
//					JSONObject object1 = JsonUtil.getJsonObj(object, "model");
//					JSONObject object2 = JsonUtil.getJsonObj(object1, "sceneModel");// 商家列表
//					Gson gson = new Gson();
//					List<SceneListData> type = gson.fromJson(JsonUtil.convertJsonArry(object2,"data").toString(),
//							new TypeToken<ArrayList<SceneListData>>(){}.getType());
//					list.addAll(type);
//					Log.i("++++++TAG+++++++++++", "onResponse: " + list.toString());
//				}
//			}
//		}, new Response.ErrorListener() {
//			@Override
//			public void onErrorResponse(VolleyError volleyError) {
//
//			}
//		}, map);
//		requestQueue.add(request);

		final JSONArray pois = new JSONArray();
		
		// ensure these attributes are also used in JavaScript when extracting POI data
		final String ATTR_ID = "id";
		final String ATTR_NAME = "name";
		final String ATTR_DESCRIPTION = "description";
		final String ATTR_LATITUDE = "latitude";
		final String ATTR_LONGITUDE = "longitude";
		final String ATTR_ALTITUDE = "altitude";

		String[] images = {"http://img1.imgtn.bdimg.com/it/u=416519010,1706085007&fm=21&gp=0.jpg"
				,"http://www.qq745.com/uploads/allimg/141012/1-141012103344.jpg"
				,"http://image2.panocooker.com/group1/M00/D1/63/rBELClfiJZqAVAOiAAFYALSE71M379.png"
				,"http://image2.panocooker.com/group1/M00/D1/63/rBELClfiJZqAVAOiAAFYALSE71M379.png"
				,"http://image2.panocooker.com/group1/M00/D1/63/rBELClfiJZqAVAOiAAFYALSE71M379.png"};
		
		for (int i = 1;i <= numberOfPlaces; i++) {
			final HashMap<String, String> poiInformation = new HashMap<String, String>();
			poiInformation.put(ATTR_ID, String.valueOf(i));
			poiInformation.put(ATTR_NAME, "你好#" + i);
			poiInformation.put(ATTR_DESCRIPTION, images[i-1]);
			double[] poiLocationLatLon = getRandomLatLonNearby(userLocation.getLatitude(), userLocation.getLongitude());
			poiInformation.put(ATTR_LATITUDE, String.valueOf(poiLocationLatLon[0]));
			poiInformation.put(ATTR_LONGITUDE, String.valueOf(poiLocationLatLon[1]));
			final float UNKNOWN_ALTITUDE = -32768f;  // equals "AR.CONST.UNKNOWN_ALTITUDE" in JavaScript (compare AR.GeoLocation specification)
			// Use "AR.CONST.UNKNOWN_ALTITUDE" to tell ARchitect that altitude of places should be on user level. Be aware to handle altitude properly in locationManager in case you use valid POI altitude value (e.g. pass altitude only if GPS accuracy is <7m).
			poiInformation.put(ATTR_ALTITUDE, String.valueOf(UNKNOWN_ALTITUDE));
			pois.put(new JSONObject(poiInformation));
		}
		
		return pois;
	}
	
	/**
	 * helper for creation of dummy places.
	 * @param lat center latitude
	 * @param lon center longitude
	 * @return lat/lon values in given position's vicinity
	 */
	private static double[] getRandomLatLonNearby(final double lat, final double lon) {
		return new double[] { lat + Math.random()/5-0.1 , lon + Math.random()/5-0.1};
	}
}