package com.zml.navi.manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import com.zml.navi.util.NetworkUtils


/**
 * @package: com.zml.navi.manager
 * @date on: 2020/9/9 13:49
 * @author : zmliang
 * @company: qvbian
 **/
class NetworkStateReceive :BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals(ConnectivityManager.CONNECTIVITY_ACTION) ){

            if (!NetworkUtils.isConnected()){
                Toast.makeText(context,"网络不给力",Toast.LENGTH_LONG).show()
            }
        }
    }


}