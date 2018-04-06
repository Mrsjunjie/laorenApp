package com.atguigu.ditu3.activity.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.ditu3.MainActivity;
import com.atguigu.ditu3.R;
import com.atguigu.ditu3.activity.AddContactActivity;

import com.atguigu.ditu3.activity.ContactInfoActivity;
import com.atguigu.ditu3.activity.adapter.haoyouadapter;
import com.atguigu.ditu3.activity.adapter.jingjiadapter;
import com.atguigu.ditu3.activity.bean.ContactBean;

import com.atguigu.ditu3.utils.Constant;
import com.atguigu.ditu3.utils.DbUtils;
import com.atguigu.ditu3.utils.MyUtils;
import com.atguigu.ditu3.utils.SpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.RequestQueue;
import com.yolanda.nohttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class middlemenul extends Fragment {
    private RadioGroup mRg_main;
  //;  private ListView list_1;
    private ListView list_2;
    private ListView list_3;
    private FrameLayout fragment_1;
    private FrameLayout fragment_2;
    private FrameLayout fragment_3;
    private String [] item={"查看好友信息","设置为紧急联系人","删除好友"};
    /**
     * 选中的Fragment的对应的位置
     */
    private List<ContactBean> list=null;
    private CheckBox checkbox_1;
    private CheckBox checkbox_2;
    private EditText gongli;
    private Button  chakan;
    private Context mContext;
    private TextView tinajia;
    private String usephone;
    private haoyouadapter haoyouadapter;
   // private MyAdapter myAdapter;
    private jingjiadapter adapterl;
    private boolean pengyou;
    private boolean jingji;
    private List<ContactBean> friend=null;
    private List<ContactBean> qiuming=null;
    /**
     * 上次切换的Fragment
     */
   // private Fragment mContent;
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View v=inflater.inflate(R.layout.center,container,false);
        mRg_main = (RadioGroup)v.findViewById(R.id.rg_main);
        inita(v);
        mRg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_common_frame://地图

                        fragment_2.setVisibility(View.GONE);
                        fragment_3.setVisibility(View.GONE);
                        fragment_1.setVisibility(View.VISIBLE);
                        inite1(v);
                        break;
                    case R.id.rb_thirdparty://朋友

                        fragment_1.setVisibility(View.GONE);
                        fragment_3.setVisibility(View.GONE);
                        fragment_2.setVisibility(View.VISIBLE);
                        pengyou=true;
                        inite2(v);

                        break;
                    case R.id.rb_custom://紧及

                        fragment_1.setVisibility(View.GONE);
                        fragment_2.setVisibility(View.GONE);
                        fragment_3.setVisibility(View.VISIBLE);
                        jingji=true;
                        inite3(v);
                        break;
                    default:

                        break;
                }
            }
        });

        return v;
    }

    private void inite3(View v) {
//        tv_desc2= (TextView) v.findViewById(R.id.tv_desc2);
        list_3= (ListView) v.findViewById(R.id.list_3);
        ContactBean contactbean=new ContactBean();
        String userphonl= SpUtils.getStringValue(getActivity(), "userphone", "");
        ContactBean contact = new ContactBean();
        contact.myPhone = userphonl;
        getlist_2(contact,Constant.GET_SETTING_CONTACT);


            list_3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("删除该联系人");
                    final ContactBean contactBean = list.get(position);
                    builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DbUtils.requestHttp(contactBean, Constant.DELETE_SETTING_CONTACT, getActivity());
                            qiuming.remove(position);
//                        if (list.size() == 0) {
//                            tv_desc2.setVisibility(View.VISIBLE);
//                        }
                            list_3.setAdapter(adapterl);
                            adapterl.notifyDataSetChanged();
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();


                        }
                    });
                    builder.show();
                }
            });

    }

    private void inite2(View v) {
        tinajia= (TextView) v.findViewById(R.id.tinajia);
        list_2= (ListView) v.findViewById(R.id.list_2);
        tinajia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加好友
                Intent intent=new Intent(mContext, AddContactActivity.class);
                intent.putExtra("usephone", usephone);
                startActivity(intent);
            }
        });
       // String  mayphone= SpUtils.getStringValue(mContext,"userphone","");
        ContactBean contactbean=new ContactBean();
        String userphon = SpUtils.getStringValue(getActivity(), "userphone", "");
        contactbean.myPhone=userphon;
        getlist_2(contactbean,Constant.ADD_CONTACT);

            list_2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {



                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    builder.setSingleChoiceItems(item, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ContactBean contactBean = friend.get(position);
                            if(i==0){

                                Intent intent=new Intent(getActivity(),ContactInfoActivity.class);
                                intent.putExtra("phone",contactBean.phone);
                                startActivity(intent);
                                dialogInterface.dismiss();
                            }else if(i==1){
                                DbUtils.requestHttp(contactBean,Constant.SETTING_CONTACT,getActivity());
                                dialogInterface.dismiss();

                            }else if(i==2){
                                DbUtils.requestHttp(contactBean,Constant.DELETE_CONTACT,getActivity());
                                friend.remove(position);
//                            if(list.size()==0){
//                                tv_desc1.setVisibility(View.VISIBLE);
//                            }
//                            lv.setAdapter(myAdapter);
                                haoyouadapter.notifyDataSetChanged();
                                dialogInterface.dismiss();




                            }

                        }
                    });




                    builder.show();

                }
            });


    }

    private void inite1(View v){
        checkbox_1= (CheckBox) v.findViewById(R.id.checkbox_1);
        checkbox_2= (CheckBox) v.findViewById(R.id.checkbox_2);
        gongli= (EditText) v.findViewById(R.id.gongli);
        chakan= (Button) v.findViewById(R.id.chakan);
        checkbox_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked==true){

                    //轨迹开启
                }
            }
        });

        checkbox_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //开启摔倒检测
                if(isChecked==true){
                    MyUtils.Toast(mContext,"服务开启");
                }else {

                }

            }
        });
        chakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //转到地图定位
                Intent intent=new Intent(mContext,MainActivity.class) ;
               String banjin=gongli.getText().toString().trim();
               intent.putExtra("banin",banjin);

                startActivity(intent);

            }
        });


    }

    private void inita(View v) {
        fragment_1=(FrameLayout) v.findViewById(R.id.fragment_1);
        fragment_2=(FrameLayout) v.findViewById(R.id.fragment_2);
        fragment_3=(FrameLayout) v.findViewById(R.id.fragment_3);
    }


    public void middlemenul(Context mContent, String usephone) {
        this.mContext=mContent;
        this.usephone=usephone;
    }

    private  void getlist_2(Object object, String url){
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

                MyUtils.Toast(mContext,"连接成功list2"+response.get());
                parseUserInfok(response.get());

            }

            @Override
            public void onFailed(int i, String s, Object o, Exception e, int i1, long l) {
                MyUtils.Toast(mContext,"连接失败list2");
            }

            @Override
            public void onFinish(int i) {

            }
        });

    }

    private  void parseUserInfok( String result) {

        list =new ArrayList<ContactBean>();
        friend =new ArrayList<ContactBean>();
        qiuming =new ArrayList<ContactBean>();
        try {
          //  JSONObject jsl = new JSONObject(result);
            JSONArray jsonArray=new JSONArray(result);
            for(int i=0;i<jsonArray.length();i++){
                ContactBean contactBean=new ContactBean();
                 JSONObject object= (JSONObject) jsonArray.getJSONObject(i);
                contactBean.myPhone= object.optString("myPhone");
                contactBean.name=object.optString("name");
                contactBean.phone=object.optString("phone");
                list.add(contactBean);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(pengyou){
            friend=list;
            haoyouadapter = new haoyouadapter(mContext, friend);
            list_2.setAdapter(haoyouadapter);
        }
        if(jingji){
            qiuming=list;
            adapterl = new jingjiadapter(mContext, qiuming);
            list_3.setAdapter(adapterl);
        }

    }




}
