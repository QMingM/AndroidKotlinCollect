package com.android.kotlin.base.utils

import android.content.Context
import android.os.Environment
import android.os.StatFs

/**
 * created by mbm on 2020/6/16
 * SDCard工具类
 */

/**
 * SD卡是否已经挂载
 */
val isSDCardMounted
    get() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED


/**
 * 获取sd卡路径
 * getExternalStorageDirectory()方法在api29被废弃
 * 改用Context#getExternalFilesDir()
 */
fun Context.sdCardPath(): String {
    return if (isSDCardMounted) {
        getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath.toString()
    } else {
        ""
    }
}

/**
 * 获取SD卡的总大小
 * 失败时返回-1
 */
fun Context.sdCardTotalSize(): Long {
    return if (isSDCardMounted) {
        val staFs = StatFs(getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.path)
        staFs.blockSizeLong * staFs.blockCountLong
    } else {
        -1
    }

}

/**
 * 获取SD卡可用空间大小
 * 失败时返回-1
 */
fun Context.sdCardAvailableSize(): Long {
    return if (isSDCardMounted) {
        val staFs = StatFs(getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.path)
        staFs.blockSizeLong * staFs.availableBlocksLong
    } else {
        -1
    }
}
