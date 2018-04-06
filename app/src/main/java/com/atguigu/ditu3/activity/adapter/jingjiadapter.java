package com.atguigu.ditu3.activity.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atguigu.ditu3.R;
import com.atguigu.ditu3.activity.bean.ContactBean;

import java.util.List;

/**
 * Created by zx on 2017/9/26.
 */

public class jingjiadapter extends BaseAdapter {
    private  Context mcontxt;
    private List<ContactBean> list;
    private ContactBean contactBean;
    public  jingjiadapter(Context mcontxt,List<ContactBean> list){
        this.list=list;
        this.mcontxt=mcontxt;
    }
    @Override
    public int getCount() {
        if(list!=null){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View V=null;
        VIEW vie=null;
        vie=new VIEW();
        if(convertView==null){
            V=View.inflate(mcontxt, R.layout.item_frends,null);
            vie.textView1=(TextView) V.findViewById(R.id.tv_name);
            vie.textView2=(TextView) V.findViewById(R.id.tv_number);
            V.setTag(vie);
        }else{
            V=convertView;
            vie= (VIEW) V.getTag();
        }
        contactBean =list.get(position);
        vie.textView1.setText(contactBean.name);
        vie.textView2.setText(contactBean.phone);
        return V;
    }

   private class VIEW{
    private TextView textView2;
    private TextView textView1;
   }

}
