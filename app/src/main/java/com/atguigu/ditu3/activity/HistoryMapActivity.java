package com.atguigu.ditu3.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.atguigu.ditu3.R;
import com.atguigu.ditu3.activity.bean.address;
import com.atguigu.ditu3.activity.bean.addressdata;
import com.atguigu.ditu3.utils.Constant;
import com.atguigu.ditu3.utils.MyUtils;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.RequestQueue;
import com.yolanda.nohttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/5/5.
 */

public class HistoryMapActivity extends Activity {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private List<address>  listl=null;
    private 	List<LatLng> resultPoints=null;
    private double longitude;
    private double latitude;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_history_map);
        Button back= (Button) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        initView();
        //initLocation();
    }

    private void initView() {
        mMapView= (MapView) findViewById(R.id.id_bmapView);

        mBaiduMap=mMapView.getMap();
        //根据给定增量缩放地图级别

        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.0f);
        mBaiduMap.setMapStatus(msu);


        String myhone=getIntent().getStringExtra("myphone");
        String data=getIntent().getStringExtra("data");
        addressdata addr=new addressdata();
        addr.setMyphone(myhone);
        addr.setData(data);
       // showTrack(addr,Constant.GET_GUIJIADDRESS);
        getContactInf(addr,Constant.GET_GUIJIADDRESS);


    }
    private void initLocation(List<LatLng> resultPoints) {
        List<LatLng>  xiao=resultPoints;

//        LatLng latLng = new LatLng(latitude, longitude);
//
//
//        MapStatus mMapStatus = new MapStatus.Builder().target(latLng)
//                .zoom(19.0f).build();
//// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
//        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
//                .newMapStatus(mMapStatus);
//// 改变地图状态
//// 开启定位图层
//        mBaiduMap.setMapStatus(mMapStatusUpdate);
//
////        添加maker标记
//
//// 定义Maker坐标点
//// 构建Marker图标
//        BitmapDescriptor bitmap = BitmapDescriptorFactory
//                .fromResource(R.drawable.icon_geo);
//// 构建MarkerOption，用于在地图上添加Marker
//        OverlayOptions option = new MarkerOptions().position(latLng).icon(
//                bitmap);
//// 在地图上添加Marker，并显示
//        mBaiduMap.clear();
//        mBaiduMap.addOverlay(option);
        mBaiduMap.clear();
        int k = this.resultPoints.size();
        if (k > 1) {
            LatLng qidian = new LatLng(this.resultPoints.get(0).latitude, this.resultPoints.get(0).longitude);
            OverlayOptions option = new MarkerOptions().position(qidian)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_point));
            mBaiduMap.addOverlay(option);


            LatLng zuihou = new LatLng(this.resultPoints.get(k - 1).latitude, this.resultPoints.get(k - 1).longitude);
            OverlayOptions optionl = new MarkerOptions().position(zuihou)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_end));
            mBaiduMap.addOverlay(optionl);


            //数据库最后一个坐标的位置
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(new LatLng(this.resultPoints.get(k - 1).latitude, this.resultPoints.get(k - 1).longitude));
            mBaiduMap.setMapStatus(u);


            MapStatus mMapStatus = new MapStatus.Builder()
                    .zoom(19.0f).build();
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                    .newMapStatus(mMapStatus);
            mBaiduMap.setMapStatus(mMapStatusUpdate);
            //折线显示
            OverlayOptions ooPolyline = new PolylineOptions().width(15)
                    .color(0xAAFF0000).points(this.resultPoints);
            mBaiduMap.addOverlay(ooPolyline);


        }
    }






    private void showTrack(final Object object, final String url){
        listl=new ArrayList<address>() ;
        resultPoints=new ArrayList<LatLng>();


        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                RequestQueue queue = NoHttp.newRequestQueue();
                Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
                Gson gson = new Gson();
                String json = gson.toJson(object);
                request.add("lrs", json);
                queue.add(88, request, new OnResponseListener<String>() {
                    @Override
                    public void onStart(int i) {
                    }
                    @Override
                    public void onSucceed(int i, Response<String> response) {
                        MyUtils.Toast(HistoryMapActivity.this,"连接成功"+response.get());



                        JSONArray jsonArray ;
                        try {
                            jsonArray = new JSONArray(response.get());
                            for (int j = 0; j < jsonArray.length(); j++) {
                                address aa=new address();
                                JSONObject object = (JSONObject) jsonArray.getJSONObject(j);
                                aa.setLatitude(Double.parseDouble((object.optString("latitude"))));
                                aa.setLongitude(Double.parseDouble(object.optString("longitude")));
                                listl.add(aa);
                                resultPoints.add(new LatLng(aa.getLatitude(),aa.getLongitude()));
                            }
                            System.out.print(resultPoints);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplication(),""+resultPoints,Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onFailed(int i, String s, Object o, Exception e, int i1, long l) {
                        MyUtils.Toast(HistoryMapActivity.this,"连接失败");
                    }
                    @Override
                    public void onFinish(int i) {
                    }
                });
                return null;
            }
            @Override
            protected void onPostExecute(Void result) {

                mBaiduMap.clear();
                int k=resultPoints.size();
                if(k>1) {
                LatLng qidian=new LatLng(resultPoints.get(0).latitude,resultPoints.get(0).longitude);
                OverlayOptions option = new MarkerOptions().position(qidian)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_point));
                mBaiduMap.addOverlay(option);




                    LatLng zuihou = new LatLng(resultPoints.get(k-1).latitude, resultPoints.get(k-1).longitude);
                    OverlayOptions optionl = new MarkerOptions().position(zuihou)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_end));
                    mBaiduMap.addOverlay(optionl);


                    //数据库最后一个坐标的位置
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(new LatLng(resultPoints.get(k-1).latitude, resultPoints.get(k-1).longitude));
                    mBaiduMap.setMapStatus(u);


                    MapStatus mMapStatus = new MapStatus.Builder()
                    		.zoom(19.0f).build();
                     //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                    	MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                    	.newMapStatus(mMapStatus);
                    	mBaiduMap.setMapStatus(mMapStatusUpdate);
                    //折线显示
                    OverlayOptions ooPolyline = new PolylineOptions().width(15)
                            .color(0xAAFF0000).points(resultPoints);
                    mBaiduMap.addOverlay(ooPolyline);
                }




                //MapStatus mMapStatus = new MapStatus.Builder()
                //		.zoom(19.0f).build();
                //// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                //	MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                //	.newMapStatus(mMapStatus);
                //	mBaiduMap.setMapStatus(mMapStatusUpdate);






                super.onPostExecute(result);
            }

        }.execute();


    }

    @Override
    protected void onPause() {

        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {

        mMapView.onResume();

        String myhone=getIntent().getStringExtra("myphone");
        String data=getIntent().getStringExtra("data");
        addressdata addr=new addressdata();
        addr.setMyphone(myhone);
        addr.setData(data);
        showTrack(addr,Constant.GET_GUIJIADDRESS);
        super.onResume();
    }

    @Override
    protected void onDestroy() {

        mMapView.onDestroy();
        super.onDestroy();
    }
    public  void getContactInf(Object object, String url ) {

        RequestQueue queue = NoHttp.newRequestQueue();
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        Gson gson = new Gson();
        String json = gson.toJson(object);
        request.add("lrs", json);
        queue.add(88, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int i) {

            }

            @Override
            public void onSucceed(int i, Response<String> response) {

                MyUtils.Toast(getApplication(),"连接成功"+response.get());

                parseUser(response.get());
            }

            @Override
            public void onFailed(int i, String s, Object o, Exception e, int i1, long l) {
                MyUtils.Toast(getApplication(),"连接失败");
            }

            @Override
            public void onFinish(int i) {

            }
        });


    }
    private  void parseUser( String result) {
        JSONArray jsonArray ;
        try {
            jsonArray = new JSONArray(result);
            for (int j = 0; j < jsonArray.length(); j++) {
                address aa=new address();
                JSONObject object = (JSONObject) jsonArray.getJSONObject(j);
                aa.setLatitude(Double.parseDouble((object.optString("latitude"))));
                aa.setLongitude(Double.parseDouble(object.optString("longitude")));
                listl.add(aa);
                resultPoints.add(new LatLng(aa.getLatitude(),aa.getLongitude()));
            }
            System.out.print(resultPoints);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initLocation(resultPoints);
        Toast.makeText(getApplication(),""+resultPoints,Toast.LENGTH_LONG).show();
    }

}
