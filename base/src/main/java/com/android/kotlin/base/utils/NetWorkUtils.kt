package com.android.kotlin.base.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.android.kotlin.base.BaseApplication

/**
 * created by mbm on 2020/6/16
 */

/**没有网络**/
const val NETWORK_NONE = -1
/**网络连接**/
//const val NETWORK_CONNECTED = 0
/**移动网络**/
const val NETWORK_MOBILE = 1

/**无线网络**/
const val NETWORK_WIFI = 2

/**未知网络**/
const val NETWORK_UNKNOWN = -2

/**
 * 获取当前的网络状态
 */
val networkState: Int
    @RequiresApi(Build.VERSION_CODES.M)
    get() {
        //得到连接管理器对象
        val cm =
            BaseApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork
        val capabilities = cm.getNetworkCapabilities(network)
        //如果网络连接，判断该网络类型
        return if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {//wifi
                    NETWORK_WIFI
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {//蜂窝
                    NETWORK_MOBILE
                }
                else -> {
                    NETWORK_UNKNOWN
                }
            }
        } else {
            NETWORK_NONE
        }

    }

/**是否是wifi**/
val isWifi
    get() = networkState == NETWORK_WIFI

/**是否是移动网络**/
val isMobileNet
    get() = networkState == NETWORK_MOBILE

/**网络是否连接**/
val isNetworkConnect
    get() = when (networkState) {
        NETWORK_MOBILE, NETWORK_WIFI -> true
        NETWORK_NONE -> false
        else -> {
            false
        }
    }