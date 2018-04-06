package com.atguigu.ditu3.activity;

import android.app.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.atguigu.ditu3.R;
import com.atguigu.ditu3.activity.bean.UserBean;
import com.atguigu.ditu3.utils.Constant;
import com.atguigu.ditu3.utils.DbUtils;

public class regist extends Activity {
    private EditText et_name;
    private EditText et_password;

   private Button tijiao;
    private EditText et_sex;
    private EditText et_age;
    private EditText et_phone;
    private EditText et_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        initUI();
    }

    private void initUI() {
        et_name = (EditText) findViewById(R.id.et_name);
        et_password = (EditText) findViewById(R.id.et_password);


        et_sex = (EditText) findViewById(R.id.et_sex);
        et_age = (EditText) findViewById(R.id.et_age);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_address = (EditText) findViewById(R.id.et_address);
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        et_sex.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (!hasFocus) {
                    if (et_sex.getText().toString().trim().length() < 0) {
                        Toast.makeText(getApplication(), "性别是男与女", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
        et_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (!hasFocus) {
                    if (et_phone.getText().toString().trim().length() < 11 && et_phone.getText().toString().trim().length() > 11) {
                        Toast.makeText(getApplication(), "号码是11位", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
        tijiao= (Button) findViewById(R.id.tijiao);
        tijiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString().trim();
                String password = et_password.getText().toString().trim();

                String sex = et_sex.getText().toString().trim();
                String age = et_age.getText().toString().trim();
                String phone = et_phone.getText().toString().trim();
                String address = et_address.getText().toString().trim();


                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password) ||
                        TextUtils.isEmpty(sex) || TextUtils.isEmpty(age) ||
                        TextUtils.isEmpty(phone) || TextUtils.isEmpty(address)) {

                    return;
                } else {
                    UserBean userBean = new UserBean();
                    userBean.username = name;
                    userBean.password = password;

                    userBean.sex = sex;
                    userBean.age = age;
                    userBean.phone = phone;
                    userBean.address = address;
                    DbUtils.requestHttp(userBean, Constant.REGIST,getApplication());

                }
            }
        });
    }


}
