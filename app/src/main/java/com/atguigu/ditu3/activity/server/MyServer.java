package com.atguigu.ditu3.activity.server;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.atguigu.ditu3.activity.bean.ContactBean;
import com.atguigu.ditu3.activity.bean.SeneorValue;
import com.atguigu.ditu3.utils.Constant;
import com.atguigu.ditu3.utils.SpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/3/13 0013.
 */

public class MyServer extends Service {

    private SensorManager mManager;
    private Sensor mSensor;
    private MyAccelerometer myAccelerometer;
    private static final String TAG = "MyServer";
    private ArrayList<SeneorValue> data;
    private List<ContactBean> list;
    private String userphone;
    private long time;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        data = new ArrayList<SeneorValue>();

        mManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
       // userphone = SpUtils.getStringValue(this, "userphone", "");
        //loadHttp();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        myAccelerometer = new MyAccelerometer();
        mManager.registerListener(myAccelerometer, mSensor, SensorManager.SENSOR_DELAY_NORMAL);


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void loadHttp() {
        ContactBean contactBean = new ContactBean();
        contactBean.myPhone = userphone;
        getContactInfo(contactBean, Constant.GET_SETTING_CONTACT);
    }


    /**
     * 实现感应器监听方法
     */
    private class MyAccelerometer implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {


            float x = Math.abs(sensorEvent.values[0]);
            float y = Math.abs(sensorEvent.values[1]);
            float z = Math.abs(sensorEvent.values[2]);
            SeneorValue seneorValue = new SeneorValue();
            seneorValue.setX(x);
            seneorValue.setY(y);
            seneorValue.setZ(z);
            if (data.size() > 0) {
                double x1 = data.get(0).getX();
                double y1 = data.get(0).getY();
                double z1 = data.get(0).getZ();
                int currentResult = (int) (x * x + y * y + z * z);
                int lastResult = (int) (x1 * x1 + y1 * y1 + z1 * z1);
                int result = currentResult - lastResult;
                data.clear();
                data.add(seneorValue);

                if (result > 80) {
                    time = System.currentTimeMillis()+5000;


                    }else{

                    if(System.currentTimeMillis() == time ){
                        String addrstr = SpUtils.getStringValue(MyServer.this, "addrstr", "");
                        Toast.makeText(MyServer.this, "我在" + addrstr + "附近摔倒了，请求救援！",Toast.LENGTH_LONG).show();
                        if (list != null) {
                            for (ContactBean cb : list) {
                               SmsManager smsManager = SmsManager.getDefault();

                                smsManager.sendTextMessage(cb.phone, null, "我在" + addrstr + "附近摔倒了，请求救援！", null, null);

                            }

                        }
                }




                }
            } else {
                data.add(seneorValue);
            }


        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    public void getContactInfo(Object object, String url) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        RequestParams request = new RequestParams(url);
        request.addParameter("lrs", json);


        x.http().post(request, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                parseUserInfo(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }


    private void parseUserInfo(String result) {
        Type listType = new TypeToken<List<ContactBean>>() {
        }.getType();

        Gson gson = new Gson();
        list = gson.fromJson(result, listType);
        System.out.println(list.size());


    }
}
