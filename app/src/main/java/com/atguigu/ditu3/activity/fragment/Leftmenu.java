package com.atguigu.ditu3.activity.fragment;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.ditu3.R;
import com.atguigu.ditu3.activity.HistoryActivity;
import com.atguigu.ditu3.activity.HistoryMapActivity;
import com.atguigu.ditu3.activity.UserInfoUpdateActivity;
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
 * Created by zx on 2017/8/23.
 */

public class Leftmenu extends Fragment {

    private TextView one;
    private TextView two;
    private TextView three;
    private TextView four;
    private TextView five;
    private String phone;
    private String name;
    private int age;
    private String sex=null;
    private String address;
    private Context mcontext;
    private Button  btn_update, btn_history;
    String action="com.pocketdigi";

    public void leftmenu(Context mcontext, String usephone){

        this.mcontext=mcontext;
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.left,container,false);
        one= (TextView) v.findViewById(R.id.text1);
        two= (TextView) v.findViewById(R.id.text2);
        three= (TextView) v.findViewById(R.id.text3);
        four= (TextView) v.findViewById(R.id.text4);
        five= (TextView) v.findViewById(R.id.text5);
        btn_update= (Button) v.findViewById(R.id.btn_update);
        btn_history= (Button) v.findViewById(R.id.btn_history);
        initData();
        return v;
    }


    private void initData() {
        phone = SpUtils.getStringValue(getActivity(), "userphone", "");
        final UserBean userBean = new UserBean();
        userBean.phone= phone;
        if(!TextUtils.isEmpty(phone)){
          // DbUtils.requesttHttp(userBean,);
            getUserInfot(userBean, Constant.USERINFO);
        }

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mcontext,UserInfoUpdateActivity.class);
//                UserBean userBean = new UserBean();
//                userBean.phone=phone;
//               getUserInfot(userBean, Constant.USERINFO);
                intent.putExtra("username",name);
                intent.putExtra("age",age+"");
                intent.putExtra("sex",sex);
                intent.putExtra("address",address);
                startActivity(intent);
            }
        });
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mcontext,HistoryActivity.class);
                intent.putExtra("phone",phone);
                startActivity(intent);



            }
        });


    }






    public  void getUserInfot(Object object, String url ){
        Gson gson = new Gson();
        String json = gson.toJson(object);

        RequestQueue queue = NoHttp.newRequestQueue();
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        request.add("lrs", json);
        queue.add(88, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int i) {

            }

            @Override
            public void onSucceed(int i, Response<String> response) {

                MyUtils.Toast(mcontext,"连接成功自己资料"+response.get());
                parseUserInfo(response.get());
            }

            @Override
            public void onFailed(int i, String s, Object o, Exception e, int i1, long l) {
                MyUtils.Toast(mcontext,"连接失败");
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
            name = js.getString("username");
            age = js.getInt("age");
            sex = js.getString("sex");
            address = js.getString("address");

            if(code==1){
                two.setText(name);
                five.setText(age+"");
                three.setText(sex);
                one.setText(phone);
                four.setText(address);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(mcontext,""+name+address+sex+age,Toast.LENGTH_LONG).show();
    }
}
