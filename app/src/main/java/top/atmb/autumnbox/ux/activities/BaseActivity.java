package top.atmb.autumnbox.ux.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by zsh24 on 02/06/2018.
 */

public class BaseActivity extends AppCompatActivity {
    public static void finishAll(){
        for (Activity activity : activities) {
            activity.finish();
        }
    }
    private static ArrayList<Activity> activities = new ArrayList<Activity>();
    private static final String TAG = "BaseActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activities.add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activities.remove(this);
    }
}
