package com.example.almal.mp3tube.AudioHandling;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by almal on 2017-05-30.
 */

public class NotificationHandling extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("hello","startreceiving");
    }
}
