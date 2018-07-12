package club.peiyan.goaltrack.sync;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import club.peiyan.goaltrack.MainActivity;
import club.peiyan.goaltrack.data.SyncBean;
import club.peiyan.goaltrack.data.DBHelper;
import club.peiyan.goaltrack.data.GoalBean;
import club.peiyan.goaltrack.utils.HttpClientUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static club.peiyan.goaltrack.data.Constants.HOST;

/**
 * Created by HPY.
 * Time: 2018/7/10.
 * Desc:
 */

public class SyncDataTask implements Runnable {

    private static final String TAG = "SyncDataTask";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static String url = HOST + "api/sync";
    private ArrayList<GoalBean> mGoalBeans;
    private MainActivity mMainActivity;
    private final DBHelper mDBHelper;

    public SyncDataTask(MainActivity mMainActivity) {
        this.mMainActivity = mMainActivity;
        mDBHelper = mMainActivity.getDBHelper();
    }

    @Override
    public void run() {
        String json = createJson();
        Log.e(TAG, "json: " + json);
        Request.Builder mBuilder = new Request.Builder();
        RequestBody mBody = RequestBody.create(JSON, json);
        mBuilder.post(mBody);
        mBuilder.url(url);
        Request mRequest = mBuilder.build();

        final OkHttpClient mClient = HttpClientUtil.getClient();
        mClient.newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String mResult = response.body().string();
                Log.i(TAG, "onResponse: " + mResult);
                Gson mGson = new Gson();
                SyncBean mSyncBean = mGson.fromJson(mResult, SyncBean.class);
                int mCode = mSyncBean.getCode();
                if (mCode == 200) {
                    List<GoalBean> mGoalBeans = mSyncBean.getData();
                    if (mGoalBeans != null && mGoalBeans.size() > 0) {
                        for (GoalBean mBean : mGoalBeans) {
                            mDBHelper.deleteGoal(mBean.getTitle());
                            mDBHelper.insertGoal(mBean);
                        }
                        mMainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mMainActivity.notifyDataSetChange(null);
                                Toast.makeText(mMainActivity, "更新成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    private String createJson() {
        JSONArray mJSONArray = new JSONArray();
        JSONObject mJSONObject = null;
        for (GoalBean bean : mGoalBeans) {
            mJSONObject = new JSONObject();
            try {
                mJSONObject.put("id", bean.getId());
                mJSONObject.put("title", bean.getTitle());
                mJSONObject.put("level", bean.getLevel());
                mJSONObject.put("parent", bean.getParent());
                mJSONObject.put("items", bean.getItems());
                mJSONObject.put("start", bean.getStart());
                mJSONObject.put("over", bean.getOver());
                mJSONObject.put("timestamp", bean.getTimestamp());
                mJSONObject.put("status", bean.getStatus());
                mJSONObject.put("user_id", bean.getUser_id());
            } catch (JSONException mE) {
                mE.printStackTrace();
            }
            mJSONArray.put(mJSONObject);
        }
        return mJSONArray.toString();
    }

    public void setSyncData(ArrayList<GoalBean> mAllGoals) {
        mGoalBeans = mAllGoals;
    }
}
