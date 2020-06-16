package com.android.kotlin.base.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * created by mbm on 2020/6/16
 * 软键盘工具类
 */

/**
 * 打开软键盘
 */
fun View.showKeyboard(): Boolean {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    requestFocus()
    return imm.showSoftInput(this, InputMethodManager.RESULT_UNCHANGED_SHOWN)
}

/**
 * 关闭软键盘
 */
fun View.hideKeyboard(): Boolean {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    return imm.hideSoftInputFromWindow(this.windowToken, InputMethodManager.RESULT_UNCHANGED_HIDDEN)
}

/**
 * 根据当前软件按盘得状态做取反操作
 */
fun View.toggleKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.RESULT_UNCHANGED_SHOWN)
}