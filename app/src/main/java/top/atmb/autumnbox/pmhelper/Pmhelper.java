package top.atmb.autumnbox.pmhelper;

import android.content.Context;
import android.content.pm.*;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.UserHandle;
import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;

import static android.content.ContentValues.TAG;

/**
 * Created by zsh24 on 01/21/2018.
 */

public final class Pmhelper {
    private static PackageManager pm;
    public static void init(Context context){
        pm = context.getPackageManager();
    }
    public static JSONObject getAppInfo(String packageName) throws PackageManager.NameNotFoundException{
        try{
            PackageInfo pi = pm.getPackageInfo(packageName,0);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("packageName",pi.packageName);
            jsonObject.put("name",pi.applicationInfo.loadLabel(pm).toString());

            PkgState sPkg = getAppUseSpace(packageName);
            jsonObject.put("cacheSize",sPkg.cacheSize);
            jsonObject.put("codeSize",sPkg.codeSize);
            jsonObject.put("dataSize",sPkg.dataSize);
            return jsonObject;
        }catch (Exception ex){
            ex.printStackTrace();
            Log.d(TAG, "getAppname: failed");
            return null;
        }
    }
    public static byte[] getAppIcon(String packageName)throws PackageManager.NameNotFoundException {
        PackageInfo pi = pm.getPackageInfo(packageName,0);
        Drawable icon =  pi.applicationInfo.loadIcon(pm);
        Bitmap bmp = ((BitmapDrawable)icon).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG,100,stream);
        return stream.toByteArray();
    }

    private static int userId(){
        try{
            Method myUserId=UserHandle.class.getDeclaredMethod("myUserId");//ignore check this when u set ur min SDK < 17
            int userID = (Integer) myUserId.invoke(pm);
            return userID;
        }catch (Exception ex){
            return 0;
        }
    }
    private static Method getPackageSizeMethod;
    private static void init_getPackageSizeMethod(){
        if(getPackageSizeMethod != null)return;
        if( Build.VERSION.SDK_INT <= 16 ||Build.VERSION.SDK_INT >= 26) return;
        try{
            getPackageSizeMethod = pm.getClass().getDeclaredMethod(
                    "getPackageSizeInfo",String.class,int.class,IPackageStatsObserver.class);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public static PkgState getAppUseSpace(String pkgName){
        PkgState result = new PkgState();
        try{
            init_getPackageSizeMethod();
            //调用方法，待调用流程完成后会回调PkgSizeObserver类的方法
            getPackageSizeMethod.invoke(pm, pkgName,userId(), new PackageStateObserver(result));
            while(!result.isSet);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }
}
