package com.android.kotlin.base.test

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.kotlin.base.R
import com.android.kotlin.base.permission.PermissionM
import com.android.kotlin.base.utils.TakePhotoUtils.rotateIfRequired
import com.android.kotlin.base.utils.gotoTakePhoto
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class TestTakePhotoActivity : AppCompatActivity() {

    private val takePhotoRequestCode = 1
    private lateinit var imageUri: Uri
    private lateinit var outputImageFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        takePhotoBtn.setOnClickListener {
            PermissionM.request(
                this,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) { allGranted, deniedList ->
                if (allGranted) {
                    gotoTakePhoto(takePhotoRequestCode) { outputImageFile, imageUri ->
                        this.outputImageFile = outputImageFile
                        this.imageUri = imageUri
                    }
                } else {
                    Toast.makeText(this, "you denied $deniedList", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            takePhotoRequestCode -> {
                if (resultCode == Activity.RESULT_OK) {
                    val bitmap =
                        BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
                    imageView.setImageBitmap(rotateIfRequired(bitmap, outputImageFile))
                }
            }
        }
    }


}
