package com.example.android.organizer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "ChannelID";
    public static final String channelName = "Channel";

    NotificationManager mManager;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationHelper(Context base) {
        super(base);
        createChannel();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID,channelName, NotificationManager.IMPORTANCE_DEFAULT);
                channel.enableLights(true);
                channel.enableVibration(true);
                channel.setLightColor(R.color.black);
                channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {

        if(mManager == null)
        {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }
String message = Todo.mTask + " " + Todo.mTime;
    public NotificationCompat.Builder getChannelNotification()
    {
        return new NotificationCompat.Builder(getApplicationContext(),channelID)
                .setContentTitle("Reminder for you task")
                .setContentText(message)
                .setSmallIcon(R.drawable.logo_splash_200x200)
                ;

    }

}
