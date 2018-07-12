package club.peiyan.goaltrack;

import android.app.Application;
import android.content.Context;

import club.peiyan.goaltrack.netTask.RegisterTask;
import club.peiyan.goaltrack.utils.AppSp;

import static club.peiyan.goaltrack.data.Constants.USER_ID;

/**
 * Created by HPY.
 * Time: 2018/7/8.
 * Desc:
 */

public class GoalApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        if (AppSp.getString(USER_ID, "").isEmpty()) {
            new Thread(new RegisterTask()).start();
        }
    }

    public static Context getContext() {
        return mContext;
    }
}
