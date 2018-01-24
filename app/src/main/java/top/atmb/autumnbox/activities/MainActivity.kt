package top.atmb.autumnbox.activities

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import top.atmb.autumnbox.R
import top.atmb.autumnbox.acp.ACPServer
import top.atmb.autumnbox.acpcommand.execute
import top.atmb.autumnbox.pmhelper.initPm

class MainActivity : AppCompatActivity(),IOpenableDrawer {
    override fun openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START)
    }

    companion object {
        val TAG:String = "MainActivity"
    }
    lateinit var mDrawerLayout:DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mDrawerLayout = findViewById(R.id.drawer_layout)


        initPm(this)
        val server= ACPServer()

        var serverState = findViewById<TextView>(R.id.text_serverstate)
        server.receivedEventHandler = { s -> execute(s) }
        server.serverStarted = {
            Log.d(TAG,"server started received")
            runOnUiThread({
                serverState.setTextColor(0x00ee00)
                serverState.text = "已启动" })
            }
        server.serverFailed = {
            Log.d(TAG,"server failed received")
            runOnUiThread({
                serverState.setTextColor(0xee0000)
                serverState.text = "发生错误"})
            }
        server.start()

        findViewById<NavigationView>(R.id.nav_view).setNavigationItemSelectedListener({item->
            when(item.itemId){
                R.id.nav_officialWebsite->{
                    var intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("http://atmb.top")
                    startActivity(intent)
                }R.id.nav_opensource->{
                    var intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("https://github.com/zsh2401/AutumnBox-android/")
                    startActivity(intent)
                }R.id.nav_about->{
                    startActivity(Intent(this,AboutActivity::class.java))
                }R.id.nav_donate->{
                startActivity(Intent(this,DonateActivity::class.java))
                }R.id.nav_exit->{
                    finish()
                }
            }
            mDrawerLayout.closeDrawers()
            true
        })
    }
}
