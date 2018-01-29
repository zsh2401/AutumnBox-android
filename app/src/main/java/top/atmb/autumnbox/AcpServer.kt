package top.atmb.autumnbox

import android.util.Log
import top.atmb.autumnbox.acp.ACP
import top.atmb.autumnbox.acpcommand.execute
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

/**
 * Created by zsh24 on 01/29/2018.
 */
class AcpServer(private var client: Socket):Runnable{
    private var reader = DataInputStream(client.getInputStream())
    private var writer = DataOutputStream(client.getOutputStream())
    companion object {
        val TAG = "AcpServer"
    }
    override fun run() {
        try{
            var buffer = ByteArray(4096)
            while(true){
                Log.d(TAG,"listening command")
                var receiveSize = reader.read(buffer)
                var command = String(buffer,0,receiveSize)
                Log.d(TAG,"received command: " + command)
                if(command == ACP.CMD_EXIT)break
                Log.d(TAG,"executing command..")
                var exeResult = execute(command)
                Log.d(TAG,String.format("executed..fCode: %d dataSize: %d",exeResult.fCode,exeResult.dataSize))
                Log.d(TAG,"writing data to stream....")
                writer.write(exeResult.toBytes())
                writer.flush()
                Log.d(TAG,"writed")
            }
            reader.close()
            writer.close()
            client.close()
        }catch (ex:Exception){
            ex.printStackTrace()
        }
        Log.d(TAG,"client disconnected")
    }
}