package com.zml.navi.bridge.callback

import androidx.lifecycle.ViewModel

/**
 * @package: com.zml.navi.bridge.callback
 * @date on: 2020/9/4 10:17
 * @author : zmliang
 * @company: qvbian
 **/
class SharedViewModel : ViewModel() {

    val timeToAddSlideListener = UnPeekLiveData<Boolean>()


    val closeSlidePanelIfExpanded= UnPeekLiveData<Boolean>()


    val activityCanBeClosedDirectly= UnPeekLiveData<Boolean>()


    val openOrCloseDrawer = UnPeekLiveData<Boolean>()


    val enableSwipeDrawer = UnPeekLiveData<Boolean>()


    companion object{

        val tagOfSecondaryPages = ArrayList<String>()
    }


}