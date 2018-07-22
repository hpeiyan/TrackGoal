package club.peiyan.goaltrack;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import club.peiyan.goaltrack.utils.TimeUtil;


/**
 * Created by HPY.
 * Time: 2018/7/21.
 * Desc:
 */

public class DownCountService extends Service {

    public static final String DOWN_COUNT = "DOWN_COUNT";
    public static final String COUNT_FINISH = "COUNT_FINISH";
    public static final String COUNT_STOP = "COUNT_STOP";
    public static final String DOWN_COUNT_ORIGIN = "DOWN_COUNT_ORIGIN";
    public static final String DOWN_COUNT_TAG = "DOWN_COUNT_TAG";
    public static final String NOTIFICATION = "club.peiyan.goaltrack";
    public static final String DOWN_COUNT_CHANNEL = "DOWN_COUNT_CHANNEL";
    private static final int DOWN_COUNT_NOTION_ID = 10052;

    private final IBinder mBinder = new MyBinder();
    private CountDownTimer mDownTimer;
    private boolean mIsStop;
    private boolean isDownCountServiceRun = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long mTime = intent.getLongExtra(DOWN_COUNT, 0);
        String[] mTags = intent.getStringArrayExtra(DOWN_COUNT_TAG);
        mIsStop = intent.getBooleanExtra(COUNT_STOP, false);
        boolean isFinish = intent.getBooleanExtra(COUNT_FINISH, false);
        if (mIsStop && mDownTimer != null) {
            mDownTimer.cancel();
            return Service.START_NOT_STICKY;
        }

        if (isFinish && mDownTimer != null) {
            mDownTimer.cancel();
            mDownTimer.onFinish();
            return Service.START_NOT_STICKY;
        }

        Intent notionIntent = new Intent(getApplicationContext(), ReLoginActivity.class);
        notionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notionIntent, Intent.FILL_IN_ACTION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), DOWN_COUNT_CHANNEL);
        mBuilder.setSmallIcon(R.mipmap.icon_app)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle(mTags[0])
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true);
        createNotificationChannel(getApplicationContext());

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

        mDownTimer = new CountDownTimer(mTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished <= TimeUtil.THRESHOLD) {
                    mDownTimer.cancel();
                    mDownTimer.onFinish();
                    return;
                }

                isDownCountServiceRun = true;
                Intent intent = new Intent(NOTIFICATION);
                intent.putExtra(DOWN_COUNT, millisUntilFinished);
                intent.putExtra(DOWN_COUNT_ORIGIN, mTime);
                intent.putExtra(DOWN_COUNT_TAG, mTags);
                sendBroadcast(intent);

                int mProgress = (int) ((100 * (mTime - millisUntilFinished) / (mTime - TimeUtil.THRESHOLD)));
                mBuilder.setProgress(100, mProgress, false);
                notificationManager.notify(DOWN_COUNT_NOTION_ID, mBuilder.build());
            }

            @Override
            public void onFinish() {
                isDownCountServiceRun = false;
                Intent intent = new Intent(NOTIFICATION);
                intent.putExtra(COUNT_FINISH, true);
                intent.putExtra(DOWN_COUNT_TAG, mTags);
                sendBroadcast(intent);
                mBuilder.setProgress(0, 0, false)
                        .setContentText("祝贺你，完成任务！！！");
                notificationManager.notify(DOWN_COUNT_NOTION_ID, mBuilder.build());
            }
        }.start();


        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }

    public class MyBinder extends Binder {
        DownCountService getService() {
            return DownCountService.this;
        }
    }

    public boolean isStop() {
        return mIsStop;
    }

    public boolean isDownCountServiceRun() {
        return isDownCountServiceRun;
    }

    private void createNotificationChannel(Context mContext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel_name";
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(DOWN_COUNT_CHANNEL, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
