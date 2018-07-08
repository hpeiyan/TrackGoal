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
}
