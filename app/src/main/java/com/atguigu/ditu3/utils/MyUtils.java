package com.atguigu.ditu3.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/13 0013.
 */

public   class  MyUtils {

    public static List<Map<String, String>> readContact(Context ctx) {
        // 首先,从raw_contacts中读取联系人的id("contact_id")
        // 其次, 根据contact_id从data表中查询出相应的电话号码和联系人名称
        // 然后,根据mimetype来区分哪个是联系人,哪个是电话号码

       // Uri rawContactsUri = Uri.parse("content://com.android.contacts/raw_contacts");
       // Uri dataUri = Uri.parse("content://com.android.contacts/data");

       // ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        // 把所有的联系人放到list
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        // 得到一个内容解析器
        ContentResolver resolver =ctx.getContentResolver();
        // 获取联系人表对应的内容提供者url raw_contacts表和data表
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri datauri = Uri.parse("content://com.android.contacts/data");

        Cursor cursor = resolver.query(uri, new String[] { "contact_id" },
                null, null, null);

        // 获取contact_id 获取联系人id
        while (cursor.moveToNext()) {
            String contact_id = cursor.getString(0);

            if (contact_id != null) {
                // 具体的某个联系人
                Map<String, String> map = new HashMap<String, String>();

                // 如果不为空 查询对应data表的联系人信息
                Cursor dataCursor = resolver.query(datauri, new String[] {
                                "data1", "mimetype" }, "contact_id=?",
                        new String[] { contact_id }, null);
                while (dataCursor.moveToNext()) {
                    String data1 = dataCursor.getString(0);
                    String mimetype = dataCursor.getString(1);
                    System.out.println("data1 ==" + data1 + "mimetype == "
                            + mimetype);

                    if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                        System.out.println("电话:" + data1);
                        map.put("phone", data1);
                    } else if ("vnd.android.cursor.item/name".equals(mimetype)) {
                        System.out.println("姓名:" + data1);
                        map.put("name", data1);
                    }
                }

                list.add(map);
                // 释放游标
                dataCursor.close();
            }

        }
        cursor.close();
        return list;

}

    public static boolean getRunningServer(Context context,String serverName){
        ActivityManager  am= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(100);
        for(ActivityManager.RunningServiceInfo info:runningServices){

            ComponentName service = info.service;
            String className = service.getClassName();

            if(className.equals(serverName)){

                return true;
            }


        }
        return false;
    }
    public static void Toast(Context context,String string){
        Toast.makeText(context, string,Toast.LENGTH_SHORT).show();
    }
//    public static String getTodayDateTime(long date) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
//                Locale.getDefault());
//        String dateString = format.format(date);
//        return dateString;
//    }
    public static String getCurrentDateTime(){
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         String time = df.format(date);
        return time;
    }


}
