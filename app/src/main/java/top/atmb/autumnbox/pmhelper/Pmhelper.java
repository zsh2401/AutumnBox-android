package top.atmb.autumnbox.pmhelper;

import android.content.Context;
import android.content.pm.*;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

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
//            pi.applicationInfo.
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
}
