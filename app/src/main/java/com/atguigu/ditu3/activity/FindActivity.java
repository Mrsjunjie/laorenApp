package com.atguigu.ditu3.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atguigu.ditu3.R;
import com.atguigu.ditu3.activity.bean.ContactBean;
import com.atguigu.ditu3.utils.Constant;
import com.atguigu.ditu3.utils.DbUtils;
import com.atguigu.ditu3.utils.SpUtils;

public class FindActivity extends Activity implements View.OnClickListener{

    private LinearLayout ll_result;
    private TextView tv_result;
    private Button btn_add;
    private String findNumber;
    private String desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_find);
        ll_result = (LinearLayout) findViewById(R.id.ll_result);
        tv_result = (TextView) findViewById(R.id.tv_result);
        btn_add = (Button) findViewById(R.id.btn_add);
        TextView tv_name= (TextView) findViewById(R.id.tv_name);
        TextView tv_number= (TextView) findViewById(R.id.tv_number);
        Button back= (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_add.setOnClickListener(this);
        desc = getIntent().getStringExtra("desc");
        if(desc.equals("不存在")){
            tv_result.setVisibility(View.VISIBLE);
            ll_result.setVisibility(View.GONE);

        }else {
            tv_name.setText(desc);
            findNumber = SpUtils.getStringValue(this, "findNumber", "");
            tv_number.setText(findNumber);
            ll_result.setVisibility(View.VISIBLE);
            tv_result.setVisibility(View.GONE );
        }
    }

    @Override
    public void onClick(View view) {
        String userphone = SpUtils.getStringValue(this, "userphone", "");
        ContactBean contactBean = new ContactBean();
        contactBean.myPhone =userphone;
        contactBean.name=desc;
        contactBean.phone=findNumber;
        DbUtils.requestHttp(contactBean, Constant.ADDCONTACT,this);




    }
}
