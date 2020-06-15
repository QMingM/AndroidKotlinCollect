package com.android.kotlin.base.network

/**
 * created by mbm on 2020/5/14
 */
data class BaseResponse<T : Any?>(
    val code: Int,
    val msg: String,
    val data: T?
)