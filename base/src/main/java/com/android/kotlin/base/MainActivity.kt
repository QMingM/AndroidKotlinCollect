package com.android.kotlin.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.kotlin.base.test.TestTakePhotoActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testPhotoBtn.setOnClickListener {
            startActivity(Intent(this, TestTakePhotoActivity::class.java))
        }
    }
}

