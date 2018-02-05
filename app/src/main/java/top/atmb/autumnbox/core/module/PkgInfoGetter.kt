package top.atmb.autumnbox.core.module

import android.util.Log
import top.atmb.autumnbox.acp.ACPDataBuilder
import top.atmb.autumnbox.acp.acpcommand.ArgException
import top.atmb.autumnbox.core.pmhelper.*

/**
 * Created by zsh24 on 01/22/2018.
 */
private val TAG = "getPkgInfo"
fun getPkgInfo(args: Array<String>): ACPDataBuilder {
    Log.d(TAG,String.format("have %d args",args.size) )
    if(args.isEmpty())throw ArgException()
    var info = getAppInfo(args[0])
    var result = ACPDataBuilder(0,info.toString().toByteArray())
    return result
}