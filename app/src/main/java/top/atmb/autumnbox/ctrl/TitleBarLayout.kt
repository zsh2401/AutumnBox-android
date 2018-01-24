package top.atmb.autumnbox.ctrl

import android.app.Activity
import android.content.Context
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import top.atmb.autumnbox.R
import top.atmb.autumnbox.activities.IOpenableDrawer

/**
 * Created by zsh24 on 01/24/2018.
 */
class TitleBarLayout(context: Context?, attrs:AttributeSet):LinearLayout(context,attrs) {
    companion object {
        val TAG = "TitleBar"
    }
    init {

        try{
            LayoutInflater.from(context).inflate(R.layout.titlebar,this)
            var oActivity = getContext() as IOpenableDrawer
            Log.d(TAG,"initing..." + oActivity)

            if(oActivity != null){
                findViewById<ImageView>(R.id.menu_icon).setOnClickListener({
                    oActivity.openDrawer()
                })
            }else{
                findViewById<ImageView>(R.id.menu_icon).visibility = View.GONE
            }
        }catch (ex:Exception){
            findViewById<ImageView>(R.id.menu_icon).visibility = View.GONE
        }

    }

}