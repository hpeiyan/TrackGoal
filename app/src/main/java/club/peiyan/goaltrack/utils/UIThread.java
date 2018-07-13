package club.peiyan.goaltrack.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by HPY.
 * Time: 2018/7/13.
 * Desc:
 */

public class UIThread {

    public static void postDelay(Runnable mRunnable, long time) {
        new Handler(Looper.getMainLooper()).postDelayed(mRunnable, time);
    }

    public static void post(Runnable mRunnable) {
        new Handler(Looper.getMainLooper()).post(mRunnable);
    }
}
