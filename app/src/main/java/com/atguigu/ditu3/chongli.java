package com.atguigu.ditu3;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.atguigu.ditu3.activity.bean.ContactBean;
import com.atguigu.ditu3.activity.bean.SeneorValue;
import com.atguigu.ditu3.activity.server.MyServer;
import com.atguigu.ditu3.utils.SpUtils;
import java.util.List;

/**
 * Created by zx on 2017/9/15.
 */

public class chongli implements SensorEventListener {
    private SensorManager  mManager;
    private Context context;
    private  Sensor mSensor;
    private  float X;
    private List<SeneorValue> data;
    private long time;

    public chongli(Context context){

        this.context=context;
    }
    public void start(){
        mManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        if(mManager!=null) {
            mSensor = mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        if (mSensor!=null){
            mManager.registerListener(sensorEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }
    public void stop(){
        mManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        double x = Math.abs(event.values[0]);
        double y = Math.abs(event.values[1]);
        double z = Math.abs(event.values[2]);
        System.out.print(x+y+z);
        SeneorValue seneorValue = new SeneorValue();
        seneorValue.setX(x);
        seneorValue.setY(y);
        seneorValue.setZ(z);
        shuju ju=new shuju();

        System.out.print(x+"   "+y+"    "+z);
        if (ju.first) {
            double x1 = ju.x;
            double y1 = ju.y;
            double z1 = ju.z;
            int currentResult = Integer.parseInt(String.valueOf(x*x + y*y + z*z));
            int lastResult = Integer.parseInt(String.valueOf(x1*x1 + y1*y1 + z1*z1));
            int result = currentResult - lastResult;
            System.out.print(result);
            Toast.makeText(context, "我在" + "附近摔倒了，请求救援！",Toast.LENGTH_LONG).show();
            if(result>0){
                ju.x=x;
                ju.y=y;
                ju.z=z;
                ju.first=true;
            }
            if (Math.abs(result)<800) {
                time = System.currentTimeMillis()+5000;


            }else{

                if(System.currentTimeMillis() == time ){
                   // String addrstr = SpUtils.getStringValue(context, "addrstr", "");
                    Toast.makeText(context, "我在" + "附近摔倒了，请求救援！",Toast.LENGTH_LONG).show();


                }
            }
        } else {
            ju.x=x;
            ju.y=y;
            ju.z=z;
            ju.first=true;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    private class shuju{
        double x=0;
        double y=0;
         double z=0;
      private   boolean first=false;
    }
    //添加重力感应监听，并实现其方法
    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            shuju ju=new shuju();
            if (event.sensor == null) {
                return;
            }

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
             double   x = event.values[SensorManager.DATA_X];
              double  y = event.values[SensorManager.DATA_Y];

                double  z = event.values[SensorManager.DATA_Z];
              //  System.out.print("x="+(int)x+"y="+(int)y+"z="+(int)z);

                if (ju.first==true) {
                    double x1 = ju.x;
                    double y1 = ju.y;
                    double z1 = ju.z;
                    System.out.print("x1="+(int)x1+"         "+"y1="+(int)y1+"         "+"z1="+(int)z1);
                    int currentResult = Integer.parseInt(String.valueOf(x*x + y*y + z*z));
                    int lastResult = Integer.parseInt(String.valueOf(x1*x1 + y1*y1 + z1*z1));
                    int result = currentResult - lastResult;
                    System.out.print(result);
                    Toast.makeText(context, x1 +"    " +y1+"    "+z1+"   ",Toast.LENGTH_LONG).show();
                    if(result>0){
                        ju.x=x;
                        ju.y=y;
                        ju.z=z;
                        ju.first=true;
                    }
                    if (Math.abs(result)>80) {
                        time = System.currentTimeMillis()+1000;


                    }else{

                        if(System.currentTimeMillis() == time ){
                           // String addrstr = SpUtils.getStringValue(context, "addrstr", "");
                            Toast.makeText(context, "我在"  + "附近摔倒了，请求救援！",Toast.LENGTH_LONG).show();


                        }
                    }
                } else {
                    ju.x=x;
                    ju.y=y;
                    ju.z=z;
                    ju.first=true;
                }
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    //注册Listener，SENSOR_DELAY_GAME为检测的精确度，
  //  mSensorManager.registerListener(sensorEventListener, mSensor,SensorManager.SENSOR_DELAY_GAME);

  }

