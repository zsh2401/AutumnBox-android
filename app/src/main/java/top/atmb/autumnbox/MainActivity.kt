package top.atmb.autumnbox

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import top.atmb.autumnbox.acp.ACPServer
import top.atmb.autumnbox.acpcommand.execute
import top.atmb.autumnbox.pmhelper.initPm

class MainActivity : AppCompatActivity() {
    companion object {
        val TAG:String = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initPm(this)
        val server= ACPServer()
        server.receivedEventHandler = { s -> execute(s) }
        server.start()
    }
}
