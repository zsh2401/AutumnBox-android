package top.atmb.autumnbox.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class CommandStopAcpServiceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        try{
            ACPService.stop()
        }catch (ex:Exception){}
    }
}
