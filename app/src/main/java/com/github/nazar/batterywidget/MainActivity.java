package com.github.nazar.batterywidget;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private TextView batteryStatus;
    private TextView batteryLevel;
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: " + intent.getExtras().keySet());

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
            int percent = (level * 100) / scale;
            batteryLevel.setText(String.valueOf(percent) + "%");

            int chargeStatus = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            if (chargeStatus == BatteryManager.BATTERY_STATUS_CHARGING) {
                batteryStatus.setText("Charging");
            } else {
                batteryStatus.setText("Idle");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        batteryStatus = (TextView) findViewById(R.id.batteryStatus);
        batteryLevel = (TextView) findViewById(R.id.batteryLevel);

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        this.registerReceiver(this.mBatInfoReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.mBatInfoReceiver != null) {
            this.unregisterReceiver(this.mBatInfoReceiver);
        }
    }
}
