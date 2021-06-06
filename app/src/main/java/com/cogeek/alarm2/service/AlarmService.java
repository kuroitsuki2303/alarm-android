package com.cogeek.alarm2.service;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.cogeek.alarm2.R;
import com.cogeek.alarm2.activity.NotificationActivity;

import static com.cogeek.alarm2.AlarmBroadcastReceiver.MUSIC_URI;
import static com.cogeek.alarm2.application.App.CHANNEL_ID;

public class AlarmService extends Service {
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);

        if (intent.getStringExtra(MUSIC_URI) != null) {
            Uri muUri = Uri.parse(intent.getStringExtra(MUSIC_URI));
            mediaPlayer = MediaPlayer.create(this, muUri);
        }

        mediaPlayer.setLooping(true);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        Intent notificationIntent = new Intent(this, NotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Tha được chưa??")
                .setContentText("Ring Ring .. Ring Ring")
                .setSmallIcon(R.drawable.ic_baseline_alarm_24)
                .setContentIntent(pendingIntent)
                .build();

        mediaPlayer.start();

        long[] pattern = { 0, 100, 1000 };
        vibrator.vibrate(pattern, 0);

        startForeground(1, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();
        vibrator.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
