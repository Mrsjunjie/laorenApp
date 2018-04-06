package com.atguigu.ditu3.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.ditu3.R;
import com.atguigu.ditu3.activity.bean.ContactBean;
import com.atguigu.ditu3.activity.bean.UserBean;
import com.atguigu.ditu3.utils.Constant;
import com.atguigu.ditu3.utils.DbUtils;
import com.atguigu.ditu3.utils.SpUtils;
import com.baidu.location.h.g;

import static com.atguigu.ditu3.utils.Constant.CHAKAN_FRIEND;

public class loginl extends Activity {
    private EditText et_phone;
    private EditText et_pwd;
    private Button btn_login;
    private loginl ctx;
    private CheckBox cb;
    private String phone;
    private String pwdStr;
    private TextView tv_regist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginl);
        ctx=this;
        initUI();

        initData();
    }
    private void initData() {
        tv_regist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent=new Intent(ctx,regist.class);
                startActivity(intent);
            }
        });




        btn_login.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(loginl.this, zhuActivity.class);
               // startActivity(intent);
                //Intent intent=new Intent(ctx,badidu.class);
                //获取用户输入的用户名和密码
                phone =  et_phone.getText().toString().trim();
                pwdStr =  et_pwd.getText().toString().trim();
                UserBean userBean = new UserBean();
                userBean.phone=phone;
                userBean.password=pwdStr;
                //判断用户输入的用户名和密码是否为空
                if(TextUtils.isEmpty(phone)||TextUtils.isEmpty(pwdStr)){
                    Toast.makeText(ctx,"手机号或密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    //判断记住密码是否选中
                    if(cb.isChecked()){
                        //选中之后对用户输入的密码进行保存
                        SpUtils.putStringValue(ctx,"phone",phone);
                        SpUtils.putStringValue(ctx,"pwd",pwdStr);

                    }else{
                        SpUtils.putStringValue(ctx,"phone","");
                        SpUtils.putStringValue(ctx,"pwd","");
                    }
                    //如果用户名和密码都不为空连接服务器进行判断用户名和密码是否正确;
                    if (checkNetwork()) {
                        DbUtils.requestHttp(userBean,  Constant.LOGIN, ctx);

                    }else {
                        Toast.makeText(ctx,"断网了",Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

    }

    private void initUI() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        btn_login = (Button) findViewById(R.id.btn_login);
        cb = (CheckBox) findViewById(R.id.cb);
        tv_regist = (TextView) findViewById(R.id.tv_regist);
        String phone = SpUtils.getStringValue(ctx, "phone", "");
        String pwd = SpUtils.getStringValue(ctx, "pwd", "");
        if(!TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(pwd)){
            et_phone.setText(phone);
            et_pwd.setText(pwd);
            cb.setChecked(true);
        }

    }
    private boolean checkNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }
}
