package top.atmb.autumnbox.ux.activities

import android.app.AlertDialog
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SwitchCompat
import android.view.View
import android.widget.TextView
import android.widget.Toast
import top.atmb.autumnbox.R
import top.atmb.autumnbox.acp.ACP
import top.atmb.autumnbox.service.ACPService
import top.atmb.autumnbox.core.util.getVersionName
import top.atmb.autumnbox.core.util.gotoAlipay
import top.atmb.autumnbox.core.util.openAlipay
import top.atmb.autumnbox.service.ACPServiceBroadcastReceiver
import top.atmb.autumnbox.ux.adapter.UniversalPageAdapter

class MainActivity : AppCompatActivity(), IOpenableDrawer {
    override fun openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START)
    }

    companion object {
        val TAG:String = "MainActivity"
    }
    private lateinit var mNavView: NavigationView
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mToolBar: android.support.v7.widget.Toolbar
    private lateinit var mBtmNavView: BottomNavigationView
    private lateinit var mViewPager:ViewPager
    lateinit var mViewList:Array<View>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }
    private fun init(){
        initViewObj()
        initNavClickEvent()
        initToolBar()
        initViewPager()
        initBtmNavEvent()
        initAcpPage()
    }
    private fun initAcpPage(){
        val pageAcp = mViewList[0]
        val pageAcpTab = pageAcp.findViewById<TabLayout>(R.id.page_acp_tab)
        val pageAcpInner = pageAcp.findViewById<ViewPager>(R.id.page_acp_inner)
        val pageAcpStatus = layoutInflater.inflate(R.layout.item_page_acp_status,null)
        val pageAcpInfo = layoutInflater.inflate(R.layout.item_page_acp_info,null)
        val views = arrayOf(
                pageAcpStatus,
                pageAcpInfo
        )
        val titles = arrayOf(
                resources.getString(R.string.tab_acp_status),
                resources.getString(R.string.tab_acp_info))
        val adapter = UniversalPageAdapter(views,titles)
        pageAcpInner.adapter =adapter
        pageAcpTab.setupWithViewPager(pageAcpInner)

        //init acp status page views
        pageAcpStatus.findViewById<TextView>(R.id.text_acp_status_desc).text =
                String.format(resources.getString(R.string.main_desc_format), ACP.VERSION)
        val textServiceState = pageAcpStatus.findViewById<TextView>(R.id.text_service_state)
        val switchServiceState = pageAcpStatus.findViewById<SwitchCompat>(R.id.switch_service_state)
        switchServiceState.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                ACPService.start()
            }else{
                ACPService.stop()
            }
        }
        val receiver = ACPServiceBroadcastReceiver(this)
        receiver.register()
        receiver.serverStarted = {
            textServiceState.text =resources.getString(R.string.state_server_running)
            switchServiceState.isChecked = true
        }
        receiver.serverStopped = {
            textServiceState.text = resources.getString(R.string.state_server_ex)
            switchServiceState.isChecked = false
        }
    }

    private fun initViewObj(){
        mDrawerLayout = findViewById(R.id.drawer_layout)
        mNavView = findViewById(R.id.nav_view)
        mBtmNavView = findViewById(R.id.btm_nav_view)
        mToolBar = findViewById(R.id.tool_bar)
        mViewPager = findViewById(R.id.main_view_pager)
    }
    private fun initToolBar(){
        setSupportActionBar(mToolBar)
        var toggle = ActionBarDrawerToggle(this,mDrawerLayout,mToolBar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        mDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }
    private fun initViewPager(){
        mViewList = arrayOf(
                layoutInflater.inflate(R.layout.item_page_acp,null),
                layoutInflater.inflate(R.layout.item_page_toolbox,null)
        )
        mViewPager.adapter = UniversalPageAdapter(mViewList)
    }

    private fun initBtmNavEvent(){
        mBtmNavView.setOnNavigationItemSelectedListener { item->
            when(item.itemId){
                R.id.item_btm_acp->{
                    mViewPager.currentItem = 0;true}
                R.id.item_btm_toolbox->{
                    mViewPager.currentItem = 1;true}
                else->false
            }
        }
        mViewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                when(position){
                    0->{ mBtmNavView.selectedItemId=R.id.item_btm_acp}
                    1->{mBtmNavView.selectedItemId = R.id.item_btm_toolbox}
                }
            }
        })
    }
    private fun initNavClickEvent(){
        mNavView.setNavigationItemSelectedListener({item->
            when(item.itemId){
                R.id.nav_officialWebsite ->{
                    var intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("http://atmb.top")
                    startActivity(intent)
                }
                R.id.nav_opensource ->{
                    var dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setTitle(resources.getString(R.string.title_pls_select_repo))
                dialogBuilder.setNegativeButton(R.string.autumnbox_android,{ dialog, which->
                    var intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("https://github.com/zsh2401/AutumnBox-android/")
                    startActivity(intent)
                    dialog.dismiss()
                })
                dialogBuilder.setPositiveButton(R.string.autumnbox_pc,{ dialog, which->
                    var intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("https://github.com/zsh2401/AutumnBox/")
                    startActivity(intent)
                    dialog.dismiss()
                })
               dialogBuilder.show()
            }
                R.id.nav_about ->{
                var dialog =  AlertDialog.Builder(this)
                dialog.setTitle(resources.getString(R.string.title_about) + " " + getVersionName() )
                dialog.setMessage(R.string.msg_about)
                dialog.setPositiveButton(R.string.btn_ok,{ dialog, wich->
                    dialog.dismiss()
                })
                dialog.show()
            }
                R.id.nav_donate ->{
                var dialog =  AlertDialog.Builder(this)
                dialog.setTitle(R.string.title_donate)
                dialog.setMessage(R.string.msg_donate)
                dialog.setNeutralButton(R.string.btn_donate_cpwechat,{ dialog, which->
                    var cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    cm.text = "Ryme2401"
                    Toast.makeText(
                            this,resources.getString(R.string.toast_already_copy),
                            Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                })
                dialog.setPositiveButton(R.string.btn_donate_goto_alipaytransfer,{ dialog, which->
                    gotoAlipay()
                    dialog.dismiss()
                })
                dialog.setNegativeButton(R.string.btn_donate_cpalipay_code,{ dialog, which->
                    var cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    cm.text = "wTGShq38cq"
                    openAlipay()
                    Toast.makeText(
                            this,resources.getString(R.string.toast_already_copy),
                            Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                })
                dialog.show()
            }
                R.id.nav_exit ->{
                stopService(Intent(this, ACPService::class.java))
                finish()
            }
            }
            mDrawerLayout.closeDrawers()
            true
        })
    }
}
