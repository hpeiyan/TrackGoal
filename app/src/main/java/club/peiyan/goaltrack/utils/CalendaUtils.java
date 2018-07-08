package club.peiyan.goaltrack.utils;

import java.util.Calendar;

/**
 * Created by HPY.
 * Time: 2018/7/8.
 * Desc:
 */

public class CalendaUtils {
    public static String getCurrntDate() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DATE);
        return year + "/" + month + "/" + day;
    }

    public static String getYesterday() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.DATE, -1);
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DATE);
        return year + "/" + month + "/" + day;
    }

}
