package top.atmb.autumnbox.core.util

import top.atmb.autumnbox.App

/**
 * Created by zsh24 on 01/26/2018.
 */
fun getVersionCode():Int{
    var pkgName  = App.context.packageName
    var pInfo = App.context.packageManager.getPackageInfo(pkgName,0)
    return pInfo.versionCode
}
fun getVersionName():String{
    var pkgName  = App.context.packageName
    var pInfo = App.context.packageManager.getPackageInfo(pkgName,0)
    return pInfo.versionName
}