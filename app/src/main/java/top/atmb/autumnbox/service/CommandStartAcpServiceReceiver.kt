package top.atmb.autumnbox.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class CommandStartAcpServiceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        ACPService.start()
        try{
            abortBroadcast()
        }catch (ex:Exception){}
    }
}
