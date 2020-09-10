package com.zml.navi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.zml.navi.util.DeviceUtils

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("zmliang",""+DeviceUtils.uniqueDeviceID())
    }

}