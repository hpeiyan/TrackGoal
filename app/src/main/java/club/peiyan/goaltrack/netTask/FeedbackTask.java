package club.peiyan.goaltrack.netTask;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import club.peiyan.goaltrack.utils.CalendaUtils;
import club.peiyan.goaltrack.utils.HttpClientUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static club.peiyan.goaltrack.data.Constants.HOST;

/**
 * Created by HPY.
 * Time: 2018/7/12.
 * Desc:
 */

public class FeedbackTask implements Runnable {
    private static final String TAG = "FeedbackTask";
    private String url = HOST + "api/feedback";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private final Activity mActivity;
    private OnFeedbackListener mRegisterListener;

    private String userName;
    private String note;
    private String mEmail;

    public FeedbackTask(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void run() {
        String json = createJson();
        RequestBody mBody = RequestBody.create(JSON, json);
        Request mRequest = new Request.Builder().url(url).post(mBody).build();
        HttpClientUtil.getClient().newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mRegisterListener.onFeedbackFail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "onResponse: " + response.code());
                String mResult = response.body().string();
                try {
                    JSONObject mJSONObject = new JSONObject(mResult);
                    if (mJSONObject.getInt("code") == 200) {
                        mRegisterListener.onFeedbackSuccess();
                        mActivity.runOnUiThread(() -> Toast.makeText(mActivity, "再次感谢您的建议！", Toast.LENGTH_SHORT).show());
                    }
                } catch (JSONException mE) {
                    mE.printStackTrace();
                }
            }
        });
    }

    public void setUserName(String mUserName) {
        userName = mUserName;
    }

    public void setNote(String mNote) {
        note = mNote;
    }

    private String createJson() {
        JSONObject mJSONObject = new JSONObject();
        try {
            mJSONObject.put("username", userName);
            mJSONObject.put("note", note);
            mJSONObject.put("email", mEmail);
            mJSONObject.put("date", CalendaUtils.getCurrntDate());
        } catch (JSONException mE) {
            mE.printStackTrace();
        }
        return mJSONObject.toString();
    }

    public void setRegisterListener(OnFeedbackListener mListener) {
        mRegisterListener = mListener;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public interface OnFeedbackListener {
        void onFeedbackSuccess();

        void onFeedbackFail();
    }
}
