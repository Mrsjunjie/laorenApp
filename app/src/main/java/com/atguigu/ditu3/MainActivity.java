package com.atguigu.ditu3;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.ditu3.activity.bean.ContactBean;
import com.atguigu.ditu3.activity.bean.address;
import com.atguigu.ditu3.activity.bean.addressdata;
import com.atguigu.ditu3.utils.Constant;
import com.atguigu.ditu3.utils.MyUtils;
import com.atguigu.ditu3.utils.SpUtils;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.adapter.BNCommonSettingParam;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType.GCJ02;

public class MainActivity extends Activity {

    private boolean kaiqi=false;
    MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private List<LatLng> pts;
    private LocationClient locationClient;
    private BitmapDescriptor geo;
    private boolean isFirst = true;
    private Context context;
    private chongli chongli;
    private Myorientation myorientation;
    private float mX;
    private Button moni, zhenshi;
    private ImageView locationn;
    private double longitude, latitude;
    private LatLng destion, location, latlng,ying=null;
    public static List<Activity> activityList = new LinkedList<Activity>();
    private long time;
    private List<LatLng> dushu;
    private static final String APP_FOLDER_NAME = "BNSDKSimpleDemo";
    private String mSDCardPath = null;

    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    public static final String SHOW_CUSTOM_ITEM = "showCustomItem";
    public static final String RESET_END_NODE = "resetEndNode";
    public static final String VOID_MODE = "voidMode";
    private final static String authBaseArr[] =
            {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
    private final static String authComArr[] = {Manifest.permission.READ_PHONE_STATE};
    private final static int authBaseRequestCode = 1;
    private final static int authComRequestCode = 2;
    private boolean hasInitSuccess = false;
    private boolean hasRequestComAuth = false;

    private static final int TIME_INTERVAL = 80;
    private static final double DISTANCE = 0.0001;
    private LocationManager lm;
    private boolean D=true;
    private  List<GpsInfo> infosl;
    private List<ContactBean> list;
    private TextView shuju;
    public BDLocation bdlocation=null;
    private String chengshi;
    private List<address>    personguiji = new ArrayList<>();
    private addressdata   persondata;
    private GPSinfoDao mGpSinfoDao;
    String action="com.pocketdigi";
    private boolean dingwei=true;
    private double bianjin,chushu,wei,jin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        //必须先注册广播接收器,否则接收不到发送结果
        sendReceiver receiver=new sendReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction(action);
        this.registerReceiver(receiver,filter);

        setContentView(R.layout.activity_main);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        initt();
        chshihua();
        lacate();
        kaiqil();
       if (initDirs()) {
        initNavi();
       }


    }
  // public MainActivity(boolean islFirst){
    //   this.kaiqi=islFirst;
   // }

    private void kaiqil() {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 判断GPS是否正常启动
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "请开启GPS导航...", Toast.LENGTH_SHORT).show();
            // 返回开启GPS导航设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 0);

