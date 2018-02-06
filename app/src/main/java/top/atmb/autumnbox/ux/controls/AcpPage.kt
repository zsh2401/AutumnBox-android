package top.atmb.autumnbox.ux.controls

import android.content.Context
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.SwitchCompat
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import top.atmb.autumnbox.acp.ACP
import top.atmb.autumnbox.R
import top.atmb.autumnbox.service.*
import top.atmb.autumnbox.ux.adapter.UniversalPageAdapter
/**
 * Created by zsh24 on 02/06/2018.
 */
class AcpPage(context:Context):LinearLayout(context) {
    private val layoutInflater:LayoutInflater = LayoutInflater.from(context)
    init {
        layoutInflater.inflate(R.layout.item_page_acp,this)
        initTab()
    }
    private fun initTab(){
        val pageAcp = this
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
        val adapter = UniversalPageAdapter(views, titles)
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
        val receiver = ACPServiceBroadcastReceiver(context)
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
}