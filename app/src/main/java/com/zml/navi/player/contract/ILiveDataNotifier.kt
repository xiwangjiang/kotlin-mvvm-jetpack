package com.zml.navi.player.contract

import androidx.lifecycle.MutableLiveData

/**
 * @package: com.zml.navi.player.contract
 * @date on: 2020/9/11 15:17
 * @author : zmliang
 * @company: qvbian
 **/
interface ILiveDataNotifier {

    fun getChangeMusicLiveData():MutableLiveData<ChangeMusic>



}