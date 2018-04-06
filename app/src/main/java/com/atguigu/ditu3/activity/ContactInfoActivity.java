package com.atguigu.ditu3.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.atguigu.ditu3.R;
import com.atguigu.ditu3.activity.bean.UserBean;
import com.atguigu.ditu3.utils.Constant;
import com.atguigu.ditu3.utils.MyUtils;
import com.atguigu.ditu3.utils.SpUtils;
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
 * Created by Administrator on 2017/4/29.
 */


public class ContactInfoActivity extends Activity {
    private TextView tv_username;
    private TextView tv_sex;
    private TextView tv_age;
    private TextView tv_phone;
    private TextView tv_address;
    private String username;
    private int age;
    private String sex;
    private String address;
    private String phone;
    private Button btn_history;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);



        initUI();
        initData();

    }

    private void initData() {

        Button back= (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        phone = getIntent().getStringExtra("phone");
        UserBean userBean = new UserBean();
        userBean.phone=phone;
        getUserInfo(userBean, Constant.USERINFO);
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ContactInfoActivity.this,HistoryActivity.class);
                intent.putExtra("phone",phone);
                startActivity(intent);

            }
        });


    }

    private void initUI() {
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_address = (TextView) findViewById(R.id.tv_address);
        btn_history = (Button) findViewById(R.id.btn_history);
    }
    public  void getUserInfo(Object object, String url ){
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

                MyUtils.Toast(getApplication(),"连接成功好友资料设置"+String.valueOf(response));
                parseUserInfo(response.get());
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



    private  void parseUserInfo( String result) {

        try {
           JSONObject js=new JSONObject(result);
           int code = js.getInt("code");


            username = js.getString("username");
           age = js.getInt("age");
            sex = js.getString("sex");
            address = js.getString("address");

            if(code==1){
                tv_username.setText(username);
                tv_age.setText(age+"");
                tv_sex.setText(sex);
                tv_phone.setText(phone);
                tv_address.setText(address);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
