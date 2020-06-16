package com.android.kotlin.base.utils

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import com.android.kotlin.base.BaseApplication

/**
 * created by mbm on 2020/6/16
 * 虚拟导航工具类
 */

val hasNavBar
    @JvmName("hasNavBar")
    get() = navBarResId != 0

private val navBarResId
    get() = BaseApplication.context.resources.getIdentifier(
        "navigation_bar_height",
        "dimen", "android"
    )


/**
 * 获取虚拟导航栏的高度，必须在布局绘制完成之后调用才能获取到正确的值（可以在onWindowFocusChanged()中调用）
 * 单位为px
 */
//val navBarHeight: Int
//    get() {
//        val resourceId = navBarResId
//        return if (resourceId != 0) {
//            getResDimenPx(resourceId)
//        } else 0
//    }

/**
 * 设置导航栏颜色
 */
var Window.navBarColor: Int
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    get() = navigationBarColor
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    set(@ColorInt value) {
        navigationBarColor = value
    }

/**
 * 设置导航栏颜色
 */
var Activity.navBarColor: Int
    get() = window.navBarColor
    set(@ColorInt value) {
        window.navBarColor = value
    }

///**
// * 设置导航栏颜色
// */
//fun Window.setNavBarColorRes(@ColorRes colorId: Int) {
//    navBarColor = getResColor(colorId)
//}
//
///**
// * 设置导航栏颜色
// */
//fun Activity.setNavBarColorRes(@ColorRes colorId: Int) {
//    navBarColor = getResColor(colorId)
//}

private const val RES_NAME_NAV_BAR = "navigationBarBackground"


/**
 * 当前虚拟导航栏是否显示
 */
val Activity.isNavBarShowed: Boolean
    get() = this.window.isNavBarShowed

/**
 * 当前虚拟导航栏是否显示
 */
val Window.isNavBarShowed: Boolean
    get() {
        val viewGroup = decorView as ViewGroup? ?: return false
        return (0 until viewGroup.childCount).firstOrNull {
            viewGroup.getChildAt(it).id != View.NO_ID
                    && BaseApplication.context.resources.getResourceEntryName(viewGroup.getChildAt(it).id) == RES_NAME_NAV_BAR
        } != null
    }

/**
 * 当前虚拟导航栏是否隐藏
 */
val Window.isNavBarHidden: Boolean
    get() = !isNavBarShowed

/**
 * 当前虚拟导航栏是否隐藏
 */
val Activity.isNavBarHidden: Boolean
    get() = window.isNavBarHidden