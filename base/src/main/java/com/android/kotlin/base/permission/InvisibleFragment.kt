package com.android.kotlin.base.permission

import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

/**
 * created by mbm on 2020/7/1
 */

typealias PermissionCallback = (Boolean, List<String>) -> Unit

class InvisibleFragment : Fragment() {
    private var callback: PermissionCallback? = null

    fun requestNow(cb: PermissionCallback, vararg permission: String) {
        callback = cb
        requestPermissions(permission, 1)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1){
            //定义一个被所有被用户拒绝的权限
            val deniedList = ArrayList<String>()
            for ((index,result) in grantResults.withIndex()){
                if (result != PackageManager.PERMISSION_GRANTED){
                    deniedList.add(permissions[index])
                }
            }
            //定义一个变量，标识所有权限是否均已被授权
            val allGranted = deniedList.isEmpty()
            callback?.let { it(allGranted,deniedList) }
        }
    }
}