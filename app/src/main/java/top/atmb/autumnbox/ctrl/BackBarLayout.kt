package top.atmb.autumnbox.ctrl

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import top.atmb.autumnbox.R
import android.widget.ImageView
import android.widget.LinearLayout
import top.atmb.autumnbox.activities.IOpenableDrawer

/**
 * Created by zsh24 on 01/24/2018.
 */
class BackBarLayout(context: Context?, attrs: AttributeSet): LinearLayout(context,attrs) {
    companion object {
        val TAG = "TitleBar"
    }
    init {
            LayoutInflater.from(context).inflate(R.layout.backbar,this)
            var oActivity = getContext() as Activity
                findViewById<ImageView>(R.id.img_back).setOnClickListener({
                    oActivity.finish()
                })
    }
}