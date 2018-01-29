package top.atmb.autumnbox

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import top.atmb.autumnbox.acp.ACP
import top.atmb.autumnbox.acpcommand.execute
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.ServerSocket
import java.net.Socket

class ACPService : Service() {
    companion object {
        var isRunning:Boolean
        get() = _isRunning
        private set(value) {_isRunning = value}
        private var _isRunning:Boolean=false
        val TAG = "ACPService"
        val BC_ACP_SERVER_ERROR= "top.atmb.autumnbox.ACP_SERVER_ERROR"
        val BC_ACP_SERVER_STARTED = "top.atmb.autumnbox.ACP_SERVER_STARTED"
        val BC_ACP_SERVER_STOPPED = "top.atmb.autumnbox.ACP_SERVER_STOPPED"
        val BC_COMMAND_RECEIVED = "top.atmb.autumnbox.COMMAND_RECEIVED"
        val BC_COMMAND_PROCESSED = "top.atmb.autumnbox.BC_COMMAND_PROCESSED"
    }
    private lateinit var localBroadcastManager:LocalBroadcastManager
    private lateinit var serverSocket: ServerSocket
    override fun onCreate() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        super.onCreate()
    }
    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serverSocket = ServerSocket(ACP.STD_PORT)
        localBroadcastManager.sendBroadcast(Intent(BC_ACP_SERVER_STARTED))
        Thread({handling()}).start()
        isRunning = true
        return super.onStartCommand(intent, flags, startId)
    }

    fun handling(){
        try{
            while(true){
                var client = serverSocket.accept()
                Log.d(TAG,"a client connected...")
                Thread(AcpServer(client)).start()
            }
        }catch (ex:Exception){
            ex.printStackTrace()
            stopSelf()
            var errorBroadcastIntent = Intent(BC_ACP_SERVER_ERROR)
            errorBroadcastIntent.putExtra("exception",ex)
            localBroadcastManager.sendBroadcast(errorBroadcastIntent)
        }
    }
    fun acpFlow(client:Socket){
        Log.d(TAG,"a client connect")
        var lastCommand = "";
        try{
            var reader = DataInputStream(client.getInputStream())
            var writer = DataOutputStream(client.getOutputStream())

            var requestBuffer = ByteArray(1024)
            var size = reader.read(requestBuffer)
            var commandReceivedIntent = Intent(BC_COMMAND_RECEIVED)
            commandReceivedIntent.putExtra("command",String(requestBuffer,0,size))
            localBroadcastManager.sendBroadcast(commandReceivedIntent)

            val result =  execute(String(requestBuffer,0,size))

            var commandProcessedIntent = Intent(BC_COMMAND_PROCESSED)
            commandProcessedIntent.putExtra("fcode",result.fCode)
            localBroadcastManager.sendBroadcast(commandProcessedIntent)

            writer.write(result!!.toBytes())
            writer.flush()
            writer.close()
            reader.close()
            client.close()
        }catch (ex:Exception){
            try{ client.close()}catch (ex:Exception){}
            ex.printStackTrace()
        }
        Log.d(TAG,"a client disconnect")
    }
    override fun onDestroy() {
        localBroadcastManager.sendBroadcast(Intent(BC_ACP_SERVER_STOPPED))
        serverSocket.close()
        isRunning = false
        super.onDestroy()
    }
}
