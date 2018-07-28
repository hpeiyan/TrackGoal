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

    public static void setVisible(View mView) {
        if (mView.getVisibility() != View.VISIBLE)
            mView.setVisibility(View.VISIBLE);
    }

    public static void setGone(View mView) {
        if (mView.getVisibility() != View.GONE || mView.getVisibility() != View.INVISIBLE)
            mView.setVisibility(View.GONE);
    }
}
