package top.atmb.autumnbox.ux.controls

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by zsh24 on 02/06/2018.
 */
class NoScrollViewPager(context:Context, attr:AttributeSet):ViewPager(context,attr) {
    private var noScroll = true

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return if(noScroll){
            false
        }else{
            super.onTouchEvent(ev)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if(noScroll){
            false
        }else{
            super.onInterceptTouchEvent(ev)
        }
    }

    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        //TODO
        super.setCurrentItem(item, smoothScroll)
    }

    override fun setCurrentItem(item: Int) {
        //TODO
        super.setCurrentItem(item)
    }

}