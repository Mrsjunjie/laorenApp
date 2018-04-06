package com.atguigu.ditu3;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.baidu.navisdk.adapter.BNRouteGuideManager;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviBaseCallbackModel;
import com.baidu.navisdk.adapter.BaiduNaviCommonModule;
import com.baidu.navisdk.adapter.NaviModuleFactory;
import com.baidu.navisdk.adapter.NaviModuleImpl;

public class BNDemoGuideActivity extends Activity {
    private final String TAG = BNDemoGuideActivity.class.getName();
    private BNRoutePlanNode mBNRoutePlanNode = null;
    private BaiduNaviCommonModule mBaiduNaviCommonModule = null;

    /*
     * 对于导航模块有两种方式来实现发起导航。 1：使用通用接口来实现 2：使用传统接口来实现
     *
     */
    // 是否使用通用接口
    private boolean useCommonInterface = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity.activityList.add(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        }
        View view = null;

            //使用通用接口
            mBaiduNaviCommonModule = NaviModuleFactory.getNaviModuleManager().getNaviCommonModule(
                    NaviModuleImpl.BNaviCommonModuleConstants.ROUTE_GUIDE_MODULE, this,
                    BNaviBaseCallbackModel.BNaviBaseCallbackConstants.CALLBACK_ROUTEGUIDE_TYPE, mOnNavigationListener);
            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onCreate();
                view = mBaiduNaviCommonModule.getView();
            }




        if (view != null) {
            setContentView(view);
        }

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mBNRoutePlanNode = (BNRoutePlanNode) bundle.getSerializable(MainActivity.ROUTE_PLAN_NODE);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onResume();

        }



    }

    protected void onPause() {
        super.onPause();


            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onPause();

        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onDestroy();
            }

        MainActivity.activityList.remove(this);

    }

    @Override
    protected void onStop() {
        super.onStop();

            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onStop();
            }


    }

    /*/
     * (non-Javadoc)
     * @see android.app.Activity#onBackPressed()
     * 此处onBackPressed传递false表示强制退出，true表示返回上一级，非强制退出
     */
    @Override
    public void onBackPressed() {

            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onBackPressed(true);

        }
    }

    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onConfigurationChanged(newConfig);
            }


    };


    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {

            if(mBaiduNaviCommonModule != null) {
                Bundle mBundle = new Bundle();
                mBundle.putInt(RouteGuideModuleConstants.KEY_TYPE_KEYCODE, keyCode);
                mBundle.putParcelable(RouteGuideModuleConstants.KEY_TYPE_EVENT, event);
                mBaiduNaviCommonModule.setModuleParams(RouteGuideModuleConstants.METHOD_TYPE_ON_KEY_DOWN, mBundle);
                try {
                    Boolean ret = (Boolean)mBundle.get(RET_COMMON_MODULE);
                    if(ret) {
                        return true;
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onStart() {
        super.onStart();
        // TODO Auto-generated method stub

            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onStart();

        }
    }


    private static final int MSG_SHOW = 1;
    private static final int MSG_HIDE = 2;
    private static final int MSG_RESET_NODE = 3;
    private Handler hd = null;



    private BNRouteGuideManager.OnNavigationListener mOnNavigationListener = new BNRouteGuideManager.OnNavigationListener() {

        @Override
        public void onNaviGuideEnd() {
            //退出导航
            finish();
        }

        @Override
        public void notifyOtherAction(int actionType, int arg1, int arg2, Object obj) {

            if (actionType == 0) {
                //导航到达目的地 自动退出
                Log.i(TAG, "notifyOtherAction actionType = " + actionType + ",导航到达目的地！");
            }

            Log.i(TAG, "actionType:" + actionType + "arg1:" + arg1 + "arg2:" + arg2 + "obj:" + obj.toString());
        }

    };

    private final static String RET_COMMON_MODULE = "module.ret";

    private interface RouteGuideModuleConstants {
        final static int METHOD_TYPE_ON_KEY_DOWN = 0x01;
        final static String KEY_TYPE_KEYCODE = "keyCode";
        final static String KEY_TYPE_EVENT = "event";
    }
}
