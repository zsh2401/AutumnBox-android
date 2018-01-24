package top.atmb.autumnbox

import android.app.Application
import android.content.Context

/**
 * Created by zsh24 on 01/25/2018.
 */
class App: Application() {
    companion object {
        var context: Context
        private set(value) {_context = value}
        get() = _context
        private lateinit var _context:Context
    }
    override fun onCreate() {
        super.onCreate()
        context = this.applicationContext
    }
}