package top.atmb.autumnbox.module

import android.content.pm.ApplicationInfo
import org.json.JSONArray
import org.json.JSONObject
import top.atmb.autumnbox.App
import top.atmb.autumnbox.acp.ACP
import top.atmb.autumnbox.acp.ACPDataBuilder

/**
 * Created by zsh24 on 01/27/2018.
 */
fun test(args:Array<String>):ACPDataBuilder{
    return ACPDataBuilder(ACP.FCODE_SUCCESS)
}
//get all apps
fun getPkgs(args:Array<String>):ACPDataBuilder{
    var pm = App.context.packageManager
    var apps = pm.getInstalledApplications(0)
    var jPkgs = JSONArray()
    apps.forEach { appInfo->
        if((appInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 1){
            //System App
            jPkgs.put(JSONArray().put(appInfo.packageName).put(appInfo.loadLabel(pm)).put(0))
        }else{
            //User App
            jPkgs.put(JSONArray().put(appInfo.packageName).put(appInfo.loadLabel(pm)).put(1))
        }
    }
    var resultJson = JSONObject()
    resultJson.put("pkgs",jPkgs)
    var result = ACPDataBuilder()
    result.fCode = ACP.FCODE_SUCCESS
    result.data = resultJson.toString().toByteArray()
    return result
}