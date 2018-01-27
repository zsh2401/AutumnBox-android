package top.atmb.autumnbox.module

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
fun getPkgs(args:Array<String>):ACPDataBuilder{
    var pkgs = App.context.packageManager.getInstalledPackages(0)
    var jPkgs = JSONArray()
    pkgs.forEach { pInfo->
        if(pInfo.applicationInfo.uid>10000)
            jPkgs.put(JSONArray().put(pInfo.packageName).put(pInfo.applicationInfo.loadLabel(App.context.packageManager)))
    }
    var resultJson = JSONObject();
    resultJson.put("pkgs",jPkgs)
    var result = ACPDataBuilder();
    result.fCode = ACP.FCODE_SUCCESS;
    result.data = resultJson.toString().toByteArray()
    return result;
}