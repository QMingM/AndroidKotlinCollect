package com.android.kotlin.base.network

import android.util.Log
import androidx.lifecycle.liveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * created by mbm on 2020/5/14
 * 网络请求过程处理
 */

/**
 * 定义await的扩展方法
 * 通过协程来处理网络请求
 */
suspend fun <T> Call<T>.await(): T {
    return suspendCoroutine { continuation ->
        enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                continuation.resumeWithException(t)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                Log.d("response", body.toString())
                if (body != null) continuation.resume(body)
                else continuation.resumeWithException(RuntimeException("response body is null"))
            }
        })
    }
}

/**
 * 定义一个高阶函数(挂起函数)
 * 抽离try/catch代码
 */
fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
    liveData<Result<T>>(context) {
        val result = try {
            block()
        } catch (e: Exception) {
            Result.failure<T>(e)
        }
        emit(result)
    }
