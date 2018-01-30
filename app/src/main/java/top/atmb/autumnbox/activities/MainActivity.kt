package top.atmb.autumnbox.activities

import android.app.AlertDialog
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.support.v7.widget.SwitchCompat
import android.widget.TextView
import android.widget.Toast
import top.atmb.autumnbox.service.ACPService
import top.atmb.autumnbox.service.ACPServiceBroadcastReceiver
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
    private lateinit var receiver: ACPServiceBroadcastReceiver
    private lateinit var mSwitch:SwitchCompat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initCtrlObj()
        initStateText()
        initBroadcastReceiver()
        initDrawerClickEvent()
        initText()
        processBundle(savedInstanceState)
    }

    override fun onDestroy() {
        receiver.unregister()
        super.onDestroy()
    }

    private fun processBundle(bundle: Bundle?){
        if(bundle == null)return
        try{
            var startService = bundle.getBoolean("startService")
            if(startService){
                ACPService.start()
            }
        }catch (ex:Exception){
            ex.printStackTrace()
        }
    }
    /*Init functions*/
    private fun initCtrlObj(){
        mServerStateText = findViewById(R.id.text_serverstate)
        mDrawerLayout = findViewById(R.id.drawer_layout)
        mSwitch = findViewById(R.id.swc_service)
        mSwitch.setOnCheckedChangeListener({view,isChecked->
            if(isChecked){
                ACPService.start()
            }else{
                ACPService.stop()
            }
        })
    }
    private fun initStateText(){
        setStateCtrls(ACPService.isRunning)
    }
    private fun setStateCtrls(state:Boolean){
        mServerStateText.text = if(state)
            resources.getString(R.string.state_server_running)
        else resources.getString(R.string.state_server_ex)
        mServerStateText.setTextColor(if(state)
            resources.getColor(R.color.colorServerRunning)
        else resources.getColor(R.color.colorServerStopped))

        mSwitch.isChecked = state
    }
    private fun initBroadcastReceiver(){
        receiver = ACPServiceBroadcastReceiver(this)
        receiver.serverStarted = {
            runOnUiThread({
                Log.i(TAG,"ACPService started...setStateCtrls state to true")
                setStateCtrls(true)
            })
        }
        receiver.commandReceived={command->
            Log.i(TAG,"ACPService received command: " + command)
        }
        receiver.serverStopped = {
            runOnUiThread({
                Log.i(TAG,"ACPService stopped...setStateCtrls state to false")
                setStateCtrls(false)
            })
        }
        receiver.register()
    }
    private fun initText(){
        var t = findViewById<TextView>(R.id.text_desc__)
        t.text = String.format(resources.getString(R.string.main_desc_format),ACP.VERSION)
    }
    private fun initDrawerClickEvent(){
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
                stopService(Intent(this, ACPService::class.java))
                finish()
            }
            }
            mDrawerLayout.closeDrawers()
            true
        })
    }
}
