package com.android.kotlin.base.utils

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File

/**
 * created by mbm on 2020/7/1
 * 拍照工具类
 */

typealias callback = (File, Uri) -> Unit

/**
 * 定义去拍照扩展函数
 */
fun Activity.gotoTakePhoto(takePhotoRequestCode: Int, cb: callback) {
    //创建File对象，用于存储拍下的图片，存放在应用关联缓存目录getExternalCacheDir()
    val outputImageFile = File(externalCacheDir, "output_image.jpg")
    if (outputImageFile.exists()) {
        outputImageFile.delete()
    }
    outputImageFile.createNewFile()
    //Android 7.0之前直接使用本地真实路径的Uri被认为是不安全的
    // 7.0之后系统会认为不安全， 所以使用FileProvider封装的方式获取
    //FileProvider.getUriForFile()，接受三个参数，第二个参数可以为任意字符串
    val imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(
            this,
            "com.example.android.fileprovider",
            outputImageFile
        )
    } else {
        Uri.fromFile(outputImageFile)
    }
    val intent = Intent("android.media.action.IMAGE_CAPTURE")
    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)//指定图片的输出地址
    startActivityForResult(intent, takePhotoRequestCode)
    cb(outputImageFile, imageUri)
}

object TakePhotoUtils {

    fun rotateIfRequired(bitmap: Bitmap, outputImageFile: File): Bitmap {
        val exif = ExifInterface(outputImageFile.path)
        val orientation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270)
            else -> bitmap
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedBitmap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        bitmap.recycle()
        return rotatedBitmap
    }
}
