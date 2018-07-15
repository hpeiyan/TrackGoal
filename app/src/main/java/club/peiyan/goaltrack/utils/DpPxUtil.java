package club.peiyan.goaltrack.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import club.peiyan.goaltrack.GoalApplication;

/**
 * Created by HPY.
 * Time: 2018/7/14.
 * Desc:
 */

public class DpPxUtil {

    public static float dp2px(float dp){
        Resources resources = GoalApplication.getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static float px2dp(float px){
        Resources resources = GoalApplication.getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
}
