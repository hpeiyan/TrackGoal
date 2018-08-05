package club.peiyan.goaltrack.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

/**
 * Created by HPY.
 * Time: 2018/8/5.
 * Desc:
 */

public class ChannelUtil {
    /**
     * 获取app当前的渠道号或application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context context, String key) {
        if (context == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String channelNumber = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelNumber = applicationInfo.metaData.getString(key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelNumber;
    }
}
