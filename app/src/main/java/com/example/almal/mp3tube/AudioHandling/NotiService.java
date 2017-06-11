package com.example.almal.mp3tube.AudioHandling;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.almal.mp3tube.R;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NotiService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.example.almal.mp3tube.AudioHandling.action.FOO";
    private static final String ACTION_BAZ = "com.example.almal.mp3tube.AudioHandling.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.almal.mp3tube.AudioHandling.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.almal.mp3tube.AudioHandling.extra.PARAM2";

    public static NotificationCompat.Builder mBuilder;
    public boolean played=true;
    public static  NotificationManager mNotificationManager;


    public NotiService() {
        super("NotiService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, NotiService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, NotiService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            /*final String action = intent.getAction();
            String extra = intent.getStringExtra("player");
*/
            Intent yesReceive = new Intent();
            yesReceive.setAction("play");


            mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.headphone)
                    .setContentTitle("PlayMusic")
                    .setPriority(Notification.PRIORITY_MAX)
                    .setWhen(0);
            NotificationCompat.Action action1;

            PendingIntent pendingIntentYes;

            yesReceive.putExtra("player","pause");
            pendingIntentYes = PendingIntent.getBroadcast(this,12345, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);
            action1 = new NotificationCompat.Action.Builder(R.drawable.small_pause, "Pause", pendingIntentYes).build();

            mBuilder.addAction(action1);
            Intent notificationIntent = new Intent(this, AudioHandlingActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0,mBuilder.build());

        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
