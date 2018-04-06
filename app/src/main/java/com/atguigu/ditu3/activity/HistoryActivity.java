package com.atguigu.ditu3.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.ditu3.R;
import com.atguigu.ditu3.activity.bean.addressdata;
import com.atguigu.ditu3.utils.Constant;
import com.atguigu.ditu3.utils.MyUtils;
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

public class HistoryActivity extends Activity {

    private ListView lv;
    private List<addressdata> list;
    private HistoryActivity ctx;
    private MyAdapter myAdapter;
    private  SharedPreferences spCount;
    private SharedPreferences.Editor editor;
    private String  list1=null;
    private SharedPreferences  spCountl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ctx=this;
        lv = (ListView) findViewById(R.id.lv);
        String phone = getIntent().getStringExtra("phone");
        addressdata ab=new addressdata();
        ab.setMyphone(phone);

        getContactInfo(ab, Constant.GET_ADDRESS);


//            myAdapter = new MyAdapter(ctx,list);
//            lv.setAdapter(myAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    addressdata addressBean = list.get(i);
                    String    time = addressBean.getData();
                    Intent intent = new Intent(ctx, HistoryMapActivity.class);
                    intent.putExtra("data", time);
                    intent.putExtra("myphone", addressBean.getMyphone());
                    startActivity(intent);

                }
            });

        Button back= (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private class MyAdapter extends BaseAdapter{
        private List<addressdata> array;
        private     Context   context;

        public MyAdapter(Context mcontext, List<addressdata> list){
           this.context=mcontext;
           this.array=list;
       }
        @Override
        public int getCount() {
            if(list!=null){
                return list.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View view=null;
            ViewHolder vh = new ViewHolder();
            if(convertView==null){
                view=View.inflate(context,R.layout.item_history,null);
                vh.tv_date= (TextView) view.findViewById(R.id.tv_date);

                view.setTag(vh);
            }else{
                view=convertView;
                vh= (ViewHolder) view.getTag();
            }

            addressdata ab = array.get(position);
            vh.tv_date.setText(ab.getData());

            return view;
        }
    }
    private class ViewHolder{
        TextView tv_date;


    }




    public  void getContactInfo(Object object, String url ) {

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

                MyUtils.Toast(ctx,"连接成功"+response.get());

                parseUser(response.get());
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
    private  void parseUser( String result) {
        list = new ArrayList<addressdata>();

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                addressdata data = new addressdata();
                JSONObject object = (JSONObject) jsonArray.getJSONObject(i);
                data.setMyphone(object.optString("myphone"));
                data.setData(object.optString("data"));
                list.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplication(), "" + list, Toast.LENGTH_LONG).show();

        myAdapter = new MyAdapter(ctx,list);
        lv.setAdapter(myAdapter);
    }

    }
