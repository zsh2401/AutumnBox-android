package top.atmb.autumnbox.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import android.util.Log

/**
 * Created by zsh24 on 01/30/2018.
 */
class ACPServiceBroadcastReceiver(context: Context) : BroadcastReceiver(){
    companion object {
        val TAG = "ACPSBReceiver"
        private var intentFilter: IntentFilter = IntentFilter()
        init {
            intentFilter.addAction(ACPService.BC_ACP_SERVER_ERROR)
            intentFilter.addAction(ACPService.BC_COMMAND_PROCESSED)
            intentFilter.addAction(ACPService.BC_COMMAND_RECEIVED)
            intentFilter.addAction(ACPService.BC_ACP_SERVER_STARTED)
            intentFilter.addAction(ACPService.BC_ACP_SERVER_STOPPED)
        }
    }
    private var localBroadcastManager: LocalBroadcastManager = LocalBroadcastManager.getInstance(context)
    var commandReceived:((String)->Unit)? = null
    var commandProcessed:((Int)->Unit)? = null
    var serverCrashed:((Exception)->Unit)?=null
    var serverStarted:(()->Unit)? = null
    var serverStopped:(()->Unit)?=null
    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.d(TAG,"received broadcast->" + p1!!.action)
        when(p1!!.action){
            ACPService.BC_ACP_SERVER_STARTED ->{serverStarted?.invoke() }
            ACPService.BC_COMMAND_RECEIVED ->{
                commandReceived?.invoke(p1!!.getStringExtra("command"))}
            ACPService.BC_COMMAND_PROCESSED ->{
                commandProcessed?.invoke(p1.getIntExtra("fcode",-1))}
            ACPService.BC_ACP_SERVER_ERROR ->{
                serverCrashed?.invoke(p1.extras["exception"] as Exception)
            }
            ACPService.BC_ACP_SERVER_STOPPED ->{
                serverStopped?.invoke()
            }
        }
    }
    fun register(){
        localBroadcastManager.registerReceiver(this,intentFilter)
    }
    fun unregister(){
        localBroadcastManager.unregisterReceiver(this)
    }
}