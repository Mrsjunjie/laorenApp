package com.atguigu.ditu3.utils;

/**
 * Created by Administrator on 2017/4/26.
 */

public class Constant {
    public static final String conn="http://192.168.0.110:8080";
    public static final String  REGIST=conn+"/Gson/com.servlet/RegistServlet";
    public static final String  LOGIN=conn+"/Gson/com.servlet/LoginServlet";//用户登录
    public static final String  USERINFO=conn+"/Gson/com.servlet/UserInfoServlet";//获取用户资料
    public static final String  FINDINFO=conn+"/Gson/com.servlet/FindServlet";//寻找用户
    public static final String  ADDCONTACT=conn+"/Gson/com.servlet/AddServlet";//添加联系人
    public static final String  ADD_CONTACT=conn+"/Gson/com.servlet/ContactServlet";//得到联系人电话
    public static final String  UPDATE_CONTACT=conn+"/Gson/com.servlet/UpdateServlet";//修改用户资料
    public static final String  DELETE_CONTACT=conn+"/Gson/com.servlet/DeleteContactServlet";
    public static final String  SETTING_CONTACT=conn+"/Gson/com.servlet/SettingServlet";
    public static final String  GET_SETTING_CONTACT=conn+"/Gson/com.servlet/GetSettingContactServlet";//得到紧急联系人
    public static final String  DELETE_SETTING_CONTACT=conn+"/Gson/com.servlet/DeleteSettingServlet";
    public static final String  ADD_ADDRESS=conn+"/Gson/com.servlet/AddressServlet";//添加轨迹数据
    public static final String  GET_ADDRESS=conn+"/Gson/com.servlet/GetAddressServlet";
    public static final String  GET_GUIJIADDRESS=conn+"/Gson/com.servlet/GetGuijiServlet";//得到添加的好友
    public static final String  CHAKAN_FRIEND="https://www.baidu.com/";//查看好友

//    public static final String  REGIST="http://10.10.26.15:8080/LrsServce/servlet/RegistServlet";
//    public static final String  LOGIN="http://10.10.26.15:8080/LrsServce/servlet/LoginServlet";
//    public static final String  USERINFO="http://10.10.26.15:8080/LrsServce/servlet/UserInfoServlet";
//    public static final String  FINDINFO="http://10.10.26.15:8080/LrsServce/servlet/FindServlet";
//    public static final String  ADDCONTACT="http://10.10.26.15:8080/LrsServce/servlet/AddServlet";
//    public static final String  ADD_CONTACT="http://10.10.26.15:8080/LrsServce/servlet/ContactServlet";
//    public static final String  UPDATE_CONTACT="http://10.10.26.15:8080/LrsServce/servlet/UpdateServlet";
//    public static final String  DELETE_CONTACT="http://10.10.26.15:8080/LrsServce/servlet/DeleteContactServlet";
//    public static final String  SETTING_CONTACT="http://10.10.26.15:8080/LrsServce/servlet/SettingServlet";
//    public static final String  GET_SETTING_CONTACT="http://10.10.26.15:8080/LrsServce/servlet/GetSettingContactServlet";
//    public static final String  DELETE_SETTING_CONTACT="http://10.10.26.15:8080/LrsServce/servlet/DeleteSettingServlet";
//    public static final String  ADD_ADDRESS="http://10.10.26.15:8080/LrsServce/servlet/AddressServlet";
//    public static final String  GET_ADDRESS="http://10.10.26.15:8080/LrsServce/servlet/GetAddressServlet";
}
