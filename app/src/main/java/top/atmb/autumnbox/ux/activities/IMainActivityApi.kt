package top.atmb.autumnbox.ux.activities

/**
 * Created by zsh24 on 01/24/2018.
 */
interface IMainActivityApi {
    fun openDrawer()
    fun closeDrawer()
    fun setExpandedAppBar(setExpanded:Boolean,animate:Boolean=true)
}