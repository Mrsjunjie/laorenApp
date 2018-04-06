package com.atguigu.ditu3.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.atguigu.ditu3.activity.AddContactActivity;
import com.atguigu.ditu3.activity.ContactInfoActivity;
import com.atguigu.ditu3.activity.FindActivity;
import com.atguigu.ditu3.activity.loginl;
import com.atguigu.ditu3.activity.zhuActivity;
import com.google.gson.Gson;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.RequestQueue;
import com.yolanda.nohttp.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Administrator on 2017/4/26.
 */

public class DbUtils {





//    public static void requestHttp(Object object, String url, final Context ctx ) throws MalformedURLException {
//        Gson gson = new Gson();
//        String json = gson.toJson(object);
//        MyUtils.Toast(ctx,"请求url:"+url);
//        RequestParams request = new RequestParams(url);
//        request.addParameter("lrs",json);
//        x.http().post(request, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//
//               parseResult((Activity) ctx,result);
//
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                MyUtils.Toast(ctx,"lllll错误");
//
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//
//            }
//        });


//    }
    public static void requestHttp(Object object, String url, final Context ctx )
    {    RequestQueue queue = NoHttp.newRequestQueue();
       // String path = "http://192.168.0.100:8080/Gson/com.servlet/RegistServlet";
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

                MyUtils.Toast(ctx,"连接成功"+String.valueOf(response.get()));
                parseResult((Activity) ctx,response.get());
            }

            @Override
            public void onFailed(int i, String s, Object o, Exception e, int i1, long l) {
                MyUtils.Toast(ctx,"连接失败");
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

            if (code == 0) {

               MyUtils.Toast(ctx, desc);

            } else if (code == 1) {//注册
                System.out.print("code");
                MyUtils.Toast(ctx,"连接成功12346"+js);
                Intent intent = new Intent(ctx, loginl.class);
                ctx.startActivity(intent);
                ctx.finish();
            } else if (code == 2) {
                //登录成功
                Intent intent = new Intent(ctx, zhuActivity.class);
                ctx.startActivity(intent);
                ctx.finish();
                System.out.print("chenggong");
                MyUtils.Toast(ctx,"登录成功");
                SpUtils.putStringValue(ctx, "userphone", desc);
            } else if (code == 3) {
               // MyUtils.Toast(ctx, desc);
            }else if(code==4){
                Intent intent=new Intent(ctx,FindActivity.class);
                intent.putExtra("desc",desc);
                ctx.startActivity(intent);
            }else if(code==5){
               Intent intent=new Intent(ctx,AddContactActivity.class);
               intent.putExtra("desc",desc);
                ctx.startActivity(intent);
            }else if(code==6){
               MyUtils.Toast(ctx,desc);

            }else if(code==7){
                MyUtils.Toast(ctx,desc);
            }else if(code==8){
               MyUtils.Toast(ctx,desc);
            }else if(code==9){
                MyUtils.Toast(ctx,desc);
               //Intent intent=new Intent(ctx, UserInfoActivity.class);
               // ctx.startActivity(intent);
               // ctx.finish();
            }else if(code==10){
                MyUtils.Toast(ctx,desc);
            }else if(code==11){
              // 已添加过紧急联系人
                MyUtils.Toast(ctx,desc);
            }else if(code==12){
//               添加成功
                MyUtils.Toast(ctx,desc);
            }else if(code==13){
               // 紧急联系人设置已达上限
               MyUtils.Toast(ctx,desc);
            }else if(code==14){
                //得到添加好友
                SpUtils.putStringValue(ctx, "contactbean", desc);
            }else if(code==15){
                //好友资料
                Intent intent=new Intent(ctx, ContactInfoActivity.class);
                intent.putExtra("desc",desc);
                ctx.startActivity(intent);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void addressHttp(Object object, String url, final Context ctx ){
        Gson gson = new Gson();
        String json = gson.toJson(object);
        RequestParams request = new RequestParams(url);
        request.addParameter("lrs",json);

        x.http().post(request, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {



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


}
