package top.atmb.autumnbox.acpcommand.modules

import android.util.Log
import top.atmb.autumnbox.acp.ACPDataBuilder
import top.atmb.autumnbox.acpcommand.IModule
import top.atmb.autumnbox.pmhelper.*

/**
 * Created by zsh24 on 01/22/2018.
 */
class PkgInfoGetter :IModule{
    companion object {
        val TAG = "PkgInfoGetter"
    }
    override fun run(args: Array<String>): ACPDataBuilder? {
        Log.d(TAG,String.format("have %d args",args.size) )
        if(args.isEmpty())return null
        var info = getAppInfo(args[0])
        var result = ACPDataBuilder(0,info.toString().toByteArray())
        return result
    }
}