            return;
        }

        // 为获取地理位置信息时设置查询条件
        String bestProvider = lm.getBestProvider(getCriteria(), true);
        // 获取位置信息
        // 如果不设置查询要求，getLastKnownLocation方法传人的参数为LocationManager.GPS_PROVIDER
        Location location = lm.getLastKnownLocation(bestProvider);
        updateView(location);
        // 监听状态
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.addGpsStatusListener(listener);
        // 绑定监听，有4个参数
        // 参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种
        // 参数2，位置信息更新周期，单位毫秒
        // 参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
        // 参数4，监听
        // 备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新

        // 1秒更新一次，或最小位移变化超过1米更新一次；
        // 注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        // drawMyRoute();
    }


    protected void drawMyRoute() {
        if(D){
            Log.i("GPS轨迹","showTrack()");
        }
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                dushu=new ArrayList<LatLng>();
                //获取数据库的数据
                GPSinfoDao dao=new GPSinfoDao(getApplicationContext());
                infosl = dao.findAll();
                for(GpsInfo info:infosl){
                    double myLatitude=Double.parseDouble(info.getLatitude()) ;
                    double 	myLongitude=Double.parseDouble(info.getLongitude());
                    LatLng point=new LatLng(myLatitude, myLongitude);
                    dushu.add(point);
                    point=null;

                }
                return null;
            }
            @Override
            protected void onPostExecute(Void result) {
                //数据库最后一个坐标的位置
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng
                        (new LatLng(Double.parseDouble(infosl.get(infosl.size()-1).getLatitude()),
                                Double.parseDouble(infosl.get(infosl.size()-1).getLongitude())));
                mBaiduMap.setMapStatus(u);
                //折线显示
                OverlayOptions ooPolyline = new PolylineOptions().width(10)
                        .color(0xAAFF0000).points(dushu);
                mBaiduMap.addOverlay(ooPolyline);
                super.onPostExecute(result);
            }

        }.execute();
    }

    //状态监听
    GpsStatus.Listener listener = new GpsStatus.Listener() {

        public void onGpsStatusChanged(int event) {
            switch (event) {
                // 第一次定位
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    Log.i(TAG, "第一次定位");
                    break;
                // 卫星状态改变
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    Log.i(TAG, "卫星状态改变");
                    // 获取当前状态
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    GpsStatus gpsStatus = lm.getGpsStatus(null);
                    // 获取卫星颗数的默认最大值
                    int maxSatellites = gpsStatus.getMaxSatellites();
                    // 创建一个迭代器保存所有卫星
                    Iterator<GpsSatellite> iters = gpsStatus.getSatellites()
                            .iterator();
                    int count = 0;
                    while (iters.hasNext() && count <= maxSatellites) {
                        GpsSatellite s = iters.next();
                        count++;
                    }
                    System.out.println("搜索到：" + count + "颗卫星");
                    break;
                // 定位启动
                case GpsStatus.GPS_EVENT_STARTED:
                    Log.i(TAG, "定位启动");
                    break;
                // 定位结束
                case GpsStatus.GPS_EVENT_STOPPED:
                    Log.i(TAG, "定位结束");
                    break;

            };
        }

        ;
    };

    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //设置是否要求速度
        criteria.setSpeedRequired(true);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        //设置是否需要方位信息
        criteria.setBearingRequired(true);
        //设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;

    }
    private LocationListener locationListener = new LocationListener() {

        /**
         * 位置信息变化时触发
         */
        public void onLocationChanged(Location location) {
            updateView(location);

            System.out.println( "时间：" + location.getTime());
            System.out.println( "经度：" + location.getLongitude());
            Log.i(TAG, "纬度：" + location.getLatitude());
            Log.i(TAG, "海拔：" + location.getAltitude());
        }

        /**
         * GPS状态变化时触发
         */
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                //GPS状态为可见时
                case LocationProvider.AVAILABLE:
                    Log.i(TAG, "当前GPS状态为可见状态");
                    break;
                //GPS状态为服务区外时
                case LocationProvider.OUT_OF_SERVICE:
                    Log.i(TAG, "当前GPS状态为服务区外状态");
                    break;
                //GPS状态为暂停服务时
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.i(TAG, "当前GPS状态为暂停服务状态");
                    break;
            }
        }

        /**
         * GPS开启时触发
         */
        public void onProviderEnabled(String provider) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission
                    .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = lm.getLastKnownLocation(provider);
            updateView(location);
        }

        /**
         * GPS禁用时触发
         */
        public void onProviderDisabled(String provider) {
            updateView(null);
        }
    };

    private void updateView(Location location){
         int count=0;

    }







    private void initt() {
        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                destion = latLng;
                addOverLay(latLng);
            }
        });
        mBaiduMap.setOnMapDoubleClickListener(new BaiduMap.OnMapDoubleClickListener() {
            @Override
            public void onMapDoubleClick(LatLng latLng) {
                ying=latLng;
                mBaiduMap.clear();
                OverlayOptions option = new MarkerOptions().position(ying)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding));
                mBaiduMap.addOverlay(option);
            }
        });

        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.0f);
        mBaiduMap.setMapStatus(msu);

        this.context = this;
        locationClient = new LocationClient(getApplicationContext());

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
        option.setScanSpan(1000);// 设置发起定位请求的间隔时间为5000ms
        option.setOpenGps(true);
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        // option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        locationClient.setLocOption(option);

        chongli=new chongli(context);//重力加速度点测试
        myorientation = new Myorientation(context);
        myorientation.setOnOrientationListener(new Myorientation.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mX = x;
            }
        });


    }

    private void addOverLay(LatLng desinfo) {

        mBaiduMap.clear();
        OverlayOptions option = new MarkerOptions().position(desinfo)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_end));
        mBaiduMap.addOverlay(option);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    private void lacate() {
        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(final BDLocation bdLocation) {
                String addrStr = bdLocation.getAddrStr();
                SpUtils.putStringValue(MainActivity.this,"addrstr",addrStr);
                if (bdLocation != null) {
                    MyLocationData data = new MyLocationData.Builder()
                            .direction(mX)//方向的值
                            .accuracy(bdLocation.getRadius())
                            .latitude(bdLocation.getLatitude())
                            .longitude(bdLocation.getLongitude())
                            .build();

                    String userphone = SpUtils.getStringValue(MainActivity.this, "userphone", "");

                    mGpSinfoDao=new GPSinfoDao(getApplicationContext());
                    double height=bdLocation.getAltitude();
                    double longitude=bdLocation.getLongitude();
                    double latitude=bdLocation.getLatitude();
                    GpsInfo info=new GpsInfo();
                    info.setLongitude(longitude+"");
                    info.setLatitude(latitude+"");
                    mGpSinfoDao.addGpsInfo(info);




                    if((System.currentTimeMillis()-time)>1000){
                        time=System.currentTimeMillis();
                        //kk.addGpsInfo(gpsInfol);
                       // DbUtils.addressHttp(ab, Constant.ADD_ADDRESS,MapActivity.this);
                    }

                    mBaiduMap.setMyLocationData(data);
                    latitude = bdLocation.getLatitude();
                    longitude = bdLocation.getLongitude();


                  if (ying!=null) {
                      if (latitude > (ying.latitude + 1.00) || latitude < (ying.latitude - 1.00) || longitude > (ying.longitude + 1.00) || longitude < (ying.longitude - 1.00)) {

                          if (list != null) {
                              for (ContactBean cb : list) {

                                  SmsManager smsManager = SmsManager.getDefault();
                                  //发送信息求救
                                  smsManager.sendTextMessage(cb.phone, null, "我在" + addrStr + "附近摔倒了，请求救援！", null, null);

                              }

                          }
                      }
                  }
                     geo = BitmapDescriptorFactory.fromResource(R.drawable.icon_point);
                      MyLocationConfiguration configuration = new MyLocationConfiguration(
                              MyLocationConfiguration.LocationMode.NORMAL, true, geo);
                      mBaiduMap.setMyLocationConfigeration(configuration);// 设置定位模式

                    dingzhibianhua(bdLocation);
                    if (isFirst) {

                        latlng = new LatLng(latitude,longitude);
                        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latlng);
                        mBaiduMap.animateMapStatus(msu);
                        System.out.print( bdLocation.getAddrStr());
                        isFirst = false;
                        Toast.makeText(context, bdLocation.getAddrStr(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });
    }
    private void dingzhibianhua(BDLocation bdLocation){
        LatLng firstlocation = null;
        String bia=getIntent().getStringExtra("banin");
        if(bia!=null) {
            if (dingwei) {
                firstlocation = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                dingwei = false;
                bianjin = Double.parseDouble(bia);
                chushu =  (firstlocation.latitude / 90.0f);
                wei = bianjin / 111.0f;//纬度差值
                jin = bianjin / (111.0f * chushu);//经度差值
            }


            if (Math.abs(bdLocation.getLatitude() - firstlocation.latitude) > wei || Math.abs(bdlocation.getLongitude() - firstlocation.longitude) > bianjin) {
                String userphone = SpUtils.getStringValue(MainActivity.this, "userphone", "");
                ContactBean contactbean = new ContactBean();
                contactbean.myPhone = userphone;
                getlist_3(contactbean, Constant.GET_SETTING_CONTACT);
            }
        }


    }
    private void jieshou(List<ContactBean> list){
        SmsManager smsMgr = SmsManager.getDefault();
        Intent intent = new Intent(action);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);
        //  smsMgr.sendTextMessage("10086", null, "1561", pi, null);
        if (list != null) {
            for (ContactBean cb : list) {
                String addstr=SpUtils.getStringValue(MainActivity.this,"addrstr","");
                //发送信息求救
                smsMgr.sendTextMessage(cb.phone, null, "我在" + addstr + "附近摔倒了，请求救援！", pi, null);

            }

        }


    }
    private class sendReceiver extends BroadcastReceiver {
        //写个接收器
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            int resultCode = getResultCode();
            if(resultCode==Activity.RESULT_OK){
                System.out.println("发送成功");
            }else{
                System.out.println("发送失败");
            }
        }
    }
    private  void getlist_3(Object object, String url) {
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

                MyUtils.Toast(getApplication(), "轨迹jingjili" + response.get());
                parseUserInfok(response.get());

            }

            @Override
            public void onFailed(int i, String s, Object o, Exception e, int i1, long l) {
                MyUtils.Toast(getApplication(), "连接失败list3");
            }

            @Override
            public void onFinish(int i) {

            }
        });
    }
    private  void parseUserInfok( String result) {


        try {
            //  JSONObject jsl = new JSONObject(result);
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                ContactBean contactBean = new ContactBean();
                JSONObject object = (JSONObject) jsonArray.getJSONObject(i);
                contactBean.myPhone = object.optString("myPhone");
                contactBean.name = object.optString("name");
                contactBean.phone = object.optString("phone");
                list.add(contactBean);
            }
            jieshou(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onStart() {
        super.onStart();
        mBaiduMap.setMyLocationEnabled(true);// 打开定位图层
        if (!locationClient.isStarted()) {
            locationClient.start();
            //开启方向传感器
            myorientation.start();
            //重力开启
            chongli.start();

           // startService(new Intent(MainActivity.this,GpsInfoCollectionService.class));
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        mBaiduMap.setMyLocationEnabled(false);// 关闭定位图层
        locationClient.stop();
        //关闭方向传感器
        myorientation.stop();
        //重力关闭
        chongli.stop();
    }

    private void chshihua() {

        shuju= (TextView) findViewById(R.id.shuju);


        final GPSinfoDao aa=new GPSinfoDao(MainActivity.this);
        moni= (Button) findViewById(R.id.moni);
        zhenshi= (Button) findViewById(R.id.zhenshi);
        locationn= (ImageView) findViewById(R.id.location);
        moni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //发送数据给服务器
               GPSinfoDao dao=new GPSinfoDao(getApplicationContext());
              // fasong.requestHttp(dao.findAll(),persondata,Constant.ADD_ADDRESS,MainActivity.this);
                finish();

                //返回时发送轨迹及给服务器
                if(dao.delete()) {
                    Toast.makeText(getApplication(),"数据删除",Toast.LENGTH_LONG).show();
                }
                personguiji=null;
              //  DbUtils.addressHttp(ab, Constant.ADD_ADDRESS,MainActivity.this);

            }
        });
        locationn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapStatusUpdate msu=MapStatusUpdateFactory.newLatLng(latlng);
                mBaiduMap.animateMapStatus(msu);
                isFirst=false;

            }
        });
        zhenshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(destion==null){
                    Toast.makeText(MainActivity.this,"请设置目的地",Toast.LENGTH_LONG).show();
                    return;
                }
                lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                // 判断GPS是否正常启动
                if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Toast.makeText(MainActivity.this, "请开启GPS导航...", Toast.LENGTH_SHORT).show();
                    // 返回开启GPS导航设置界面
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 0);

                    return;
                }
                if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    routeplanToNavi(true);
                }
            }
        });
    }


















    //导航
    public void  showTrack(View v){
        String userph = SpUtils.getStringValue(MainActivity.this, "userphone", "");
        String a=MyUtils.getCurrentDateTime();
        GPSinfoDao gpSinfoDao = new GPSinfoDao(MainActivity.this);
        if(gpSinfoDao.findAll().size()==0){
            Toast.makeText(MainActivity.this,"没有GPS数据",Toast.LENGTH_LONG).show();
            return;
        }
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 判断GPS是否正常启动
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(MainActivity.this, "请开启GPS导航...", Toast.LENGTH_SHORT).show();
            // 返回开启GPS导航设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 0);

            return;
        }
       Intent intentl=new Intent(MainActivity.this,MyTrackActivity.class);
        intentl.putExtra("userph",userph);
        intentl.putExtra("a",a);
        startActivity(intentl);

    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }
    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
    String authinfo = null;
    private boolean hasBasePhoneAuth() {
        // TODO Auto-generated method stub

        PackageManager pm = this.getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    private boolean hasCompletePhoneAuth() {
        // TODO Auto-generated method stub

        PackageManager pm = this.getPackageManager();
        for (String auth : authComArr) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    private void initNavi() {
        // 申请权限
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            if (!hasBasePhoneAuth()) {

                this.requestPermissions(authBaseArr, authBaseRequestCode);
                return;
            }
        }
        BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
                    authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败, " + msg;
                }
                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText( MainActivity.this, authinfo, Toast.LENGTH_LONG).show();
                    }
                });
            }

            public void initSuccess() {
                Toast.makeText( MainActivity.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                hasInitSuccess = true;
                initSetting();
            }

            public void initStart() {
                Toast.makeText( MainActivity.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
            }

            public void initFailed() {
                Toast.makeText( MainActivity.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
            }
        }, null);
    }
    //private CoordinateType mCoordinateType = null;
    private void routeplanToNavi(boolean shif) {

        if (!hasInitSuccess) {
            Toast.makeText(MainActivity.this, "还未初始化!", Toast.LENGTH_SHORT).show();
        }
        // 权限申请
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // 保证导航功能完备
            if (!hasCompletePhoneAuth()) {
                if (!hasRequestComAuth) {
                    hasRequestComAuth = true;
                    this.requestPermissions(authComArr, authComRequestCode);
                    return;
                } else {
                    Toast.makeText(MainActivity.this, "没有完备的权限!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;


        sNode = new BNRoutePlanNode(latlng.longitude, latlng.latitude, "我的地点", null, GCJ02);
        eNode = new BNRoutePlanNode(destion.longitude, destion.latitude, "我的目的地", null,GCJ02);

        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);

            // 开发者可以使用旧的算路接口，也可以使用新的算路接口,可以接收诱导信息等
            // BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode));
            BaiduNaviManager.getInstance().launchNavigator(this, list, 1, shif, new DemoRoutePlanListener(sNode)
            );
        }

    }
    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            /*
             * 设置途径点以及resetEndNode会回调该接口
             */
            Intent intent = new Intent(MainActivity.this, BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            Toast.makeText(MainActivity.this, "算路失败", Toast.LENGTH_SHORT).show();
        }
    }
    private void initSetting() {
        // BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
        BNaviSettingManager
                .setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        // BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
        BNaviSettingManager.setIsAutoQuitWhenArrived(true);
        Bundle bundle = new Bundle();
        // 必须设置APPID，否则会静音
        bundle.putString(BNCommonSettingParam.TTS_APP_ID, "9996592");
        BNaviSettingManager.setNaviSdkParam(bundle);
    }


}
