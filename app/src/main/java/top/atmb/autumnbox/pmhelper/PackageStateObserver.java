package top.atmb.autumnbox.pmhelper;

import android.content.pm.PackageStats;
import android.os.RemoteException;
import android.util.Log;
import android.content.pm.*;
/**
 * Created by zsh24 on 01/22/2018.
 */

public class PackageStateObserver extends IPackageStatsObserver.Stub {
    PkgState state ;
    public PackageStateObserver(PkgState result){
        this.state = result;
    }
    @Override
    public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
            throws RemoteException {

        long cacheSize = pStats.cacheSize; //缓存大小
        long dataSize = pStats.dataSize;  //数据大小
        long codeSize = pStats.codeSize;  //应用程序大小
        state.cacheSize =  pStats.cacheSize;
        state.dataSize = pStats.dataSize;
        state.codeSize = pStats.codeSize;
        state.isSet = true;
        Log.d("abc", "onGetStatsCompleted: " + cacheSize + ":" + dataSize + ":" + codeSize);
    }
}
