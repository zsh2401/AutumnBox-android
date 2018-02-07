package top.atmb.autumnbox.ux.controls

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.util.AttributeSet
import android.util.Log

/**
 * Created by zsh24 on 02/07/2018.
 */
class MyAppBar(context: Context,attr:AttributeSet):AppBarLayout(context,attr) {
    override fun setExpanded(expanded: Boolean, animate: Boolean) {
        val stack = Thread.currentThread().stackTrace
        for(s in stack){
            Log.d("AppBar","setExpanded caller: " + s.className)
        }
        stack.forEach { s->
            Log.d("AppBar","setExpanded caller: " + s.className)
        }
        super.setExpanded(expanded, animate)
    }

    override fun setExpanded(expanded: Boolean) {
        val stack = Thread.currentThread().stackTrace
        for(s in stack){
            Log.d("AppBar","setExpanded caller: " + s.className)
        }
        stack.forEach { s->
            Log.d("AppBar","setExpanded caller: " + s.className)
        }
        super.setExpanded(expanded)
    }

    override fun offsetTopAndBottom(offset: Int) {
        val stack = Thread.currentThread().stackTrace
        for(s in stack){
            Log.d("AppBar","offsetter caller: " + s.className)
        }
        super.offsetTopAndBottom(offset)
    }
}