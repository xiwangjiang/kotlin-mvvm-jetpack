package com.zml.navi.util

import android.content.res.Resources
import android.util.DisplayMetrics
import java.lang.Exception
import java.lang.reflect.Field

/**
 * @package: com.zml.navi.util
 * @date on: 2020/9/10 16:57
 * @author : zmliang
 * @company: qvbian
 **/
object AdaptScreenUtils {

    private var isInitMiui : Boolean = false
    private var mTmpMetricsField : Field? = null


    fun adaptWidth(resource : Resources,designWidth : Int) :Resources{
        val dm = getDisplayMetrics(resource)
        dm.xdpi =  (dm.widthPixels*72f)/designWidth
        setAppDmXdpi(dm.xdpi)
        return resource
    }

    fun adaptHeight(resource : Resources,designHeight : Int):Resources{
        val dm = getDisplayMetrics(resource)
        dm.xdpi = (dm.heightPixels*72f)/designHeight
        setAppDmXdpi(dm.xdpi)
        return resource
    }

    fun setAppDmXdpi(xdpi : Float){
        Utils.getApp()!!.resources.displayMetrics.xdpi = xdpi
    }


    fun getDisplayMetrics(resource: Resources):DisplayMetrics{
        return getMiuiTmpMetrics(resource) ?: return resource.displayMetrics
    }

    fun getMiuiTmpMetrics(resource: Resources) : DisplayMetrics?{
        if (!isInitMiui){
            var ret : DisplayMetrics?=null
            val simpleName = resource.javaClass.simpleName
            if (simpleName == "MiuiResources" || "XResources" == simpleName){
                try {
                    mTmpMetricsField = resource.javaClass.getDeclaredField("mTmpMetrics")
                    mTmpMetricsField!!.isAccessible = true
                    ret = mTmpMetricsField!!.get(resource) as DisplayMetrics
                }catch (e : Exception){
                    e.printStackTrace()
                }
            }
            isInitMiui = true
            return ret
        }
        return try {
            mTmpMetricsField!!.get(resource) as DisplayMetrics
        }catch (e:Exception){
            null
        }
    }

}















