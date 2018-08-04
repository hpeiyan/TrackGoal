package club.peiyan.goaltrack.data;

import club.peiyan.goaltrack.utils.AppSp;

/**
 * Created by HPY.
 * Time: 2018/7/8.
 * Desc:
 */

public class Constants {
    public static final String LATEST_GOAL = "LATEST_GOAL";
    public static final String HOST = "http://120.79.79.63:8080/";
    public static final String USER_NAME = "username";

    public static final String REGISTER = "register";
    /*Setting Config */
    public static final String SCORE_SHOW_DAY = "SCORE_SHOW_DAY";

    /**
     * 分数显示的天数
     *
     * @return 默认31天
     */
    public static int getScoreShowDay() {
        return AppSp.getInt(Constants.SCORE_SHOW_DAY, 31);
    }

    public static void setScoreShowDay(int days) {
        AppSp.putInt(Constants.SCORE_SHOW_DAY, days);
    }
}
