package com.atguigu.ditu3;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.atguigu.ditu3.activity.bean.addressdata;
import com.atguigu.ditu3.utils.Constant;
import com.atguigu.ditu3.utils.fasong;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;


public class MyTrackActivity extends Activity {
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private Boolean D=true;
	private 	List<LatLng> resultPoints;
	private 	List<LatLng> resultPoint;
	List<GpsInfo> infos;
	public MyTrackActivity() {
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if(D){
			Log.i("GPS�켣","onCreate()");
		}

		setContentView(R.layout.activity_track);
		super.onCreate(savedInstanceState);
		//初始化地图相关
		mMapView=(MapView)findViewById(R.id.bmapView);
		mBaiduMap=mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.0f);
		mBaiduMap.setMapStatus(msu);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02

		// option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		LocationClient locationClient = new LocationClient(getApplicationContext());
		locationClient.setLocOption(option);
		showTrack();
	}

	public void fanhui(View v){
		finish();

	}
	public void baocun(View v){
		resultPoint=new ArrayList<LatLng>();
		//获取数据库的数据
		GPSinfoDao dao=new GPSinfoDao(getApplicationContext());
		infos=dao.findAll();
		for(GpsInfo info:infos){
			double myLatitude=Double.parseDouble(info.getLatitude()) ;
			double myLongitude=Double.parseDouble(info.getLongitude());
			LatLng point=new LatLng(myLatitude, myLongitude);
			resultPoint.add(point);
			point=null;

		}
		infos=dao.findAll();
        String userphone=getIntent().getStringExtra("userph");
		String a=getIntent().getStringExtra("a");
		addressdata ziliao = new addressdata();
		ziliao.setData(a);
		ziliao.setMyphone(userphone);
		fasong.requestHttp(resultPoint,ziliao, Constant.ADD_ADDRESS,MyTrackActivity.this);
		finish();
	}
	/**
	 * ��ʾ�켣
	 */
	private void showTrack(){
		if(D){
			Log.i("GPS�켣","showTrack()");
		}
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				 resultPoints=new ArrayList<LatLng>();
				//获取数据库的数据
					GPSinfoDao dao=new GPSinfoDao(getApplicationContext());
					 infos=dao.findAll();
					for(GpsInfo info:infos){
					double myLatitude=Double.parseDouble(info.getLatitude()) ;
					double 	myLongitude=Double.parseDouble(info.getLongitude());
					LatLng point=new LatLng(myLatitude, myLongitude);
					resultPoints.add(point);
					point=null;
			
					}
					return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				     int kk=resultPoints.size();
				    if (kk>2) {
						mBaiduMap.clear();
						OverlayOptions option = new MarkerOptions().position(resultPoints.get(0))
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_point));
						mBaiduMap.addOverlay(option);
						OverlayOptions optionl = new MarkerOptions().position(resultPoints.get(resultPoints.size() - 1))
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_end));
						mBaiduMap.addOverlay(optionl);


						//数据库最后一个坐标的位置
						MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(new LatLng(Double.parseDouble(infos.get(infos.size() - 1).getLatitude()), Double.parseDouble(infos.get(infos.size() - 1).getLongitude())));
						mBaiduMap.setMapStatus(u);


						//MapStatus mMapStatus = new MapStatus.Builder()
						//		.zoom(19.0f).build();
						//// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
						//	MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
						//	.newMapStatus(mMapStatus);
						//	mBaiduMap.setMapStatus(mMapStatusUpdate);


						//折线显示
						OverlayOptions ooPolyline = new PolylineOptions().width(10)
								.color(0xAAFF0000).points(resultPoints);
						mBaiduMap.addOverlay(ooPolyline);
						super.onPostExecute(result);
					}
			}
			
		}.execute();
				
	
	}
	
	@Override
	protected void onPause() {
		if(D){
			Log.i("GPS轨迹","onPause()");
		}
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		if(D){
			Log.i("GPS轨迹","onResume()");
		}
		mMapView.onResume();
		showTrack();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if(D){
			Log.i("GPS轨迹","onDestroy()");
		}
		mMapView.onDestroy();
		super.onDestroy();
	}



	private void initLocation() {
		String guijiphone=getIntent().getStringExtra("myphone");
		String  guijidata=getIntent().getStringExtra("data");
		addressdata guiji=new addressdata();
		guiji.setData(guijidata);
		guiji.setMyphone(guijiphone);


	}

}
