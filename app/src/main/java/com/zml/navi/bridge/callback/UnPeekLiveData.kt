package com.zml.navi.bridge.callback

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * @package: com.zml.navi.bridge.callback
 * @date on: 2020/9/4 10:20
 * @author : zmliang
 * @company: qvbian
 **/
class UnPeekLiveData<T> : MutableLiveData<T>() {

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, observer)
        hook(observer)
    }


    fun hook(observer: Observer< in T>){

    }

}