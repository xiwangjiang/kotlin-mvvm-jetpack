package com.zml.navi

import android.Manifest
import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.core.app.AppOpsManagerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

/**
 * @package: com.zml.navi
 * @date on: 2020/9/2 17:58
 * @author : zmliang
 * @company: qvbian
 **/
class App : Application(), ViewModelStoreOwner {

    private var mAppVieModelStore : ViewModelStore ?=null
    private var mFactory : ViewModelProvider.Factory?=null

    override fun onCreate() {
        super.onCreate()


        var result = AppOpsManagerCompat.noteProxyOp(this, AppOpsManagerCompat.permissionToOp(
            Manifest.permission.READ_PHONE_STATE)!!,this.getPackageName())

        if (result!= AppOpsManagerCompat.MODE_ALLOWED){
            //if (result!=PackageManager.PERMISSION_GRANTED){
            Log.e("ZMLIANG","READ_PHONE_STATE 是否被授予 : false")
        }else {
            Log.e("ZMLIANG","READ_PHONE_STATE 是否被授予 : true"+"; ")
        }

        mAppVieModelStore = ViewModelStore()
    }



    override fun getViewModelStore(): ViewModelStore {
        return mAppVieModelStore!!
    }

    fun getAppViewModelProvider(activity: Activity) : ViewModelProvider{
        return ViewModelProvider((activity.applicationContext as App),
            (getAppFactory(activity)))
    }

    fun getAppFactory(activity: Activity) : ViewModelProvider.Factory{
        val application = checkApplication(activity)
        if (mFactory == null){
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        }
        return mFactory!!
    }


    fun checkApplication(activity: Activity) : Application{
        val application = activity.application
        if (application == null){
            throw IllegalStateException("Your activity/fragment is not yet attached to \"\n" +
                    "                    + \"Application. You can't request ViewModel before onCreate call.")
        }
        return application
    }





}