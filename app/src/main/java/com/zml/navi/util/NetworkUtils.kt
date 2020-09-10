package com.zml.navi.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * @package: com.zml.navi.util
 * @date on: 2020/9/9 13:52
 * @author : zmliang
 * @company: qvbian
 **/
 object NetworkUtils {


    enum class NetworkType{
        NETWORK_ETHERNET,
        NETWORK_WIFI,
        NETWORK_4G,
        NETWORK_3G,
        NETWORK_2G,
        NETWORK_UNKNOWN,
        NETWORK_NO
    }



    fun getActiveNetworkInfo() : NetworkInfo?{
        val cm = Utils.getApp()?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }


    fun isConnected() : Boolean{
        val info = getActiveNetworkInfo()
        return info!=null && info.isConnected
    }



}