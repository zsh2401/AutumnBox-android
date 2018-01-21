package top.atmb.autumnbox.acp;
/*
ACP V0.8
ACP标准流程
ACP仅支持PC端向安卓端的主动请求,不支持安卓端向PC端的请求

安卓服务端启动
PC端连接
PC端发送请求 (geticon com.miui.fm)
安卓端发送处理结果与数据
结束连接

PC端请求数据结构:
UTF8 string 的请求命令
标准格式: command [arg1] [arg2] ...
例如: geticon com.miui.fm

安卓端返回数据结构:
byte[dataSize + 1] 返回一个大小为具体内容大小+1的数组
byte[0] 0 | FirstCode 用于表示执行结果
byte[1-?] 具体的数据

*/

/*ACP标准*/
public final class ACP{
    public static final int STD_PORT = 24020;
    public static final double VERSION = 0.8;
    //Commands
    public static final String CMD_GETICON = "geticon";
    public static final String CMD_GETPKGINFO = "getpkginfo";
    public static final String CMD_TEST = "test";
    //Error Codes
    public static final byte FCODE_SUCCESS = 0;
    public static final byte FCODE_UNKNOW_ERR = 1;
    public static final byte FCODE_ERR_WITH_EX= 2;
    public static final byte FCODE_PKG_NOT_FOUND = 3;
    public static final byte FCODE_UNKNOW_COMMAND = 4;
    /*print log*/
    public static void printLog(String TAG,String str){
        System.out.println(TAG + ":  " + str);
    }
}