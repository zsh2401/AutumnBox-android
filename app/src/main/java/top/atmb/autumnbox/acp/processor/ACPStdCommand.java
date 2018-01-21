package top.atmb.autumnbox.acp.processor;

/**
 * Created by zsh24 on 01/21/2018.
 */

public final class ACPStdCommand {
    private String[] args;
    private String baseCommand;
    public String baseCommand(){
        return baseCommand;
    }
    public String[] args(){
        return args;
    }
    public ACPStdCommand(String fullCommand) throws ACPCommandParseException{
        String[] cs = fullCommand.split(" ");
        baseCommand = cs[0];
        args = new String[cs.length - 1];
        if(args.length != 0){
            System.arraycopy(cs,1,args,0,args.length);
        }
    }
}
