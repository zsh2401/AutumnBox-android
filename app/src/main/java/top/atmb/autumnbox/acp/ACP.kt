package top.atmb.autumnbox.acp;

import android.util.Log

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
class ACP{
    companion object {
        val STD_PORT  = 24020
        val VERSION = 0.8
        val CMD_GETICON = "geticon"
        val CMD_GETPKGINFO = "getpkginfo"
        val CMD_TEST = "test"
        val FCODE_SUCCESS:Byte = 0
        val FCODE_UNKNOW_ERR:Byte = 1
        val FCODE_ERR_WITH_EX:Byte =2
        val FCODE_PKG_NOT_FOUND:Byte = 3
        val FCODE_UNKNOWN_COMMAND:Byte = 4
        fun printLog(tag:String,message:String){
            Log.d(tag,message)
        }
    }
}