package com.zml.navi.base

import android.content.pm.ApplicationInfo
import android.content.res.Resources
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zml.navi.App
import com.zml.navi.bridge.callback.SharedViewModel
import com.zml.navi.manager.NetworkStateManager
import com.zml.navi.util.AdaptScreenUtils
import com.zml.navi.util.ScreenUtils

/**
 * @package: com.zml.navi.base
 * @date on: 2020/9/4 10:14
 * @author : zmliang
 * @company: qvbian
 **/
open class BaseActivity : AppCompatActivity() {

    protected var mSharedViewMode : SharedViewModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSharedViewMode = getAppViewModelProvider().get(SharedViewModel::class.java)

        NetworkStateManager.instance.mNetworkStateCallback.observe(this, Observer {
            TODO("这里可以执行统一的网络状态通知喝处理")
        })

    }


    override fun getResources(): Resources {
        return if (ScreenUtils.isPortrait()){
            AdaptScreenUtils.adaptWidth(super.getResources(),360)
        }else{
            AdaptScreenUtils.adaptHeight(super.getResources(),640)
        }
    }



    protected fun getAppViewModelProvider() : ViewModelProvider {
        return (applicationContext as App).getAppViewModelProvider(this)
    }


    fun isDebug() : Boolean{
        return (applicationContext.applicationInfo!!.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }

    fun toast(txt:String){
        Toast.makeText(applicationContext,txt,Toast.LENGTH_LONG).show()
    }

}

















