package club.peiyan.goaltrack;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;


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
    private final IBinder mBinder = new MyBinder();
    private CountDownTimer mDownTimer;
    private boolean mIsStop;

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

        mDownTimer = new CountDownTimer(mTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Intent intent = new Intent(NOTIFICATION);
                intent.putExtra(DOWN_COUNT, millisUntilFinished);
                intent.putExtra(DOWN_COUNT_ORIGIN, mTime);
                intent.putExtra(DOWN_COUNT_TAG, mTags);
                sendBroadcast(intent);
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(NOTIFICATION);
                intent.putExtra(COUNT_FINISH, true);
                intent.putExtra(DOWN_COUNT_TAG, mTags);
                sendBroadcast(intent);
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
}
