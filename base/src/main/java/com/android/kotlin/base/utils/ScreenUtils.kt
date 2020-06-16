package com.android.kotlin.base.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Point
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.view.WindowManager
import androidx.annotation.RequiresPermission

/**
 * created by mbm on 2020/6/16
 * 屏幕相关工具类
 */


/**
 * 获取屏幕宽度
 */
@SuppressLint("ObsoleteSdkInt")
fun Context.screenWidth(): Int {
    val wm = getSystemService(WINDOW_SERVICE) as WindowManager
    val point = Point()
    when (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        true -> wm.defaultDisplay.getRealSize(point)
        false -> wm.defaultDisplay.getSize(point)
    }
    return point.x
}

/**
 * 获取屏幕高度
 */
@SuppressLint("ObsoleteSdkInt")
fun Context.screenHeight(): Int {
    val wm = getSystemService(WINDOW_SERVICE) as WindowManager
    val point = Point()
    when (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        true -> wm.defaultDisplay.getRealSize(point)
        false -> wm.defaultDisplay.getSize(point)
    }
    return point.y
}

/**
 * 获取屏幕方向
 * 1竖屏   2横屏
 */
fun Context.screenOrientation(): Int {
    return resources.configuration.orientation
}

/**
 * 获取屏幕密度
 */
val screenDensity
    get() = Resources.getSystem().displayMetrics.density

/**
 * 获取屏幕DPI
 */
val screenDPI
    get() = Resources.getSystem().displayMetrics.densityDpi


/**
 * 设置横屏
 * 横屏有两个方向，在某个横屏方向下再次设置横屏会选择180°，故需要先判断当前是否已经横屏。
 */
fun Activity.setScreenLandscape() {
    if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }
}

/**
 * 设置竖屏
 */
fun Activity.setScreenPortrait() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}


/**
 * 横竖屏切换
 */
fun Activity.toggleScreenOrientation() {
    requestedOrientation =
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
}

/**
 * 锁定屏幕方向
 */
fun Activity.lockScreenOrientation() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
}

/**
 * 取消锁定屏幕方向
 */
fun Activity.unlockScreenOrientation() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
}


/**
 * 判断和设置是否全屏，赋值为true设置成全屏
 */
var Activity.isFullScreen: Boolean
    get() {
        val flag = WindowManager.LayoutParams.FLAG_FULLSCREEN
        return (window.attributes.flags and flag) == flag
    }
    set(value) {
        if (value) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

/**
 * 设置全屏
 */
fun Activity.setFullScreen() {
    if (!isFullScreen) {
        isFullScreen = true
    }
}

/**
 * 设置非全屏
 */
fun Activity.setNonFullScreen() {
    if (isFullScreen) {
        isFullScreen = false
    }
}

/**
 * 屏幕是否亮屏
 */
@SuppressLint("ObsoleteSdkInt")
fun Context.isScreenOn(): Boolean {
    @SuppressLint("ObsoleteSdkInt")
    val powerManager =
        getSystemService(Context.POWER_SERVICE) as PowerManager
    return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
        powerManager.isScreenOn
    } else {
        powerManager.isInteractive
    }

}


/**
 * 屏幕是否锁屏
 */
fun Context.isScreenLocked() {
    (getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager).isKeyguardLocked
}


/**
 * 判断和设置是否保持屏幕常亮，只作用于当前窗口
 */
var Activity.isKeepScreenOn: Boolean
    get() {
        val flag = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        return (window.attributes.flags and flag) == flag
    }
    set(value) {
        when (value) {
            true -> window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            false -> window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

/**
 * 保持屏幕常亮，只作用于当前窗口
 */
fun Activity.setKeepScreenOn() {
    if (!isKeepScreenOn) {
        isKeepScreenOn = true
    }
}

/**
 * 取消保持屏幕常亮，只作用于当前窗口
 */
fun Activity.setNonKeepScreenOn() {
    if (isKeepScreenOn) {
        isKeepScreenOn = false
    }
}

/**
 * 获取自动锁屏时间
 * @throws Settings.SettingNotFoundException
 */
fun Context.getScreenAutoLockTime() = try {
    Settings.System.getInt(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT)
} catch (e: Settings.SettingNotFoundException) {
    e.printStackTrace()
    -1
}

/**
 * 设置自动锁屏时间
 * @return 设置成功返回true
 * 如果设置永不自动锁屏 参数传入Int.MAX_VALUE
 */
@RequiresPermission(android.Manifest.permission.WRITE_SETTINGS)
fun Context.setScreenAutoLockTime(time: Int): Boolean =
    Settings.System.putInt(
        contentResolver,
        Settings.System.SCREEN_OFF_TIMEOUT,
        time
    )
