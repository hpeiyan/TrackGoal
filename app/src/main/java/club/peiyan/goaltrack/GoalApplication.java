package club.peiyan.goaltrack;

import android.app.Application;
import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;

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
        CrashReport.initCrashReport(getApplicationContext());
    }

    public static Context getContext() {
        return mContext;
    }
}
