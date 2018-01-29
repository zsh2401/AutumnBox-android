package top.atmb.autumnbox.activities

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import top.atmb.autumnbox.ACPService
import top.atmb.autumnbox.R
import top.atmb.autumnbox.acp.ACP
import top.atmb.autumnbox.util.getVersionName
import top.atmb.autumnbox.util.gotoAlipay
import top.atmb.autumnbox.util.openAlipay

class MainActivity : AppCompatActivity(),IOpenableDrawer {
    override fun openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START)
    }

    companion object {
        val TAG:String = "MainActivity"
    }
    private lateinit var mServerStateText:TextView
    private lateinit var mDrawerLayout:DrawerLayout
    private lateinit var receiver:ACPServiceBroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var t = findViewById<TextView>(R.id.text_desc__)
        t.text = String.format(resources.getString(R.string.main_desc_format),ACP.VERSION)
        mServerStateText = findViewById(R.id.text_serverstate)
        mDrawerLayout = findViewById(R.id.drawer_layout)
        mServerStateText.text = if(ACPService.isRunning)
            resources.getString(R.string.state_server_running)
            else resources.getString(R.string.state_server_loading)

        mServerStateText.setTextColor(resources.getColor(R.color.colorServerRunning))
        receiver = ACPServiceBroadcastReceiver(this)
        receiver.serverStarted = {
            runOnUiThread({
                Log.d(TAG,"server started")
                mServerStateText.setTextColor(resources.getColor(R.color.colorServerRunning))
                mServerStateText.text = resources.getString(R.string.state_server_running)
            })
        }
        receiver.serverStopped = {
            runOnUiThread({
                mServerStateText.setTextColor(resources.getColor(R.color.colorServerStopped))
                mServerStateText.text = resources.getString(R.string.state_server_ex)
            })
        }
        receiver.register()

        if(!ACPService.isRunning){
            startService(Intent(this,ACPService::class.java))
        }

        bindDrawerClickEvent()
    }

    override fun onDestroy() {
        receiver.unregister()
        super.onDestroy()
    }
    private fun bindDrawerClickEvent(){
        findViewById<NavigationView>(R.id.nav_view).setNavigationItemSelectedListener({item->
            when(item.itemId){
                R.id.nav_officialWebsite->{
                    var intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("http://atmb.top")
                    startActivity(intent)
                }R.id.nav_opensource->{
                    var dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setTitle(resources.getString(R.string.title_pls_select_repo))
                dialogBuilder.setNegativeButton(R.string.autumnbox_android,{dialog,which->
                    var intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("https://github.com/zsh2401/AutumnBox-android/")
                    startActivity(intent)
                    dialog.dismiss()
                })
                dialogBuilder.setPositiveButton(R.string.autumnbox_pc,{dialog,which->
                    var intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("https://github.com/zsh2401/AutumnBox/")
                    startActivity(intent)
                    dialog.dismiss()
                })
               dialogBuilder.show()
            }R.id.nav_about->{
                var dialog =  AlertDialog.Builder(this)
                dialog.setTitle(resources.getString(R.string.title_about) + " " + getVersionName() )
                dialog.setMessage(R.string.msg_about)
                dialog.setPositiveButton(R.string.btn_ok,{dialog,wich->
                    dialog.dismiss()
                })
                dialog.show()
            }R.id.nav_donate->{
                var dialog =  AlertDialog.Builder(this)
                dialog.setTitle(R.string.title_donate)
                dialog.setMessage(R.string.msg_donate)
                dialog.setNeutralButton(R.string.btn_donate_cpwechat,{dialog,which->
                    var cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    cm.text = "Ryme2401"
                    Toast.makeText(
                            this,resources.getString(R.string.toast_already_copy),
                            Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                })
                dialog.setPositiveButton(R.string.btn_donate_goto_alipaytransfer,{dialog,which->
                    gotoAlipay()
                    dialog.dismiss()
                })
                dialog.setNegativeButton(R.string.btn_donate_cpalipay_code,{dialog,which->
                    var cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    cm.text = "wTGShq38cq"
                    openAlipay()
                    Toast.makeText(
                            this,resources.getString(R.string.toast_already_copy),
                            Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                })
                dialog.show()
            }R.id.nav_exit->{
                stopService(Intent(this,ACPService::class.java))
                finish()
            }
            }
            mDrawerLayout.closeDrawers()
            true
        })
    }
    private class ACPServiceBroadcastReceiver(context:Context) : BroadcastReceiver(){
        companion object {
            val TAG = "ACPSBReceiver"
        }
        private var localBroadcastManager:LocalBroadcastManager = LocalBroadcastManager.getInstance(context)
        private var intentFilter:IntentFilter = IntentFilter()
        init {
            intentFilter.addAction(ACPService.BC_ACP_SERVER_ERROR)
            intentFilter.addAction(ACPService.BC_COMMAND_PROCESSED)
            intentFilter.addAction(ACPService.BC_COMMAND_RECEIVED)
            intentFilter.addAction(ACPService.BC_ACP_SERVER_STARTED)
            intentFilter.addAction(ACPService.BC_ACP_SERVER_STOPPED)
        }
        var commandReceived:((String)->Unit)? = null
        var commandProcessed:((Int)->Unit)? = null
        var serverCrashed:((Exception)->Unit)?=null
        var serverStarted:(()->Unit)? = null
        var serverStopped:(()->Unit)?=null
        override fun onReceive(p0: Context?, p1: Intent?) {
            Log.d(TAG,"received broadcast->" + p1!!.action)
            when(p1!!.action){
                ACPService.BC_ACP_SERVER_STARTED->{serverStarted?.invoke() }
                ACPService.BC_COMMAND_RECEIVED->{
                    commandReceived?.invoke(p1!!.getStringExtra("command"))}
                ACPService.BC_COMMAND_PROCESSED->{
                    commandProcessed?.invoke(p1.getIntExtra("fcode",-1))}
                ACPService.BC_ACP_SERVER_ERROR->{
                    serverCrashed?.invoke(p1.extras["exception"] as Exception)
                }
                ACPService.BC_ACP_SERVER_STOPPED->{
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
}
