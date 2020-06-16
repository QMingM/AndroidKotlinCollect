package com.android.kotlin.base.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.android.kotlin.base.BaseApplication

/**
 * created by mbm on 2020/6/16
 * 复制纯文本
 * @param text: 需要复制的文本
 * @param label: 用户可见的对复制数据的描述
 * @return 是否复制成功
 */
@JvmOverloads
fun clipPlainText(text: CharSequence, label: CharSequence = ""): Boolean {
    val cm = BaseApplication.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText(label, text)
    cm.setPrimaryClip(clipData)
    return cm.hasPrimaryClip()
}