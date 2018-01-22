package top.atmb.autumnbox;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import top.atmb.autumnbox.acp.processor.ACPDataBuilder;
import top.atmb.autumnbox.acp.processor.AutoProcessor;
import  top.atmb.autumnbox.pmhelper.*;
import top.atmb.autumnbox.acp.*;

public class MainActivity extends Activity implements ACPResponser{

    private static final String TAG = "MainActivity";
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: wtf???");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);

        PmHelperKt.initPm(this);
        ACPServer server=  new ACPServer();
        server.setResponser(this);
        server.start();
    }

    @Override
    public ACPDataBuilder onRequestReceived(String command) {
        Log.d(TAG, "onRequestReceived: received command " + command);
        ACPDataBuilder builder = AutoProcessor.process(command);
        return builder;
    }
}
