package com.atguigu.ditu3.activity.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atguigu.ditu3.R;
import com.atguigu.ditu3.activity.bean.ContactBean;
import com.atguigu.ditu3.activity.bean.UserBean;
import com.atguigu.ditu3.utils.Constant;
import com.atguigu.ditu3.utils.DbUtils;

import java.util.List;

/**
 * Created by zx on 2017/9/10.
 */

public class haoyouadapter extends BaseAdapter{
    private final Context mcontxt;
    private final List<ContactBean> list;
    private ContactBean contactBean;

    public haoyouadapter(Context mContxet, List<ContactBean> list){
      this.mcontxt=mContxet;
        this.list=list;
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
        View v = null;
        viewhodler viewh = null;
        viewh=new viewhodler();
        if(convertView==null){
             v=View.inflate(mcontxt,R.layout.firend_item,null);
             viewh.tv_name=(TextView) v.findViewById(R.id.tv_name);
            viewh.tv_number=(TextView) v.findViewById(R.id.tv_number);
            viewh.tv_chakan=(TextView) v.findViewById(R.id.tv_chakan);
            v.setTag(viewh);
        }else{
            v=convertView;
            viewh= (viewhodler) v.getTag();
        }
        contactBean =list.get(position);
        viewh.tv_name.setText(contactBean.name);
        viewh.tv_number.setText(contactBean.phone);
        viewh.tv_chakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UserBean use=new UserBean();
//               use.username =contactBean.name;
//                use.phone=contactBean.phone;
//               // DbUtils.requestHttp(use, Constant.CHAKAN_FRIEND,mcontxt);

            }
        });
        return v;
    }
private class viewhodler{
    private TextView tv_name;
    private TextView tv_number;
    private  TextView tv_chakan;
}
}
