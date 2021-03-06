package top.atmb.autumnbox

import android.app.Application
import android.content.Context
import android.support.v4.content.LocalBroadcastManager

/**
 * Created by zsh24 on 01/25/2018.
 */
class App: Application() {
    companion object {
        val localBroadcastManager:LocalBroadcastManager
        get() = _localBroadcastManager
        private lateinit var _localBroadcastManager:LocalBroadcastManager

        val context: Context
        get() = _context
        private lateinit var _context:Context

        val current:App
        get()=_app
        private lateinit var _app:App
    }
    override fun onCreate() {
        super.onCreate()
        _app = this
        _localBroadcastManager = LocalBroadcastManager.getInstance(this)
        _context = this.applicationContext
    }
}