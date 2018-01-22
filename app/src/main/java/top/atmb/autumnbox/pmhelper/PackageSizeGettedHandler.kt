@file:Suppress("DEPRECATION")

package top.atmb.autumnbox.pmhelper

import android.content.pm.IPackageStatsObserver
import android.content.pm.PackageStats

/**
 * Created by zsh24 on 01/22/2018.
 */

class PackageSizeGettedHandler(_state: PkgState) : IPackageStatsObserver.Stub() {
    private var state: PkgState = _state
    override fun onGetStatsCompleted(pStats: PackageStats?, succeeded: Boolean) {
        state.cacheSize = pStats!!.cacheSize
        state.codeSize = pStats!!.codeSize
        state.dataSize = pStats!!.dataSize
        state.isSeted = true
    }

}
