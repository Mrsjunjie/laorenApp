package com.atguigu.ditu3.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;

import com.atguigu.ditu3.R;
import com.atguigu.ditu3.activity.fragment.Leftmenu;
import com.atguigu.ditu3.activity.fragment.mainui;
import com.atguigu.ditu3.activity.fragment.middlemenul;
import com.atguigu.ditu3.utils.SpUtils;

import java.util.List;

public class zhuActivity extends FragmentActivity {
    private mainui mainUi;
    private Leftmenu leftmenul;
    private middlemenul middle;
   // private List<BaseFragment> mBaseFragment;
    private Context mContent;
    private  String usephone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainUi=new mainui(this);
        this.mContent=this;
        setContentView(mainUi);
        leftmenul=new Leftmenu();
         usephone= SpUtils.getStringValue(mContent,"usephone","");
        leftmenul.leftmenu(mContent,usephone);

        getSupportFragmentManager().beginTransaction().add(mainUi.LEFT_ID,leftmenul).commit();
        middle=new middlemenul();
        middle.middlemenul(mContent,usephone);
        getSupportFragmentManager().beginTransaction().add(mainUi.MIDEELE_ID,middle).commit();
    }
}
