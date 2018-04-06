package com.atguigu.ditu3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class LocationInfosDBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME="gpsinfo.db";
	private static final int VERSION=2;
	public LocationInfosDBHelper(Context context
			) {
		super(context, DATABASE_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE gpsinfo(_id integer primary key autoincrement,longitude TEXT,latitude TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 db.execSQL("drop table gpsinfo   " );
         onCreate(db);

	}


}
