package com.android.kotlin.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.kotlin.base.utils.sdCardPath
import com.android.kotlin.base.utils.statusbar.statusBarHeight

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       Log.e("mk", sdCardPath())
    }
}
