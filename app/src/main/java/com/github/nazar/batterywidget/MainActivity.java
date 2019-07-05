package com.github.nazar.batterywidget;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private TextView batteryStatus;
    private TextView batteryLevel;
    private TextView batteryHealth;
    private TextView batteryTemperature;
    private TextView batteryVoltage;
    private ImageView iconView;

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
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

            int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
            batteryHealth.setText(healthStatus(health));

            int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
            batteryVoltage.setText(String.valueOf(voltage));

            int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
            float celcius = (temperature / 10f);
            batteryTemperature.setText(String.format("%.1f", celcius) + "°C");

            int icon = intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL, -1);
            if (icon != -1) {
                iconView.setImageResource(icon);
            } else {
                iconView.setImageResource(R.drawable.ic_launcher);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iconView = (ImageView) findViewById(R.id.iconView);
        batteryStatus = (TextView) findViewById(R.id.batteryStatus);
        batteryLevel = (TextView) findViewById(R.id.batteryLevel);
        batteryHealth = (TextView) findViewById(R.id.batteryHealth);
        batteryTemperature = (TextView) findViewById(R.id.batteryTemperature);
        batteryVoltage = (TextView) findViewById(R.id.batteryVoltage);

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        this.registerReceiver(this.mBatInfoReceiver, filter);
    }

    private String healthStatus(int health) {
        switch (health) {
            case BatteryManager.BATTERY_HEALTH_DEAD:
                return "Dead";
            case BatteryManager.BATTERY_HEALTH_GOOD:
                return "Good";
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                return "Over voltage";
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                return "Overheat";
            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                return "Unknown";
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                return "Failure";
            default:
                return "Unknown";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.mBatInfoReceiver != null) {
            this.unregisterReceiver(this.mBatInfoReceiver);
        }
    }
}
