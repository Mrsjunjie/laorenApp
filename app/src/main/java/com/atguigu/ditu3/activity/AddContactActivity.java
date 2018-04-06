package com.atguigu.ditu3.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.atguigu.ditu3.R;
import com.atguigu.ditu3.activity.bean.UserBean;
import com.atguigu.ditu3.utils.Constant;
import com.atguigu.ditu3.utils.DbUtils;
import com.atguigu.ditu3.utils.MyUtils;
import com.atguigu.ditu3.utils.SpUtils;


/**
 * Created by Administrator on 2017/4/18.
 */

public class AddContactActivity extends Activity {

    private Button btn_add_contact;
	private Button btn_search;
    private EditText et_number;
    private AddContactActivity ctx;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        ctx=this;
        btn_add_contact = (Button) findViewById(R.id.btn_add_contact);
        btn_search = (Button) findViewById(R.id.btn_search);
        et_number = (EditText) findViewById(R.id.et_number);
        Button back= (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initData();


    }

    private void initData() {
    	btn_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                String number = et_number.getText().toString().trim();
                UserBean userBean = new UserBean();
                userBean.phone=number;

                if(TextUtils.isEmpty(number)){
                    MyUtils.Toast(ctx,"请输入联系人手机号！");
                    return;
                }else{
                    SpUtils.putStringValue(ctx,"findNumber",number);
                    DbUtils.requestHttp(userBean, Constant.FINDINFO,ctx);
                }




				
			}
		});
    	
        btn_add_contact.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddContactActivity.this,ContactActivity.class);
                startActivity(intent);
                
            }
        });
    }
}
