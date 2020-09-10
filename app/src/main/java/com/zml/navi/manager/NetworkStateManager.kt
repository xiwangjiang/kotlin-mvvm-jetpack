package com.zml.navi.manager

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.zml.navi.App
import com.zml.navi.bridge.callback.UnPeekLiveData

/**
 * @package: com.zml.navi.manager
 * @date on: 2020/9/9 12:00
 * @author : zmliang
 * @company: qvbian
 **/
class NetworkStateManager private constructor():  DefaultLifecycleObserver{

    val mNetworkStateCallback = UnPeekLiveData<NetState>()
    var mNetworkStateReceive:NetworkStateReceive?=null


    companion object{
        val instance:NetworkStateManager by lazy {
            NetworkStateManager()
        }
    }


    override fun onStart(owner: LifecycleOwner) {
        mNetworkStateReceive = NetworkStateReceive()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        if (owner is AppCompatActivity){
            owner.registerReceiver(mNetworkStateReceive,filter)
        }else if (owner is Fragment){
            owner.activity!!.registerReceiver(mNetworkStateReceive,filter)
        }
    }


    override fun onStop(owner: LifecycleOwner) {
        if (owner is AppCompatActivity){
            owner.unregisterReceiver(mNetworkStateReceive)
        }else if (owner is Fragment){
            owner.activity!!.unregisterReceiver(mNetworkStateReceive)
        }
    }

}
















