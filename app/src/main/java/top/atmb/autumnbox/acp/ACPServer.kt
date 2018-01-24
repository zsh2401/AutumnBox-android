package top.atmb.autumnbox.acp;

import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.ServerSocket
import java.net.Socket
class ACPServer: Thread() {
    companion object {
        var TAG = "ACPServer"
    }
    var serverStarted:(()->Unit)? = null
    var serverFailed:((Exception)->Unit)?= null
    var receivedEventHandler:((String)->ACPDataBuilder)? = null
    override fun run() {
        try{
            var serverSocket = ServerSocket(ACP.STD_PORT)
            ACP.printLog(TAG,"server listening")
            serverStarted?.invoke()
            while(true){
                var client = serverSocket.accept()
                Thread({acpFlow(client)}).start()
            }
        }catch (ex:Exception){
            ex.printStackTrace()
           serverFailed?.invoke(ex)
        }
    }
    private fun acpFlow(client:Socket){
        ACP.printLog(TAG,"a client connected")

        try{
            var reader = DataInputStream(client.getInputStream())
            var writer = DataOutputStream(client.getOutputStream())

            var requestBuffer = ByteArray(1024)
            var size = reader.read(requestBuffer)
            //终极抄袭C#?????
            val result =  receivedEventHandler?.invoke(String(requestBuffer,0,size))
            writer.write(result!!.toBytes())

            writer.flush()
            writer.close()
            reader.close()
            client.close()
        }catch (ex:Exception){
            ex.printStackTrace()
            try{
                client.close()
            }catch (ex:Exception){}
        }
        ACP.printLog(TAG,"a client disconnected")
    }
}
