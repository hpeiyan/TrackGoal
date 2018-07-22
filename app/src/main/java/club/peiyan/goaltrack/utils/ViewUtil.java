package club.peiyan.goaltrack.utils;

import android.view.View;

/**
 * Created by HPY.
 * Time: 2018/7/22.
 * Desc:
 */

public class ViewUtil {
    public static boolean isVisible(View mView) {
        return mView.getVisibility() == View.VISIBLE ? true : false;
    }
}
