package top.atmb.autumnbox.ux.adapter

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

/**
 * Created by zsh24 on 02/05/2018.
 */
class UniversalPageAdapter(private val views:Array<View>,private val titles:Array<String>?=null):PagerAdapter() {
    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
       return view == `object`
    }

    override fun getCount(): Int {
        return views.size
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container?.removeView(views[position])
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        container?.addView(views[position])
        return views[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        if(titles != null){
            return titles[position]
        }
        return super.getPageTitle(position)
    }
}