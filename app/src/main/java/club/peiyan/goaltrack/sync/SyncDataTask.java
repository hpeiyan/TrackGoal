package club.peiyan.goaltrack.sync;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import club.peiyan.goaltrack.utils.HttpClientUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by HPY.
 * Time: 2018/7/10.
 * Desc:
 */

public class SyncDataTask implements Runnable {

    private static final String TAG = "SyncDataTask";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static String url = "http://120.79.79.63:8080/login";

    @Override
    public void run() {
        String json = createJson();
        Log.e(TAG, "json: " + json);
        Request.Builder mBuilder = new Request.Builder();
        RequestBody mBody = RequestBody.create(JSON, json);
        mBuilder.post(mBody);
        mBuilder.url(url);
        Request mRequest = mBuilder.build();

        OkHttpClient mClient = HttpClientUtil.getClient();
        mClient.newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "onResponse: " + response.body().string());
            }
        });
    }

    private String createJson() {
        JSONObject mJSONObject = new JSONObject();
        try {
            mJSONObject.put("email", "huang@gmail.com");
            mJSONObject.put("password", "huancom");

        } catch (JSONException mE) {
            mE.printStackTrace();
        }
        return mJSONObject.toString();
    }
}
