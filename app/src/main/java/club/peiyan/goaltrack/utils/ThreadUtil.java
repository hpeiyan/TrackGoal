package club.peiyan.goaltrack.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by HPY.
 * Time: 2018/7/13.
 * Desc:
 */

public class ThreadUtil {

    public static void uiPostDelay(Runnable mRunnable, long time) {
        new Handler(Looper.getMainLooper()).postDelayed(mRunnable, time);
    }

    public static void uiPost(Runnable mRunnable) {
        new Handler(Looper.getMainLooper()).post(mRunnable);
    }

    public static void ioThread(Runnable mRunnable) {
        new Thread(mRunnable).start();
    }
}
