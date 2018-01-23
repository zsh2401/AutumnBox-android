package top.atmb.autumnbox.module

import top.atmb.autumnbox.acp.ACP
import top.atmb.autumnbox.acp.ACPDataBuilder
import top.atmb.autumnbox.acpcommand.ArgExcecption
import top.atmb.autumnbox.pmhelper.getAppIcon

/**
 * Created by zsh24 on 01/23/2018.
 */
class IconGetter : IModule {
    override fun run(args: Array<String>): ACPDataBuilder {
        if(args.isEmpty())throw ArgExcecption()
        var builder = ACPDataBuilder()
        builder.data = getAppIcon(args[0])
        builder.fCode = ACP.FCODE_SUCCESS
        return builder
    }
}