package com.atguigu.ditu3.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

public class SpUtils {


    private static SharedPreferences sp;

    public static void putBooleanValue(Context context, String key,
                                       boolean value) {

        if (sp == null) {
            sp = context.getSharedPreferences("congfig", 0);
        }
        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBooleanValue(Context context, String key,
                                          boolean defValue) {

        if (sp == null) {
            sp = context.getSharedPreferences("congfig", 0);
        }
        return sp.getBoolean(key, defValue);
    }

    public static void putStringValue(Context context, String key,
                                      String value) {

        if (sp == null) {
            sp = context.getSharedPreferences("congfig", 0);
        }
        sp.edit().putString(key, value).commit();
    }

    public static String getStringValue(Context context, String key,
                                        String defValue) {

        if (sp == null) {
            sp = context.getSharedPreferences("congfig", 0);
        }
        return sp.getString(key, defValue);
    }

    public static void rmove(Context context, String key) {
        // TODO Auto-generated method stub
        if (sp == null) {
            sp = context.getSharedPreferences("congfig", 0);
        }
        sp.edit().remove(key).commit();
    }

}
