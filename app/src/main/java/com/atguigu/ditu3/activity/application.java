package com.atguigu.ditu3.activity;

import android.app.Application;

import com.yolanda.nohttp.NoHttp;

/**
 * Created by zx on 2017/9/19.
 */

public class application  extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
        NoHttp.init(this);
    }
}
