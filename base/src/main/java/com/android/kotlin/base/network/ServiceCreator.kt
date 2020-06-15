package com.android.kotlin.base.network

import android.util.Log
import com.android.kotlin.base.BaseApplication
import com.android.kotlin.base.BuildConfig
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * created by mbm on 2020/5/14
 * 创建retrofit实例，并返回接口动态代理对象
 */
object ServiceCreator {
    private const val BASE_URL = "https://api.caiyunapp.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(getClient())
        .build()

    /**
     * 返回接口动态代理对象
     */
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    /**
     * 定义泛型实化功能
     */
    inline fun <reified T> create(): T = create(T::class.java)


    /**
     * 获取OkHttpClient实例
     */
    private fun getClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()//添加一个log拦截器，打印所有的log
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY//log日志输出级别
            okHttpClient.addInterceptor(httpLoggingInterceptor)
        }
        val cacheFile = File(BaseApplication.context.cacheDir, "cache")//设置请求缓存的位置
        val cache = Cache(cacheFile, 1024 * 1024 * 50)//50M缓存大小
        okHttpClient.addInterceptor(addHeadInterceptor())
        okHttpClient.addInterceptor(addCommonParameterInterceptor())
        okHttpClient.cache(cache)//添加缓存
        okHttpClient.connectTimeout(60L, TimeUnit.SECONDS)
        okHttpClient.readTimeout(60L, TimeUnit.SECONDS)
        okHttpClient.readTimeout(60L, TimeUnit.SECONDS)
        return okHttpClient.build()
    }

    /**
     * 设置公共参数
     */
    private fun addCommonParameterInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val request: Request
            val modifiedUrl = originalRequest.url().newBuilder()
                .addQueryParameter("token", BaseApplication.TOKEN)
                .addQueryParameter("lang", "zh_CN")
                .build()
            request = originalRequest.newBuilder().url(modifiedUrl).build()
            chain.proceed(request)
        }
    }

    /**
     * 设置请求头
     */
    private fun addHeadInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request().newBuilder()
//                .addHeader("User-Agent", generalUserAgent())
//                .addHeader("rst", getRst())
                .method(chain.request().method(), chain.request().body())
                .build()
            val headers = request.headers()
            Log.d("OkHttp", "--> 请求头  ${headers.toString()}")
            chain.proceed(request)
        }
    }

}