package com.zml.navi.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.lang.reflect.Field
import java.util.*
import kotlin.collections.HashMap

/**
 * @package: com.zml.navi.util
 * @date on: 2020/9/9 14:02
 * @author : zmliang
 * @company: qvbian
 **/
object Utils {

    private val PERMISSION_ACTIVITY_CLASS_NAME =
        "com.blankj.utilcode.util.PermissionUtils.PermissionActivity"

    private val ACTIVITY_LIFECYCLE = ActivityLifecycleImpl()

    private var sApplication: Application? = null


    fun init(app: Application) {
        if (sApplication == null) {
            sApplication = app
            sApplication!!.registerActivityLifecycleCallbacks(ACTIVITY_LIFECYCLE)
        } else {
            if (app.javaClass != sApplication!!::class.java) {
                sApplication!!.unregisterActivityLifecycleCallbacks(ACTIVITY_LIFECYCLE)
                ACTIVITY_LIFECYCLE.mActivityList.clear()
                sApplication = app
                sApplication!!.registerActivityLifecycleCallbacks(ACTIVITY_LIFECYCLE)
            }
        }

    }


    fun getApp(): Application? {
        if (sApplication != null) {
            return sApplication
        }
        val app = getApplicationByReflect()
        init(app)
        return app
    }

    @SuppressLint("PrivateApi")
    private fun getApplicationByReflect(): Application {
        try {
            val activityThread = Class.forName("android.app.ActivityThread")
            val thread = activityThread.getMethod("currentActivityThread").invoke(null)
            val app = activityThread.getMethod("getApplication").invoke(thread) ?: throw NullPointerException("u should init firstly")
            return app as Application
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        }

        throw NullPointerException("u should init firstly")
    }


    class ActivityLifecycleImpl : Application.ActivityLifecycleCallbacks {

        val mActivityList = LinkedList<Activity>()

        val mStateListenerMap = HashMap<Object, OnAppStatusChangedListener>()

        val mDestroyedListenerMap = HashMap<Activity, Set<OnActivityDestroyedListener>>()

        var mForegroundCount = 0

        var mConfigCount = 0

        var mIsBackground = false

        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStarted(activity: Activity) {
            if (!mIsBackground){
                setTopActivity(activity)
            }
            if (mConfigCount<0){
                ++mConfigCount
            }else{
                ++mForegroundCount
            }
        }

        override fun onActivityDestroyed(activity: Activity) {
            mActivityList.remove(activity)
            consumeOnActivityDestroyedListener(activity)
            fixSoftInputLeaks(activity)
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityStopped(activity: Activity) {
            if (activity.isChangingConfigurations){
                --mConfigCount
            }else{
                --mForegroundCount
                if (mForegroundCount<=0){
                    mIsBackground = true
                    postStatus(false)
                }
            }
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            setTopActivity(activity)
        }

        override fun onActivityResumed(activity: Activity) {
            setTopActivity(activity)
            if (mIsBackground){
                mIsBackground = false
                postStatus(true)
            }
        }

        private fun postStatus(isForeground : Boolean){
            if (mStateListenerMap.isEmpty()){
                return
            }
            mStateListenerMap.values.forEach {
                if (isForeground){
                    it.onForeground()
                }else{
                    it.onBackground()
                }
            }
        }

        private fun setTopActivity(activity: Activity) {
            if (PERMISSION_ACTIVITY_CLASS_NAME == activity::class.java.name) {
                return
            }
            if (mActivityList.contains(activity)) {
                if (mActivityList.last() != activity) {
                    mActivityList.remove(activity)
                    mActivityList.addLast(activity)
                }
            } else {
                mActivityList.addLast(activity)
            }
        }

        private fun consumeOnActivityDestroyedListener(activity: Activity) {
            val iterator = mDestroyedListenerMap.entries.iterator()
            while (iterator.hasNext()) {
                val enty = iterator.next()
                if (enty.key == activity) {
                    val value = enty.value
                    value.forEach { listener: OnActivityDestroyedListener ->
                        listener.onActivityDestroyed(activity)
                    }
                    iterator.remove()
                }
            }
        }

        private fun getTopActivityByReflect(): Activity? {
            try {
                val activityThreadClass = Class.forName("android.app.ActivityThread")
                val currentActivityThreadMethod =
                    activityThreadClass.getMethod("currentActivityThread").invoke(null)
                val mActivityListField = activityThreadClass.getDeclaredField("mActivityList")
                mActivityListField.isAccessible = true
                val activities =
                    mActivityListField.get(currentActivityThreadMethod) as Map<*, Activity>

                activities.values.forEach { activityRecord: Any ->
                    val activityRecordClass = activityRecord.javaClass
                    val pausedField = activityRecordClass.getDeclaredField("paused")
                    pausedField.isAccessible = true
                    if (!pausedField.getBoolean(activityRecord)) {
                        val activityField = activityRecordClass.getDeclaredField("activity")
                        activityField.isAccessible = true
                        return activityField.get(activityRecord) as Activity
                    }


                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }

        private fun fixSoftInputLeaks(activity: Activity){
            val imm = getApp()?.getSystemService(Context.INPUT_METHOD_SERVICE) ?: return
            val leakViews = arrayOf("mLastSrvView", "mCurRootView", "mServedView", "mNextServedView")
            leakViews.forEach {
                try {
                    val leakViewField: Field =
                        InputMethodManager::class.java.getDeclaredField(it)

                    if (!leakViewField.isAccessible){
                        leakViewField.isAccessible=true
                    }
                    val obj =leakViewField.get(imm)
                    if (obj !is View){
                        return@forEach
                    }
                    if (obj.rootView == activity.window.decorView.rootView){
                        leakViewField.set(imm,null)
                    }

                }catch (ignore : Throwable){
                    ignore.printStackTrace()
                }
            }
        }

    }


    interface OnAppStatusChangedListener {
        fun onForeground()

        fun onBackground()
    }

    interface OnActivityDestroyedListener {
        fun onActivityDestroyed(activity: Activity)
    }

}