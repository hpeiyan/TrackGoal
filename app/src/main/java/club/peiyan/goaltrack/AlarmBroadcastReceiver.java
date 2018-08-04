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
    private static final String CHANNEL_ID = "Goal_Alarm";
    private static final int NOTIFICATION_ID = 10086;
    public static final java.lang.String TITLE = "title";
    public static final java.lang.String CONTENT = "content";


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle mBundle = intent.getExtras();
        if (mBundle != null) {
            String mTitle = mBundle.getString(TITLE, "Goal Track");
            String mContent = mBundle.getString(CONTENT, "千里之行，始于足下，行动吧！");
            notice(context, mTitle, mContent);
        }
    }

    private void notice(Context mContext, String title, String content) {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(mContext, LoadingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.mipmap.icon_app)
                .setContentTitle("♨︎ " + title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true);

        createNotificationChannel(mContext);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
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
            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
