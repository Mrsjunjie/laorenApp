package com.atguigu.ditu3.utils;

import android.app.Activity;
import android.content.Context;

import com.atguigu.ditu3.GPSinfoDao;
import com.atguigu.ditu3.GpsInfo;
import com.atguigu.ditu3.activity.bean.address;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.RequestQueue;
import com.yolanda.nohttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zx on 2017/9/28.
 */

public class fasong {

    public static void requestHttp(Object object,Object objec, String url, final Context ctx) {
        List<GpsInfo> infos;
        List<LatLng> resultPoints;
        resultPoints= (List<LatLng>) object;
//        infos= (List<GpsInfo>) object;
//        for(GpsInfo info:infos){
//            double   myLatitude=Double.parseDouble(info.getLatitude()) ;
//            double 	myLongitude=Double.parseDouble(info.getLongitude());
//            address point=new address(myLatitude, myLongitude);
//            resultPoints=new ArrayList<address>();
//            resultPoints.add(point);
//        }
        

        RequestQueue queue = NoHttp.newRequestQueue();
        // String path = "http://192.168.0.100:8080/Gson/com.servlet/RegistServlet";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        Gson gson = new Gson();
//        String js = JSON.toJSONString(json);
        String json = gson.toJson(resultPoints);//轨迹数据
        String jsonl=gson.toJson(objec);// 个人资料
        String cishu=gson.toJson(resultPoints.size());
        request.add("lrs", json);
        request.add("cishu", cishu);
        request.add("lr", jsonl);
        queue.add(88, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int i) {

            }

            @Override
            public void onSucceed(int i, Response<String> response) {

                MyUtils.Toast(ctx, "连接成功" + String.valueOf(response.get()));
//                parseResult((Activity) ctx, response.get());
            }

            @Override
            public void onFailed(int i, String s, Object o, Exception e, int i1, long l) {
                MyUtils.Toast(ctx, "连接失败");
            }

            @Override
            public void onFinish(int i) {

            }
        });

    }


    private static void parseResult(Activity ctx, String result) {

        try {
            JSONObject js = new JSONObject(result);
            int code = js.getInt("code");
            String desc = js.getString("desc");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
