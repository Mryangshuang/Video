package com.ys.com.video.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.ys.com.video.Tool.ToastTool;

public class NetWorkStateReceiver extends BroadcastReceiver {
    private boolean isfirstTime;
    SharedPreferences config;
    public NetWorkStateReceiver() {

    }
    @Override
    public void onReceive(Context context, Intent intent) {
//        如果是非第一次  就可以进行弹toast
        config = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        isfirstTime=config.getBoolean("isfirstTime",true);

        //检测API小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager systemService = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo phone = systemService.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifi = systemService.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifi.isConnected() && phone.isConnected()&&!isfirstTime) {
                ToastTool.toast(context, "wifi和移动网络");
            } else if (!wifi.isConnected() && phone.isConnected()&&!isfirstTime) {
                ToastTool.toast(context, "移动网络");
            } else if (wifi.isConnected() && !phone.isConnected()&&!isfirstTime) {
                ToastTool.toast(context, "wifi");
            } else if (!wifi.isConnected() && !phone.isConnected()&&!isfirstTime) {
                ToastTool.toast(context, "无网络链接");
            }
        } else {
            //检测API是大于23
            ConnectivityManager systemService = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取所有网络连接的信息
            Network[] allNetworks = systemService.getAllNetworks();
            Log.i("net", allNetworks.length + "");
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < allNetworks.length; i++) {
                if (allNetworks[i] != null) {
                    NetworkInfo info = systemService.getNetworkInfo(allNetworks[i]);
                    sb.append(info.getTypeName() + "");
                }
            }
            if (sb.toString().equals("")&&!isfirstTime){
                ToastTool.toast(context, "网络变化：无网络可用");
            }else if(!sb.toString().equals("")&&!isfirstTime){
                ToastTool.toast(context, "网络变化:"+sb.toString() +" 可用");
            }
        }
        config.edit().putBoolean("isfirstTime",false).commit();
    }
}
