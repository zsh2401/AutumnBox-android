@file:Suppress("DEPRECATION")

package top.atmb.autumnbox.core.pmhelper

import android.content.pm.IPackageStatsObserver
import android.content.pm.PackageStats

/**
 * Created by zsh24 on 01/22/2018.
 */

class PackageSizeGettedHandler(var state: PkgState) : IPackageStatsObserver.Stub() {
    override fun onGetStatsCompleted(pStats: PackageStats?, succeeded: Boolean) {
        state.cacheSize = pStats!!.cacheSize
        state.codeSize = pStats!!.codeSize
        state.dataSize = pStats!!.dataSize
        state.isSeted = true
    }

}
