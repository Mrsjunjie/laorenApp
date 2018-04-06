package com.atguigu.ditu3.activity;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.ditu3.R;
import com.atguigu.ditu3.activity.bean.ContactBean;
import com.atguigu.ditu3.utils.Constant;
import com.atguigu.ditu3.utils.DbUtils;
import com.atguigu.ditu3.utils.MyUtils;
import com.atguigu.ditu3.utils.SpUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactActivity extends Activity {

    private ListView lv;
    private List<Map<String, String>> contactList;
    private MyAdapter adapter;
    private Context ctx;
   private ContactBean con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_contact);
        lv = (ListView) findViewById(R.id.lv);

        ctx = this;
        Button back= (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        contactList = MyUtils.readContact(this);


        adapter = new MyAdapter();
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //调用系统方法拨打电话
               // Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri
                 //       .parse("tel:" + contactList.get(position).get("phone")));
               //startActivity(dialIntent);
              // Intent intent = new Intent();// 创建Intent对象
              //  intent.setAction(Intent.ACTION_DIAL);// 为Intent设置动作

              //  intent.setData(Uri.parse("tel:"+contactList.get(position).get("phone")));// 为Intent设置数据
              //  startActivity(intent);// 将Intent传递给Activity
                con=new ContactBean();
                con.myPhone=SpUtils.getStringValue(getApplication(),"phone","");
                con.name=contactList.get(position).get("name");
                con.phone=contactList.get(position).get("phone");
                Toast.makeText(ContactActivity.this,""+con.name,Toast.LENGTH_LONG).show();;
                view.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                     ContactBean con=new ContactBean();
//                        con.myPhone=SpUtils.getStringValue(getApplication(),"phone","");
//                        con.name=contactList.get(position).get("name");
//                        con.phone=contactList.get(position).get("phone");
 //                       Toast.makeText(getApplication(),""+con.myPhone,Toast.LENGTH_LONG).show();;
                        DbUtils.requestHttp(con,Constant.ADDCONTACT,getApplication());



                    }
                });

            }
        });

    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return contactList.size();
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

            View view;
            ViewHolder vh = new ViewHolder();
            if(convertView==null){
                view=View.inflate(ContactActivity.this,R.layout.item,null);
                vh.tv_phone= (TextView) view.findViewById(R.id.tv_phone);
                vh.tv_contact= (TextView) view.findViewById(R.id.tv_contact);
                vh.btn_add=(Button) view.findViewById(R.id.btn_add);
                view.setTag(vh);

            }else{

                view=convertView;
                vh= (ViewHolder) view.getTag();
            }

            HashMap<String, String> map = (HashMap<String, String>) contactList.get(position);

            vh.tv_contact.setText(map.get("name"));
            vh.tv_phone.setText(map.get("phone"));
            String userphone = SpUtils.getStringValue(ctx, "userphone", "");
            final ContactBean contactBean = new ContactBean();
            contactBean.myPhone =userphone;
            contactBean.name=map.get("name");

            contactBean.phone=map.get("phone").replaceAll(" ","").replaceAll("-","");
            vh.btn_add.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    DbUtils.requestHttp(contactBean, Constant.ADDCONTACT,ctx);
                }
            });
            return view;
        }
    }

    private class ViewHolder{

        public TextView tv_phone;
        public TextView tv_contact;
        public Button  btn_add;


    }
}
