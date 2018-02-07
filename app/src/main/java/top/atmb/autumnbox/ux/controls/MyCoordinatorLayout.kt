package top.atmb.autumnbox.ux.controls

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewParent

/**
 * Created by zsh24 on 02/07/2018.
 */
class MyCoordinatorLayout(context: Context,attr:AttributeSet):CoordinatorLayout(context,attr) {
    companion object {
        private const val TAG = "MyCoodinatorLayout"
    }
    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        Log.d(TAG,"scorlling...")
        try{
            val pager = (child as ViewPager)
            return ( pager.getChildAt(pager.currentItem) as IScrollSettingGetable).canScroll()
        }catch (ex:Exception){}
        return super.onStartNestedScroll(child, target, axes, type)
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        Log.d(TAG,"scorlling2...")
        try{
            val pager = (child as ViewPager)
            return ( pager.getChildAt(pager.currentItem) as IScrollSettingGetable).canScroll()
        }catch (ex:Exception){}
        return super.onStartNestedScroll(child, target, nestedScrollAxes)
    }

    override fun onNestedFling(target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        //What the fuck? what i do? why bug fixed? return false just?
        /*BUG复述:
        当在ToolBoxPage滑动并使得appbar被折叠后,通过底部栏切换到AcpPage
        切换到后最初显示的页,appbar是展开的,但滑动到旁边的时,appbar又会缩回去
        奇怪
        */
        Log.d(TAG,"what?")
        return false
//        return super.onNestedFling(target, velocityX, velocityY, consumed)
    }
}