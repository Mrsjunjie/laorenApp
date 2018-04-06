package com.atguigu.ditu3;




import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class GpsInfoCollectionService extends Service {
	
	private Boolean D=true;
	private GPSinfoDao mGpSinfoDao;
	private LocationManager mLocationManager;
	private Location mLocation;
	public GpsInfoCollectionService() {
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		if(D){
			Log.i("GPS服务数据收集","IBinder()");
		}
		return null;
	}
	@Override
	public void onCreate() {
		if(D){
			Log.i("GPS服务数据收集","onCreate()");
		}
		 mGpSinfoDao=new GPSinfoDao(getApplicationContext());
		 mLocationManager=(LocationManager) getSystemService(LOCATION_SERVICE);
		super.onCreate();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(D){
			Log.i("GPS服务数据收集","onStartCommand()");
		}
		  Criteria criteria = new Criteria();
	        criteria.setAccuracy(Criteria.ACCURACY_FINE);//��ȡ��ȷ��λ��.
	        criteria.setAltitudeRequired(true);
	        criteria.setBearingRequired(true);
	        criteria.setCostAllowed(true);
	        criteria.setPowerRequirement(Criteria.POWER_HIGH);
	        criteria.setSpeedRequired(true);
	        
	        String provider = mLocationManager.getBestProvider(criteria, true);
	     
		
	     		
	        mLocationManager.requestLocationUpdates(provider, 300, 300, new LocationListener() {
				
				@Override
				public void onStatusChanged(String provider, int status, Bundle extras) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onProviderEnabled(String provider) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onProviderDisabled(String provider) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLocationChanged(Location location) {
					//updateLocation(location);
					mLocation=location;
					new AsyncTask<Void, Void, Void>() {

						@Override
						protected Void doInBackground(Void... params) {


							double height=mLocation.getAltitude();
							double longitude=mLocation.getLongitude();
							double latitude=mLocation.getLatitude();
							GpsInfo info=new GpsInfo();
							info.setLongitude(longitude+"");
							info.setLatitude(latitude+"");
							mGpSinfoDao.addGpsInfo(info);
							System.out.print(longitude+"    "+latitude);
							info=null;
							return null;

						}
						
						
					}.execute();
	        
	    
				}
				
	        });
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		if(D){
			Log.i("GPS服务数据收集","onDestroy()");
		}
//		DbUtils.addressHttp(ab, Constant.ADD_ADDRESS,getApplication());
		mGpSinfoDao=null;
		mLocationManager=null;
		mLocation=null;
		super.onDestroy();
	}

}
