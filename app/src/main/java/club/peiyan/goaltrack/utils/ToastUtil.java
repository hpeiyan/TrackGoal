package club.peiyan.goaltrack.utils;

import android.widget.Toast;

import club.peiyan.goaltrack.GoalApplication;

/**
 * Created by HPY.
 * Time: 2018/7/13.
 * Desc:
 */

public class ToastUtil {

    public static void toast(final String message) {
        ThreadUtil.uiPost(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GoalApplication.getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
