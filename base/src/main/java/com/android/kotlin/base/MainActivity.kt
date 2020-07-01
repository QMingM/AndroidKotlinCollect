package com.android.kotlin.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.kotlin.base.test.TestTakePhotoActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        takePhotoBtn.setOnClickListener {
            startActivity(Intent(this, TestTakePhotoActivity::class.java))
        }
    }
}

//    private fun gotoTakePhoto() {
//        //创建File对象，用于存储拍下的图片，存放在应用关联缓存目录getExternalCacheDir()
//        outputImage = File(externalCacheDir, "output.image.jpg")
//        if (outputImage.exists()) {
//            outputImage.delete()
//        }
//        outputImage.createNewFile()
//        //Android 7.0之前直接使用本地真实路径的Uri被认为是不安全的
//        // 7.0之后系统会认为不安全， 所以使用FileProvider封装的方式获取
//        //FileProvider.getUriForFile()，接受三个参数，第二个参数可以为任意字符串
//        imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            FileProvider.getUriForFile(
//                this,
//                "com.example.cameraalbumtest.fileprovider",
//                outputImage
//            )
//        } else {
//            Uri.fromFile(outputImage)
//        }
//        val intent = Intent("android.media.action.IMAGE_CAPTURE")
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)//指定图片的输出地址
//        startActivityForResult(intent, takePhoto)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            takePhoto -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    val bitmap =
//                        BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
//                    imageView.setImageBitmap(rotateIfRequired(bitmap))
//                }
//            }
//        }
//    }
//
//    private fun rotateIfRequired(bitmap: Bitmap): Bitmap {
//        val exif = ExifInterface(outputImage.path)
//        val orientation =
//            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
//        return when (orientation) {
//            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90)
//            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180)
//            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270)
//            else -> bitmap
//        }
//    }
//
//    private fun rotateBitmap(bitmap: Bitmap, degree: Int): Bitmap {
//        val matrix = Matrix()
//        matrix.postRotate(degree.toFloat())
//        val rotatedBitmap =
//            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
//        bitmap.recycle()
//        return rotatedBitmap
//    }
//}
