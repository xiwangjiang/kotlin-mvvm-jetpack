package com.zml.navi.util

import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.view.WindowManager

/**
 * @package: com.zml.navi.util
 * @date on: 2020/9/10 16:50
 * @author : zmliang
 * @company: qvbian
 **/
object ScreenUtils {

    fun getScreenWidth() : Int{
        val wm = Utils.getApp()?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
            wm.defaultDisplay.getRealSize(point)
        }else{
            wm.defaultDisplay.getSize(point)
        }
        return point.x
    }

    fun isPortrait():Boolean{
        return Utils.getApp()!!.resources.configuration
            .orientation == Configuration.ORIENTATION_PORTRAIT
    }
















}