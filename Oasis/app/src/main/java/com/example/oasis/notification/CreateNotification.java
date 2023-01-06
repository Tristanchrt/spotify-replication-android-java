package com.example.oasis.notification;

import android.app.Notification;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.oasis.R;
import com.example.oasis.models.AudioFile;
import com.example.oasis.utils.DownloadImageTask;

public class CreateNotification {

    public  static final String CHANNEL_ID = "channel1";

    public static Notification notification;

    public static void createNotification(Context context, AudioFile music, int playButton, int pos, int size) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(context, "tag");
            Bitmap icon;
            try {
                icon = new DownloadImageTask().execute(music.getImagePath()).get();
            } catch (Exception e) {
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.song);
            }
            notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.song)
                    .setContentTitle(music.getTitle())
                    .setContentText(music.getArtist())
                    .setLargeIcon(icon)
                    .setOnlyAlertOnce(true)
                    .setShowWhen(false)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build();

            synchronized (notificationManagerCompat){
                notificationManagerCompat.notify();
            }
        }
    }

}
