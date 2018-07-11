package club.peiyan.goaltrack.utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by HPY.
 * Time: 2018/7/10.
 * Desc:
 */

public class HttpClientUtil {

    private static OkHttpClient mOkHttpClient;

    private HttpClientUtil() {
        createClient();
    }

    private static void createClient() {
        OkHttpClient.Builder mBuilder = new OkHttpClient().newBuilder();
        mBuilder.connectTimeout(20, TimeUnit.SECONDS);
        mBuilder.readTimeout(20, TimeUnit.SECONDS);
        mBuilder.writeTimeout(20, TimeUnit.SECONDS);
        mOkHttpClient = mBuilder.build();
    }

    public static OkHttpClient getClient() {
        if (mOkHttpClient == null) createClient();
        return mOkHttpClient;
    }
}
