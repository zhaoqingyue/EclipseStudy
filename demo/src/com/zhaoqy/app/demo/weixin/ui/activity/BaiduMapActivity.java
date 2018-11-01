package com.zhaoqy.app.demo.weixin.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.weixin.ui.dialog.FlippingDialog;

public class BaiduMapActivity extends Activity implements OnClickListener
{
	public static BDLocation lastLocation = null;
	public static BaiduMapActivity instance = null;
	public static MapView mMapView = null;
	private LocationClient      mLocClient;
	private MyLocationListenner myListener = new MyLocationListenner();
	private TextView            mRight;
	private TextView            mTitle;
	private ImageView           mBack;
	private BaiduMap            mBaiduMap;
	private FlippingDialog      mLoadingDialog;
	private LocationMode        mCurrentMode;

	/**
	 * 构造广播监听类，监听 SDK key 验证以及网络异常广播
	 */
	public class BaiduSDKReceiver extends BroadcastReceiver 
	{
		public void onReceive(Context context, Intent intent) 
		{
			String s = intent.getAction();
			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) 
			{
				Toast.makeText(instance, "key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置", Toast.LENGTH_SHORT).show();
			} 
			else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) 
			{
				Toast.makeText(instance, "网络出错", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private BaiduSDKReceiver mBaiduReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		instance = this;
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext
		//注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_weixin_baidumap);
		
		initView();
		initData();
		initListener();
		register();
		setBaiduMap();
	}

	private void initView() 
	{
		mMapView = (MapView) findViewById(R.id.bmapView);
		mRight = (TextView) findViewById(R.id.id_title_right_text);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
	}

	private void initData() 
	{
		mRight.setText("发送");
		mRight.setVisibility(View.VISIBLE);
		mTitle.setText("位置");
		mBack.setVisibility(View.VISIBLE);
	}

	private void initListener() 
	{
		mRight.setOnClickListener(this);
		mBack.setOnClickListener(this);
	}
	
	private void register()
	{
		//注册 SDK 广播监听者
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mBaiduReceiver = new BaiduSDKReceiver();
		registerReceiver(mBaiduReceiver, iFilter);
	}
	
	private void setBaiduMap() 
	{
		Intent intent = getIntent();
		double latitude = intent.getDoubleExtra("latitude", 0);
		mCurrentMode = LocationMode.NORMAL;
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		mBaiduMap.setMyLocationEnabled(true);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15.0f));
		mMapView.setLongClickable(true);
		if (latitude == 0) 
		{
			BaiduMapOptions mapoption = new BaiduMapOptions();
			mMapView = new MapView(this, mapoption);
			mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, null));
			showMapWithLocationClient();
		}
		else 
		{
			double longtitude = intent.getDoubleExtra("longitude", 0);
			String address = intent.getStringExtra("address");
			LatLng p = new LatLng(latitude, longtitude);
			mMapView = new MapView(this, new BaiduMapOptions().mapStatus(new MapStatus.Builder().target(p).build()));
			showMap(latitude, longtitude, address);
		}
	}

	private void showMap(double latitude, double longtitude, String address) 
	{
		mRight.setVisibility(View.GONE);
		LatLng llA = new LatLng(latitude, longtitude);
		CoordinateConverter converter = new CoordinateConverter();
		converter.coord(llA);
		converter.from(CoordinateConverter.CoordType.COMMON);
		LatLng convertLatLng = converter.convert();
		OverlayOptions ooA = new MarkerOptions().position(convertLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka)).zIndex(4).draggable(true);
		mBaiduMap.addOverlay(ooA);
		mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 17.0f));
	}

	private void showMapWithLocationClient() 
	{
		mLoadingDialog = new FlippingDialog(this, "正在确定你的位置...");
		mLoadingDialog.show();
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("gcj02");
		option.setScanSpan(30000);
		option.setAddrType("all");
		mLocClient.setLocOption(option);
	}

	@Override
	protected void onPause() 
	{
		mMapView.onPause();
		if (mLocClient != null) 
		{
			mLocClient.stop();
		}
		super.onPause();
		lastLocation = null;
	}

	@Override
	protected void onResume() 
	{
		mMapView.onResume();
		if (mLocClient != null) 
		{
			mLocClient.start();
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() 
	{
		if (mLocClient != null)
		{
			mLocClient.stop();
		}
		mMapView.onDestroy();
		unregisterReceiver(mBaiduReceiver);
		super.onDestroy();
	}

	/**
	 * 监听函数，有新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener 
	{
		@Override
		public void onReceiveLocation(BDLocation location) 
		{
			if (location == null) 
			{
				return;
			}
			Log.d("map", "On location change received:" + location);
			Log.d("map", "addr:" + location.getAddrStr());
			mRight.setEnabled(true);
			if (mLoadingDialog != null) 
			{
				mLoadingDialog.dismiss();
			}

			if (lastLocation != null) 
			{
				if (lastLocation.getLatitude() == location.getLatitude() && lastLocation.getLongitude() == location.getLongitude()) 
				{
					Log.d("map", "same location, skip refresh");
					// mMapView.refresh(); //need this refresh?
					return;
				}
			}
			lastLocation = location;
			mBaiduMap.clear();
			LatLng llA = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
			CoordinateConverter converter = new CoordinateConverter();
			converter.coord(llA);
			converter.from(CoordinateConverter.CoordType.COMMON);
			LatLng convertLatLng = converter.convert();
			OverlayOptions ooA = new MarkerOptions().position(convertLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka)).zIndex(2).draggable(true);
			mBaiduMap.addOverlay(ooA);
			mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 17.0f));
		}

		public void onReceivePoi(BDLocation poiLocation) 
		{
			if (poiLocation == null) 
			{
				return;
			}
		}
	}

	public class NotifyLister extends BDNotifyListener 
	{
		public void onNotify(BDLocation mlocation, float distance) 
		{
		}
	}

	public void back(View v) 
	{
		finish();
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_title_left_img:
		{
			finish();
			break;
		}
		case R.id.id_title_right_text:
		{
			if (lastLocation != null) 
			{
				Intent intent = getIntent();
				intent.putExtra("latitude", lastLocation.getLatitude());
				intent.putExtra("longitude", lastLocation.getLongitude());
				intent.putExtra("address", lastLocation.getAddrStr());
				setResult(RESULT_OK, intent);
				finish();
			}
			break;
		}
		default:
			break;
		}
	}
}
