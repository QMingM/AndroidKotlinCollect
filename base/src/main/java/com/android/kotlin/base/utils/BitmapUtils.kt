package com.android.kotlin.base.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

/**
 * created by mbm on 2020/6/16

 * 将View转换为Bitmap
 * @param  scale: 生成的Bitmap相对于原View的大小比例，范围为0~1.0
 */
@InternalCoroutinesApi
fun viewToBitmap(view: View, scale: Float = 1.0F): Bitmap? = when (view) {
    is ImageView -> {
        val drawable: Drawable = view.drawable
        (drawable as BitmapDrawable).bitmap
    }
    else -> {
        view.clearFocus()
        val bitmap =
            createBitmapSafely((view.width * scale).toInt(), (view.height * scale).toInt())
        if (bitmap != null) {
            val canvas = Canvas()
            synchronized(canvas) {
                with(canvas) {
                    setBitmap(bitmap)
                    save()
                    drawColor(Color.WHITE)
                    scale(scale, scale)
                    view.draw(canvas)
                    restore()
                    setBitmap(null)
                }
            }
        }
        bitmap
    }
}

/**
 * 安全创建Bitmap，如果产生了OOM，可以主动GC后再尝试。
 * https://github.com/QMUI/QMUI_Android
 * @param width : Bitmap宽度
 * @param height : Bitmap高度度
 * @param retryCount : 重试次数，默认一次
 * @param config : Bitmap.Config，默认为ARGB_8888
 * @return 创建Bitmap成功返回Bitmap，否则返回null。
 */
fun createBitmapSafely(
    width: Int,
    height: Int,
    retryCount: Int = 1,
    config: Bitmap.Config = Bitmap.Config.ARGB_8888
): Bitmap? = try {
    Bitmap.createBitmap(width, height, config)
} catch (e: OutOfMemoryError) {
    e.printStackTrace()
    when (retryCount > 0) {
        true -> {
            System.gc()
            createBitmapSafely(width, height, retryCount, config)
        }
        false -> null
    }
}

/**
 * 把Uri对象解析成bitmap
 */
fun uriToBitmap(context: Context, uri: Uri): Bitmap {
    return context.contentResolver.openFileDescriptor(uri, "r").use {
        BitmapFactory.decodeFileDescriptor(it?.fileDescriptor)
    }
}