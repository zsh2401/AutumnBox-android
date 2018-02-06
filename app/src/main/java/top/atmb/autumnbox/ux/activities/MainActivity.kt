package top.atmb.autumnbox.ux.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.util.Log
import android.view.View
import top.atmb.autumnbox.R
import top.atmb.autumnbox.ux.adapter.UniversalPageAdapter
import top.atmb.autumnbox.ux.controls.AcpPage
import top.atmb.autumnbox.ux.controls.ToolBoxPage

class MainActivity : BaseActivity(), IDrawerControllable {
    override fun openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START)
    }

    override fun closeDrawer() {
        mDrawerLayout.closeDrawers()
    }

    companion object {
        val TAG:String = "MainActivity"
    }
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mToolBar: android.support.v7.widget.Toolbar
    private lateinit var mBtmNavView: BottomNavigationView
    private lateinit var mViewPager:ViewPager
    private lateinit var mViewList:Array<View>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"cearting")
        setContentView(R.layout.activity_main)
        init()
    }
    private fun init(){
        initViewObj()
        initToolBar()
        initViewPager()
        initBtmNavEvent()
    }

    private fun initViewObj(){
        mDrawerLayout = findViewById(R.id.drawer_layout)
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
                AcpPage(this),
                ToolBoxPage(this)
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

    override fun onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawers()
        }else{
            super.onBackPressed()
        }
    }
}
