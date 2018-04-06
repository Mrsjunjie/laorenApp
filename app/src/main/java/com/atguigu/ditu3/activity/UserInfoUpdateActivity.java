package com.atguigu.ditu3.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.atguigu.ditu3.R;
import com.atguigu.ditu3.activity.bean.UserBean;
import com.atguigu.ditu3.utils.Constant;
import com.atguigu.ditu3.utils.DbUtils;
import com.atguigu.ditu3.utils.SpUtils;


/**
 * Created by Administrator on 2017/4/18.
 */

public class UserInfoUpdateActivity extends Activity {

    private UserInfoUpdateActivity ctx;
    private EditText et_username;
    private EditText et_sex;
    private EditText et_age;
    private EditText et_address;
    private Button btn_update;
    private String username;
    private String age;
    private String sex;
    private String address;
    private  Button back;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
        ctx=this;
        initUI();
        initData();

    }

    private void initData() {

        username = getIntent().getStringExtra("username");
        age = getIntent().getStringExtra("age");
        sex = getIntent().getStringExtra("sex");
        address = getIntent().getStringExtra("address");
        et_username.setText(username);
        et_age.setText(age);
        et_sex.setText(sex);
        et_address.setText(address);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserBean userBean = new UserBean();
                String address = et_address.getText().toString();
                String username = et_username.getText().toString();
                String age = et_age.getText().toString();
                String sex = et_sex.getText().toString();

                String userphone = SpUtils.getStringValue(ctx, "userphone", "");

                userBean.phone=userphone;
                userBean.username=username;
                userBean.age=age;
                userBean.sex=sex;
                userBean.address=address;
                DbUtils.requestHttp(userBean, Constant.UPDATE_CONTACT,ctx);

            }
        });
    }

    private void initUI() {
        back= (Button) findViewById(R.id.back);
        et_username = (EditText) findViewById(R.id.et_username);
        et_sex = (EditText) findViewById(R.id.et_sex);
        et_age = (EditText) findViewById(R.id.et_age);
        et_address = (EditText) findViewById(R.id.et_address);
        btn_update = (Button) findViewById(R.id.btn_update);
    }
}
