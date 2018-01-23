package top.atmb.autumnbox.acpcommand

/**
 * Created by zsh24 on 01/22/2018.
 */
data class Command(var baseCommand:String,var args:Array<String> = arrayOf())

@Throws(CommandParseFailedException::class)
fun String.toCommand():Command{
    var strs:Array<String> = this.split(" ").toTypedArray();
    if(strs.isEmpty()){
        throw CommandParseFailedException()
    }
    var args = arrayOf<String>()
    if(strs.size > 1){
        args = strs.copyOfRange(1,strs.size)
    }
    return Command(strs[0],args)
}

