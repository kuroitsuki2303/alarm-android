package com.cogeek.alarm2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.cogeek.alarm2.service.AlarmService;

import java.util.Calendar;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    public static final String MUSIC_URI = "MUSIC_URI";
    @Override
    public void onReceive(Context context, Intent intent) {
        startAlarmService(context,intent);
    }

    private void startAlarmService(Context context, Intent intent) {
        Intent intentService = new Intent(context, AlarmService.class);
        if (intent.getStringExtra(MUSIC_URI) != null) {
            intentService.putExtra(MUSIC_URI, intent.getStringExtra(MUSIC_URI));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }
}
