package top.atmb.autumnbox.module

import top.atmb.autumnbox.acp.ACPDataBuilder

/**
 * Created by zsh24 on 01/22/2018.
 */
interface IModule {
    fun run(args:Array<String>): ACPDataBuilder
}