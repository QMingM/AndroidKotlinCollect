package com.android.kotlin.base

import android.app.Application
import android.content.Context

/**
 * created by mbm on 2020/5/14
 */
class BaseApplication : Application() {

    companion object {
        lateinit var context: Context
        const val TOKEN = "RDAELzUbSWleFmn4"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}