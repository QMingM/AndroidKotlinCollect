
import android.os.Build
import com.android.kotlin.base.BaseApplication

/**
 * created by mbm on 2020/5/15
 */
object AppUtils {

    /**
     * 获取当前版本号versionCode
     */
    fun getVersionCode(): Int {
        val packageManager = BaseApplication.context.packageManager
        val packageInfo = packageManager.getPackageInfo(BaseApplication.context.packageName, 0)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode.toInt()
        } else {
            packageInfo.versionCode //API 28已废弃，改用longVersionCode
        }
    }

    /**
     * 获取当前版本name
     */
    fun getVersionName(): String {
        val packageManager = BaseApplication.context.packageManager
        val packageInfo = packageManager.getPackageInfo(BaseApplication.context.packageName, 0)
        return packageInfo.versionName
    }
}