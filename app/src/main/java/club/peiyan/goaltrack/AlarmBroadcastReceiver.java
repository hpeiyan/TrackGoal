package club.peiyan.goaltrack;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by HPY.
 * Time: 2018/7/18.
 * Desc:
 */

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "CHANNEL_ID";
    public static final java.lang.String TITLE = "title";
    public static final java.lang.String CONTENT = "content";


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle mBundle = intent.getExtras();
        if (mBundle != null) {
            String mTitle = mBundle.getString(TITLE, "GoalTrack");
            String mContent = mBundle.getString(CONTENT, "content");
            notice(context, mTitle, mContent);
        }
    }

    private void notice(Context mContext, String title, String content) {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(mContext, ReLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.mipmap.icon_app)
                .setContentTitle("Goal Track")
                .setContentText(title)
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                .addAction(R.drawable.ic_menu_send, "action",
//                        snoozePendingIntent)
                .setAutoCancel(true);

        // Issue the initial notification with zero progress
//        int PROGRESS_MAX = 100;
//        int PROGRESS_CURRENT = 60;
//        mBuilder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);

        createNotificationChannel(mContext);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
// notificationId is a unique int for each notification that you must define
        notificationManager.notify(1024, mBuilder.build());
    }

    private void createNotificationChannel(Context mContext) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel_name";
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
