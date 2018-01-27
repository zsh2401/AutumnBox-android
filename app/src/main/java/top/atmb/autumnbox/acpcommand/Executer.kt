package top.atmb.autumnbox.acpcommand

import android.util.Log
import top.atmb.autumnbox.acp.ACP
import top.atmb.autumnbox.acp.ACPDataBuilder
import top.atmb.autumnbox.module.*

/**
 * Created by zsh24 on 01/22/2018.
 */

private  val TAG = "execter"
fun execute(command:String):ACPDataBuilder{
    var result = ACPDataBuilder()
    try{
        val cmd = command.toCommand()
        val module = find(cmd.baseCommand)
        var exeResult = module(cmd.args)
        if(exeResult != null){
            result = exeResult
        }
    }catch (ex:CommandParseFailedException){
        result.fCode =  ACP.FCODE_UNKNOWN_COMMAND
    }catch (ex:CommandNotFoundException){
        result.fCode = (ACP.FCODE_UNKNOWN_COMMAND)
    }catch (ex:NullPointerException){
        result.fCode = (ACP.FCODE_UNKNOWN_COMMAND)
    }catch (ex:Exception){
        result.fCode = (ACP.FCODE_ERR_WITH_EX)
        Log.d(TAG,"execute failed..")
        ex.printStackTrace()
        val message = ex.toString() + ex.message
        result.data = message.toByteArray()
    }
    return result
}

@Throws(CommandNotFoundException::class)
fun find(baseCommand:String):(Array<String>)->ACPDataBuilder{
    return when(baseCommand){
        ACP.CMD_GETICON-> {args-> getIcon(args)}
        ACP.CMD_GETPKGINFO->{args-> getPkgInfo(args)}
        ACP.CMD_GETPKGS->{ args->getPkgs(args)}
        ACP.CMD_TEST->{args-> test(args) }
        else -> throw CommandNotFoundException()
    }
}
