package com.example.almal.mp3tube.utilities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import com.example.almal.mp3tube.R;
import com.example.almal.mp3tube.ui.AudioHandling.AudioHandlingActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by almal on 2018-10-01.
 */

public class NotificationGenerator {


    private static final int NOTIFICATION_ID_OPEN_ACTIVITY = 9;
    private static final int NOTIFICATION_ID_CUSTOM_BIG = 9191100;
    NotificationCompat.Builder notificationBuilder;
    NotificationManager notificationManager;
    Context context;
    Intent notifyIntent;

    public NotificationGenerator(Context context, NotificationCompat.Builder notificationBuilder, NotificationManager notificationManager) {
        this.notificationBuilder = notificationBuilder;
        this.notificationManager = notificationManager;
        this.context = context;
    }

    public void openActivityNotification() {
        Intent notifyIntent = new Intent(context, AudioHandlingActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent notifyPendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(notifyPendingIntent);

        notificationBuilder.setSmallIcon(R.drawable.logo);
        notificationManager.notify(NOTIFICATION_ID_OPEN_ACTIVITY, notificationBuilder.build());

    }

    public void customBigNotification(String song_title,String url) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_bar);
        notifyIntent = new Intent(AudioHandlingActivity.getStartIntent(context));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(pendingIntent);

        Intent deleteIntent = new Intent(GlobalEntities.DELETE_INTENT);
        PendingIntent deletePendingIntent = PendingIntent.getBroadcast(context, 0, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setDeleteIntent(deletePendingIntent);


        notificationBuilder.setSmallIcon(R.drawable.logo);

        notificationBuilder.setCustomBigContentView(remoteViews);





        if (url.equals("") ||url == null) {
            url = "";
        }

        if(!song_title.equals("") && song_title !=null){
            notificationBuilder.getBigContentView().setTextViewText(R.id.notification_title_tv, song_title);
        }
            //notificationBuilder.getBigContentView().setImageViewResource(R.id.notification_logo, image_id);
        notificationBuilder.getBigContentView().setImageViewResource(R.id.notification_pause_btn, R.drawable.notification_pause_track);
        if(!url.equals("") && url !=null) {
            Picasso.with(context).load(url).into(remoteViews, R.id.notification_logo, NOTIFICATION_ID_CUSTOM_BIG, notificationBuilder.mNotification);
        }
       /* if (state.equals(GlobalEntities.PLAY_TAG)) {
            notificationBuilder.getBigContentView().setImageViewResource(R.id.notification_pause_btn, R.drawable.notification_play_track);
        } else {
            notificationBuilder.getBigContentView().setImageViewResource(R.id.notification_pause_btn, R.drawable.notification_pause_track);

        }*/

        setListeners(remoteViews, context);

        notificationManager.notify(NOTIFICATION_ID_CUSTOM_BIG, notificationBuilder.build());
    }

    private void setListeners(RemoteViews view, Context context) {
        Intent previous = new Intent(GlobalEntities.NOTIFY_PREVIOUS);
        Intent pause = new Intent(GlobalEntities.NOTIFY_PAUSE);
        Intent next = new Intent(GlobalEntities.NOTIFY_NEXT);

        PendingIntent pPrevious = PendingIntent.getBroadcast(context, 0, previous, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.notification_previous_btn, pPrevious);


        PendingIntent pPause = PendingIntent.getBroadcast(context, 0, pause, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.notification_pause_btn, pPause);


        PendingIntent pNext = PendingIntent.getBroadcast(context, 0, next, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.notification_next_btn, pNext);


    }

    public void changePlayPauseButton(String state) {
        if (state.equals(GlobalEntities.PLAY_TAG)) {
            //notificationBuilder.setOngoing(true);
            notificationBuilder.getBigContentView().setImageViewResource(R.id.notification_pause_btn, R.drawable.notification_pause_track);
        } else {
            notificationBuilder.getBigContentView().setImageViewResource(R.id.notification_pause_btn, R.drawable.notification_play_track);

        }

        notificationManager.notify(NOTIFICATION_ID_CUSTOM_BIG, notificationBuilder.build());
    }

}
