package club.peiyan.goaltrack;

import android.app.Application;
import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

/**
 * Created by HPY.
 * Time: 2018/7/8.
 * Desc:
 */

public class GoalApplication extends Application {

    private static Context mContext;
    private static final String APP_KEY = "5b51f571f43e48294800022f";
    private static final String APP_CHANNEL = "test";
    private static final String APP_PUSH = "dbbaa0640d5b4c423cbb74bee04a2b8e";

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        CrashReport.initCrashReport(getApplicationContext());
        initUmengAnalytistic();
    }

    private void initUmengAnalytistic() {
        UMConfigure.init(getApplicationContext(), APP_KEY, APP_CHANNEL, UMConfigure.DEVICE_TYPE_PHONE, APP_PUSH);
        MobclickAgent.setScenarioType(mContext, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setCatchUncaughtExceptions(false);
    }

    public static Context getContext() {
        return mContext;
    }
}
