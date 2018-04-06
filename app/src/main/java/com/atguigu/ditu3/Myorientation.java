package com.atguigu.ditu3;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by zx on 2017/8/10.
 */

public class Myorientation implements SensorEventListener{
    private SensorManager sensorManager;
    private Context context;
    private  Sensor sensor;
    private  float X;
    public Myorientation(Context context){
        this.context=context;
    }
    public  void start(){
      sensorManager= (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
       if( sensorManager!=null) {
           sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
       }

        if(sensor!=null){
            sensorManager.registerListener(this,sensor,SensorManager.
                    SENSOR_DELAY_UI);
        }
    }

    public void stop(){
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
          if(event.sensor.getType()==sensor.TYPE_ORIENTATION) {
              float x = event.values[SensorManager.DATA_X];
              System.out.print(x);
              if (Math.abs(x - X) > 1.0) {

                   if(onOrientationListener!=null){
                       onOrientationListener.onOrientationChanged(x);
                   }
              }
              X=x;
          }





    }

    public OnOrientationListener getOnOrientationListener() {
        return onOrientationListener;
    }

    public void setOnOrientationListener(OnOrientationListener onOrientationListener) {
        this.onOrientationListener = onOrientationListener;
    }

    private  OnOrientationListener onOrientationListener;
    public interface OnOrientationListener{
       void  onOrientationChanged(float x);
   }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
