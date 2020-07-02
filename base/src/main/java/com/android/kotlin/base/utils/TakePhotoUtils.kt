package com.android.kotlin.base.utils

import android.app.Activity
import android.content.ContentUris
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
    //请注意，startActivityForResult() 方法受调用 resolveActivity()
    // （返回可处理 Intent 的第一个 Activity 组件）的条件保护。执行此检查非常重要，
    // 因为如果您使用任何应用都无法处理的 Intent 调用 startActivityForResult()，
    // 您的应用就会崩溃。所以只要结果不是 Null，就可以放心使用 Intent。
    Intent("android.media.action.IMAGE_CAPTURE").apply {
        resolveActivity(packageManager)?.let {
            putExtra(MediaStore.EXTRA_OUTPUT, imageUri)//指定图片的输出地址
            startActivityForResult(this, takePhotoRequestCode)
        }
    }
    cb(outputImageFile, imageUri)
}

/**
 * 去相册获取图片扩展函数
 */
fun Activity.gotoAlbum(fromAlbumRequestCode: Int) {
//    //打开文件选择器
//    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//    intent.addCategory(Intent.CATEGORY_OPENABLE)
//    //指定只显示图片
//    intent.type = "image/*"
//    startActivityForResult(intent,fromAlbumRequestCode)
    Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "image/*"
        startActivityForResult(this, fromAlbumRequestCode)
    }
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


/**
 * 适合所有Android系统版本
 * 获取Android手机相册中的图片
 */
fun Activity.getAlbumAllPicture(): List<Uri> {
    val cursor = contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        null, null, null, "${MediaStore.MediaColumns.DATE_ADDED} desc"
    )
    var list = mutableListOf<Uri>()
    if (cursor != null) {
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
            val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            println("image uri is $uri")
            list.add(uri)
        }
        cursor.close()
    }
    return list
}
