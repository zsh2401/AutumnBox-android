package top.atmb.autumnbox.core.util

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.widget.Toast
import top.atmb.autumnbox.App
import top.atmb.autumnbox.R

/**
 * Created by zsh24 on 01/26/2018.
 */
private val alipayClientPkgName = "com.eg.android.AlipayGphone"
private val URL_FORMAT= "intent://platformapi/startapp?saId=10000007&" +
        "clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2FFKX06104YFIEAHNXXKP7E2%3F_s" +
        "%3Dweb-other&_t=1472443966571#Intent;" +
        "scheme=alipayqr;package=com.eg.android.AlipayGphone;end"
fun gotoAlipay(){
    if(isInstallAlipayClient()){
        App.context.startActivity(Intent.parseUri(URL_FORMAT,Intent.URI_INTENT_SCHEME))
    }else{
        Toast.makeText(App.context,App.context.resources.getString(R.string.msg_have_no_installl_alipay),Toast.LENGTH_LONG).show()
    }
}
private fun isInstallAlipayClient() :Boolean{
    try{
        return App.context.packageManager.getPackageInfo(alipayClientPkgName,0) != null
    }catch (ex:PackageManager.NameNotFoundException){
        return false
    }
}
fun openAlipay(){
    openAppByPkgName(alipayClientPkgName)
}
fun openAppByPkgName(pkgName:String){
    try{
        var pi = App.context.packageManager.getPackageInfo(pkgName,0)
        var resolveIntent = Intent(Intent.ACTION_MAIN,null)
        resolveIntent.setPackage(pi.packageName)
        var apps:List<ResolveInfo> = App.context.packageManager.queryIntentActivities(resolveIntent,0)
        var info = apps[1]
        if(info != null){
            var _pkgName = info.activityInfo.packageName
            var className = info.activityInfo.name
            var intent = Intent(Intent.ACTION_MAIN)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
            var cn = ComponentName(_pkgName,className)
            intent.setComponent(cn)
            App.context.startActivity(intent)
        }

    }catch (ex:Exception){ex.printStackTrace()}
}