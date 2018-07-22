package club.peiyan.goaltrack.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by HPY.
 * Time: 2018/7/21.
 * Desc:
 */

public class TimeUtil {
    public static final long THRESHOLD = 16 * 60 * 60 * 1000;
    private static SimpleDateFormat mTimeFormat = new SimpleDateFormat("HH:mm:ss");

    public static String formateCostTime(long costTime) {
        return mTimeFormat.format(new Date(costTime + THRESHOLD));
    }

    public static String formatDownTime(long downCountTime) {
        return mTimeFormat.format(new Date(downCountTime));
    }

    public static String formateCostTime(int hourOfDay, int minute) {
        int mCurrentHours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int mCurrentMinutes = Calendar.getInstance().get(Calendar.MINUTE);
        int time = ((hourOfDay + 16 - mCurrentHours) * 60 + (minute - mCurrentMinutes)) * 60 * 1000;
        return mTimeFormat.format(new Date(time));
    }

    public static long getPureDownCoutTime(long mixTime) {
        return mixTime - THRESHOLD;
    }


}
