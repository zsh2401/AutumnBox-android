package top.atmb.autumnbox.ux.controls

import android.app.AlertDialog
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import top.atmb.autumnbox.service.*
import android.support.design.widget.NavigationView
import android.util.AttributeSet
import top.atmb.autumnbox.ux.activities.BaseActivity
import android.view.LayoutInflater
import android.widget.Toast
import top.atmb.autumnbox.R
import top.atmb.autumnbox.core.util.getVersionName
import top.atmb.autumnbox.core.util.gotoAlipay
import top.atmb.autumnbox.core.util.openAlipay
import top.atmb.autumnbox.ux.activities.MainActivity

/**
 * Created by zsh24 on 02/06/2018.
 */
class NavView(context: Context, attr:AttributeSet):NavigationView(context,attr) {
    companion object {
        const val TAG = "NavView"
    }

    private var mFatherActivity:MainActivity
    private var mNavView:NavigationView
    init {
        LayoutInflater.from(context).inflate(R.layout.nav_view_layout,this)
        mNavView = findViewById(R.id.nav_view)
        mFatherActivity = context as MainActivity
        initEvent()
    }
    private fun initEvent(){
        mNavView.setNavigationItemSelectedListener { item->
            when(item.itemId) {
                R.id.nav_officialWebsite -> {
                    var intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("http://atmb.top")
                    context.startActivity(intent)
                }
                R.id.nav_opensource -> {
                    var dialogBuilder = AlertDialog.Builder(context)
                    dialogBuilder.setTitle(resources.getString(R.string.title_pls_select_repo))
                    dialogBuilder.setNegativeButton(R.string.autumnbox_android, { dialog, which ->
                        var intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse("https://github.com/zsh2401/AutumnBox-android/")
                        context.startActivity(intent)
                        dialog.dismiss()
                    })
                    dialogBuilder.setPositiveButton(R.string.autumnbox_pc, { dialog, which ->
                        var intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse("https://github.com/zsh2401/AutumnBox/")
                        context.startActivity(intent)
                        dialog.dismiss()
                    })
                    dialogBuilder.show()
                }
                R.id.nav_about -> {
                    var dialog = AlertDialog.Builder(context)
                    dialog.setTitle(resources.getString(R.string.title_about) + " " + getVersionName())
                    dialog.setMessage(R.string.msg_about)
                    dialog.setPositiveButton(R.string.btn_ok, { dialog, wich ->
                        dialog.dismiss()
                    })
                    dialog.show()
                }
                R.id.nav_donate -> {
                    var dialog = AlertDialog.Builder(context)
                    dialog.setTitle(R.string.title_donate)
                    dialog.setMessage(R.string.msg_donate)
                    dialog.setNeutralButton(R.string.btn_donate_cpwechat, { dialog, which ->
                        var cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        cm.text = "Ryme2401"
                        Toast.makeText(
                                context, resources.getString(R.string.toast_already_copy),
                                Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    })
                    dialog.setPositiveButton(R.string.btn_donate_goto_alipaytransfer, { dialog, which ->
                        gotoAlipay()
                        dialog.dismiss()
                    })
                    dialog.setNegativeButton(R.string.btn_donate_cpalipay_code, { dialog, which ->
                        var cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        cm.text = "wTGShq38cq"
                        openAlipay()
                        Toast.makeText(
                                context, resources.getString(R.string.toast_already_copy),
                                Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    })
                    dialog.show()
                }
                R.id.nav_exit->{
                    ACPService.stop()
                    BaseActivity.finishAll()
                }
            }
            mFatherActivity?.closeDrawer()
            true
        }
    }
}