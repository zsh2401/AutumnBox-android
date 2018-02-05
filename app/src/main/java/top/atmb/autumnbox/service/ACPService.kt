package top.atmb.autumnbox.service

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import top.atmb.autumnbox.App
import top.atmb.autumnbox.acp.ACP
import java.net.ServerSocket
class ACPService : Service() {
    companion object {
        fun start(force: Boolean = false): Boolean {
            try {
                if (!isRunning || force) {
                    App.context.startService(Intent(App.context, ACPService::class.java))
                }
                return true
            } catch (ex: Exception) {
                ex.printStackTrace()
                return false
            }
        }

        fun stop() {
            try {
                App.context.stopService(Intent(App.context, ACPService::class.java))
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        private fun isServiceRun(mContext: Context, className: String): Boolean {
            var isRun = false
            val activityManager = mContext
                    .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val serviceList = activityManager
                    .getRunningServices(40)
            val size = serviceList.size
            for (i in 0 until size) {
                if (serviceList[i].service.className == className) {
                    isRun = true
                    break
                }
            }
            return isRun
        }

        val isRunning: Boolean
            get() {
                return isServiceRun(App.context, "top.atmb.autumnbox.service.ACPService")
            }
        val TAG = "ACPService"
        val BC_ACP_SERVER_ERROR = "top.atmb.autumnbox.ACP_SERVER_ERROR"
        val BC_ACP_SERVER_STARTED = "top.atmb.autumnbox.ACP_SERVER_STARTED"
        val BC_ACP_SERVER_STOPPED = "top.atmb.autumnbox.ACP_SERVER_STOPPED"
        val BC_COMMAND_RECEIVED = "top.atmb.autumnbox.COMMAND_RECEIVED"
        val BC_COMMAND_PROCESSED = "top.atmb.autumnbox.BC_COMMAND_PROCESSED"
    }

    private lateinit var localBroadcastManager: LocalBroadcastManager
    private lateinit var serverSocket: ServerSocket
    override fun onCreate() {
        Log.i(TAG,"Creating")
        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        serverSocket = ServerSocket(ACP.STD_PORT)
        localBroadcastManager.sendBroadcast(Intent(BC_ACP_SERVER_STARTED))
        super.onCreate()
    }

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }
    var listeningThread:Thread?=null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG,"onStartCommand()")
        if(listeningThread?.isAlive != true){
            listeningThread = Thread({listen()})
            listeningThread!!.start()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun listen() {
        try {
            while (true) {
                var client = serverSocket.accept()
                Log.i(TAG, "a client connected...")
                AcpServer(serverSocket,client).runAsync()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            if(!isDestroying){
                stopSelf()
                var errorBroadcastIntent = Intent(BC_ACP_SERVER_ERROR)
                errorBroadcastIntent.putExtra("exception", ex)
                localBroadcastManager.sendBroadcast(errorBroadcastIntent)
            }
        }
    }
    private var isDestroying = false
    override fun onDestroy() {
        super.onDestroy()
        isDestroying = true
        serverSocket.close()
        localBroadcastManager.sendBroadcast(Intent(BC_ACP_SERVER_STOPPED))
        Log.i(TAG,"Destroy!")
    }
}
