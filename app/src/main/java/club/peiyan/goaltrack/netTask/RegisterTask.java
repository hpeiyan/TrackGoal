package club.peiyan.goaltrack.netTask;

import android.app.Activity;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import club.peiyan.goaltrack.data.RegisterBean;
import club.peiyan.goaltrack.utils.AppSp;
import club.peiyan.goaltrack.utils.HttpClientUtil;
import club.peiyan.goaltrack.utils.ToastUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static club.peiyan.goaltrack.data.Constants.HOST;
import static club.peiyan.goaltrack.data.Constants.USER_NAME;

/**
 * Created by HPY.
 * Time: 2018/7/12.
 * Desc:
 */

public class RegisterTask implements Runnable {
    private static final String TAG = "RegisterTask";
    private String url = HOST + "api/users";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private String userName;
    private String password;
    private final Activity mActivity;
    private OnRegisterListener mRegisterListener;

    public RegisterTask(Activity mActivity) {
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
                mRegisterListener.onRegisterFail();
                ToastUtil.toast("注册失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String mResult = response.body().string();
                Gson mGson = new Gson();
                RegisterBean mBean = mGson.fromJson(mResult, RegisterBean.class);
                if (mBean != null && mBean.getCode() == 200) {
                    AppSp.putString(USER_NAME, mBean.getData().getUsername());
                    mRegisterListener.onRegisterSuccess();
                    ToastUtil.toast("注册成功");
                } else {
                    mRegisterListener.onRegisterFail();
                    ToastUtil.toast(mBean.getMsg());
                }
            }
        });
    }

    public void setUserName(String mUserName) {
        userName = mUserName;
    }

    public void setPassword(String mPassword) {
        password = mPassword;
    }

    private String createJson() {
        JSONObject mJSONObject = new JSONObject();
        try {
            mJSONObject.put("username", userName);
            mJSONObject.put("password", password);
        } catch (JSONException mE) {
            mE.printStackTrace();
        }
        return mJSONObject.toString();
    }

    public void setRegisterListener(OnRegisterListener mListener) {
        mRegisterListener = mListener;
    }

    public interface OnRegisterListener {
        void onRegisterSuccess();

        void onRegisterFail();
    }
}
