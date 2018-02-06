package top.atmb.autumnbox.ux.controls

import android.content.Context
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import top.atmb.autumnbox.ux.adapter.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewParent
import android.widget.LinearLayout
import top.atmb.autumnbox.R
/**
 * Created by zsh24 on 02/06/2018.
 */
class ToolBoxPage(context:Context):LinearLayout(context) {
    private lateinit var mViewPager: ViewPager
    private lateinit var mTab:TabLayout
    private lateinit var views:Array<View>
    init{
        LayoutInflater.from(context).inflate(R.layout.item_page_toolbox,this)
        mViewPager = findViewById(R.id.page_toolbox_inner)
        mTab = findViewById(R.id.tab_toolbox)
        initViews()
    }
    private fun initViews(){
        val pageRoot = LayoutInflater.from(context).inflate(R.layout.item_page_toolbox_root,null)
        val pageNoRoot = LayoutInflater.from(context).inflate(R.layout.item_page_toolbox_noroot,null)
        val titles = arrayOf(resources.getString(R.string.tab_toolbox_noroot),resources.getString(R.string.tab_toolbox_root))
        views = arrayOf(pageNoRoot,pageRoot)
        val adapter = UniversalPageAdapter(views,titles)
        mViewPager.adapter = adapter
        mTab.setupWithViewPager(mViewPager)
    }
}