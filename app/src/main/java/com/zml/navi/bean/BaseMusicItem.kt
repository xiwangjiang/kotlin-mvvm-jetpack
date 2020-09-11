package com.zml.navi.bean

/**
 * @package: com.zml.navi.bean
 * @date on: 2020/9/11 15:25
 * @author : zmliang
 * @company: qvbian
 **/
data class BaseMusicItem<A : BaseArtistItem> (val musicId:String,val coverImg:String,val url:String,
val title:String,val artist: A){

}