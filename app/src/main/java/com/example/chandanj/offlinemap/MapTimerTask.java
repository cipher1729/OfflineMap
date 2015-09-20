package com.example.chandanj.offlinemap;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by cipher1729 on 9/19/2015.
 */
public class MapTimerTask  extends TimerTask{
    private Context context;
    private Timer timer;
    private int stepTracker;
    public static boolean keepScheduling;
    public MapTimerTask(Context context, int stepTracker, Timer timer, boolean keepScheduling)
    {
        this.context = context;
        this.timer= timer;
        this.keepScheduling = keepScheduling;
        this.stepTracker = stepTracker;
    }
    @Override
    public void run() {
        //start media player
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        MediaPlayer mp = MediaPlayer.create(context, notification);
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mp != null) {
                    mp.start();
                }
            }
        });
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stepTracker++;
                //not sure how much time to wait after this
                if(stepTracker==MainActivity.routeList.get(0).legs[0].steps.length)
                    keepScheduling= false;
                if(keepScheduling)
                {

                    MainActivity.trackerDist = Utils.parseDistance(MainActivity.routeList.get(0).legs[0].steps[stepTracker].distance);
                    MainActivity.trackerTime = Utils.parseDuration(MainActivity.routeList.get(0).legs[0].steps[stepTracker].duration);
                    //MainActivity.textView.setText(MainActivity.routeList.get(0).legs[0].steps[stepTracker].duration);
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.notification_template_icon_bg)
                            .setContentTitle(MainActivity.routeList.get(0).legs[0].steps[stepTracker].distance)
                            .setContentText("Next turn coming up in 10 minutes");
                    NotificationManager mNotificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(stepTracker,mBuilder.build());
                    MainActivity.statusTextView.setText("Timer " +String.valueOf(keepScheduling) + " with next values "+ MainActivity.routeList.get(0).legs[0].steps[stepTracker].distance + " "+ MainActivity.routeList.get(0).legs[0].steps[stepTracker].duration );
                    long alertTime = Utils.alertTime(MainActivity.routeList.get(0).legs[0].steps[stepTracker].duration);
                    timer.schedule(new MapTimerTask(context,stepTracker, timer, keepScheduling),MainActivity.trackerTime - alertTime);
                }
            }
        });

    }
}
