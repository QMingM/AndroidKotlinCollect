import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.File

/**
 * created by mbm on 2020/5/15
 *
 * 定义Context扩展方法
 * 获取应用版本名称
 * @return 失败返回unKnown
 */
fun Context.getAppVersionName(packageName: String = this.packageName): String {
    return try {
        if (packageName.isBlank()) {
            return "unKnown"
        } else {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            packageInfo?.versionName ?: "unKnown"
        }
    } catch (e: PackageManager.NameNotFoundException) {
        "unKnown"
    }
}

/**
 * 定义Context扩展方法
 * 获取应用版本号
 * @return 失败返回-1
 */
@RequiresApi(Build.VERSION_CODES.P)
fun Context.getAppVersionCode(packageName: String = this.packageName): Int {
    return try {
        if (packageName.isBlank()) {
            -1
        } else {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            packageInfo?.longVersionCode?.toInt() ?: -1
        }
    } catch (e: PackageManager.NameNotFoundException) {
        -1
    }
}

/**
 * 获取应用大小，单位为b，默认为本应用
 * @return 失败时返回-1
 */
fun Context.getAppSize(packageName: String = this.packageName): Long {
    return try {
        if (packageName.isBlank()) {
            -1
        } else {
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            File(applicationInfo.sourceDir).length()
        }
    } catch (e: PackageManager.NameNotFoundException) {
        -1
    }
}

/**
 * 获取应用图标，默认为本应用
 * @return 失败时返回null
 */
fun Context.getAppIcon(packageName: String = this.packageName): Drawable? {
    return try {
        if (packageName.isBlank()) {
            null
        } else {
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            applicationInfo.loadIcon(packageManager)
        }
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }
}
