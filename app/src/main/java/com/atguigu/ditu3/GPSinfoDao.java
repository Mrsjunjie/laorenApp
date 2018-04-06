package com.atguigu.ditu3;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
public class GPSinfoDao {
	private LocationInfosDBHelper mDBHelper;
	public GPSinfoDao(Context context) {
		
		mDBHelper=new LocationInfosDBHelper(context);
	}

	
	public boolean addGpsInfo(GpsInfo info){
		
		boolean flag=false;
		SQLiteDatabase database=null;
		try {
			database=mDBHelper.getWritableDatabase();
			database.execSQL("insert into gpsinfo(longitude ,latitude) values(?,?)",
					new String[]{info.getLongitude(),info.getLatitude()});
			flag=true;
			database.close();
			
		} catch (Exception e) {

		}
		
		return flag;
	}
	public boolean delete(){
		boolean flag=false;
		SQLiteDatabase database=null;
		try {
		    database=mDBHelper.getWritableDatabase();
			database.delete("gpsinfo",null,null);
		   // database.execSQL("delete * from  gpsinfo");
			flag=true;

		} catch (Exception e) {

		}
		database.close();
		return flag;
	}
	
	public  List<GpsInfo> findAll(){
		SQLiteDatabase database=null;
		List<GpsInfo> infos=new ArrayList<GpsInfo>();
		Cursor cursor=null;
		try {
			database=mDBHelper.getReadableDatabase();
			cursor=database.rawQuery("select longitude,latitude from gpsinfo ",null);
			while(cursor.moveToNext()){
				GpsInfo gpsInfo=new GpsInfo();
				gpsInfo.setLongitude(cursor.getString(0));
				gpsInfo.setLatitude(cursor.getString(1));
				infos.add(gpsInfo);
				gpsInfo=null;
				
			}
			database.close();
		} catch (Exception e) {

		}
		return infos;
	}
	
}
