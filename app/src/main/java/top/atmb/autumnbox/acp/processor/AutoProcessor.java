package top.atmb.autumnbox.acp.processor;

import android.content.pm.PackageManager;

import top.atmb.autumnbox.acp.ACP;
import top.atmb.autumnbox.pmhelper.*;

/**
 * Created by zsh24 on 01/21/2018.
 */

public class AutoProcessor{
    public static ACPDataBuilder process(String _command){
        ACPDataBuilder builder = new ACPDataBuilder();
        try{
            ACPStdCommand command = new ACPStdCommand(_command);
            switch (command.baseCommand().toLowerCase()){
                case ACP.CMD_GETPKGINFO:
                    try{
                        String json =  PmHelperKt.getAppInfo(command.args()[0]).toString();
                        builder.setfCode(ACP.FCODE_SUCCESS);
                        builder.setData(json.getBytes());
                    }catch (PackageManager.NameNotFoundException ex){
                        builder.setfCode(ACP.FCODE_PKG_NOT_FOUND);
                    }catch (Exception ex){
                        builder.setfCode(ACP.FCODE_UNKNOW_ERR);
                    }
                    break;
                case ACP.CMD_TEST:
                    builder.setfCode(ACP.FCODE_SUCCESS);
                    break;
                case ACP.CMD_GETICON:
                    try{
                        byte[] data = PmHelperKt.getAppIcon(command.args()[0]);
                        builder.setData(data);
                        builder.setfCode(ACP.FCODE_SUCCESS);
                    }catch (PackageManager.NameNotFoundException ex){
                        builder.setfCode(ACP.FCODE_PKG_NOT_FOUND);
                    }catch (Exception e){
                        builder.setfCode(ACP.FCODE_UNKNOW_ERR);
                    }
                    break;
                default:
                    builder.setfCode(ACP.FCODE_UNKNOW_COMMAND);
                    break;
            }
        }catch (ACPCommandParseException ex){
            builder.setfCode(ACP.FCODE_UNKNOW_COMMAND);
        }
        return builder;
    }
}
