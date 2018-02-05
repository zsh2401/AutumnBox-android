package top.atmb.autumnbox.core.module

import top.atmb.autumnbox.acp.ACP
import top.atmb.autumnbox.acp.ACPDataBuilder
import top.atmb.autumnbox.acp.acpcommand.ArgException
import top.atmb.autumnbox.core.pmhelper.getAppIcon

/**
 * Created by zsh24 on 01/23/2018.
 */
@Throws(ArgException::class)
fun getIcon(args:Array<String>):ACPDataBuilder{
    if(args.isEmpty())throw ArgException()
    var builder = ACPDataBuilder()
    builder.data = getAppIcon(args[0])
    builder.fCode = ACP.FCODE_SUCCESS
    return builder
}