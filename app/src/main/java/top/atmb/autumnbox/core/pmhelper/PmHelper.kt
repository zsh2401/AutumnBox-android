package top.atmb.autumnbox.core.pmhelper

import android.content.pm.IPackageStatsObserver
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.UserHandle
import org.json.JSONObject
import top.atmb.autumnbox.App
import java.io.ByteArrayOutputStream
import java.lang.reflect.Method

/**
 * Created by zsh24 on 01/22/2018.
 */
private var Pm:PackageManager
get() = App.context.packageManager
set(value) {}

private var TAG:String = "pmHelper";

fun userId(): Int {
    try {
        val myUserId = UserHandle::class.java.getDeclaredMethod("myUserId")//ignore check this when u set ur min SDK < 17
        return myUserId.invoke(Pm) as Int
    } catch (ex: Exception) {
        return 0
    }
}
@Throws(PackageManager.NameNotFoundException::class)
fun getAppInfo(packageName: String):JSONObject?{
    val pi = Pm!!.getPackageInfo(packageName, 0)
    val jsonObject = JSONObject()
    jsonObject.put("packageName", pi.packageName)
    jsonObject.put("name", pi.applicationInfo.loadLabel(Pm).toString())
    val (codeSize, cacheSize, dataSize) = getAppUseSpace(packageName)
    jsonObject.put("cacheSize", cacheSize)
    jsonObject.put("codeSize", codeSize)
    jsonObject.put("dataSize", dataSize)
    return jsonObject
}

@Throws(PackageManager.NameNotFoundException::class)
fun getAppIcon(packageName: String): ByteArray {

    val pi = Pm.getPackageInfo(packageName, 0)
    val icon = pi.applicationInfo.loadIcon(Pm)
    val bmp = (icon as BitmapDrawable).bitmap
    val stream = ByteArrayOutputStream()
    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}

private var getPackageSizeMethod: Method? = null
private fun init_getPackageSizeMethod() {
    if (getPackageSizeMethod != null) return
    if (Build.VERSION.SDK_INT <= 16 || Build.VERSION.SDK_INT >= 26) return
    try {
        getPackageSizeMethod = Pm!!.javaClass.getDeclaredMethod(
                "getPackageSizeInfo", String::class.java, Int::class.javaPrimitiveType, IPackageStatsObserver::class.java)
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}



fun getAppUseSpace(pkgName: String): PkgState {
    val result = PkgState()
    try {
        init_getPackageSizeMethod()
        //调用方法，待调用流程完成后会回调PkgSizeObserver类的方法
        getPackageSizeMethod!!.invoke(Pm, pkgName, userId(), PackageSizeGettedHandler(result))
        while (!result.isSeted);
    } catch (ex: Exception) {
        ex.printStackTrace()
    }

    return result
}