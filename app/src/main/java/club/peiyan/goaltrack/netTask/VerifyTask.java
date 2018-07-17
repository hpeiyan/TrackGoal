package club.peiyan.goaltrack.netTask;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import club.peiyan.goaltrack.data.Constants;
import club.peiyan.goaltrack.data.VerifyBean;
import club.peiyan.goaltrack.utils.AppSp;
import club.peiyan.goaltrack.utils.ToastUtil;
import okhttp3.Authenticator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

import static club.peiyan.goaltrack.data.Constants.HOST;

/**
 * Created by HPY.
 * Time: 2018/7/12.
 * Desc:
 */

public class VerifyTask implements Runnable {
    private static final String TAG = "VerifyTask";
    private String url = HOST + "api/resource";
    private String userName;
    private String password;
    private final Activity mActivity;
    private OnVerifyListener mVerifyListener;

    public VerifyTask(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void run() {

        OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
        OkHttpClient mClient = mBuilder.authenticator(new Authenticator() {
            @Nullable
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                return response.request().newBuilder().
                        header("Authorization", Credentials.basic(userName, password)).build();
            }
        }).build();

        Request mRequest = new Request.Builder().url(url).get().build();
        mClient.newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mVerifyListener.onVerifyFail();
                ToastUtil.toast("登录失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "onResponse: " + response.code());
                String mResult = response.body().string();
                Gson mGson = new Gson();
                VerifyBean mBean = mGson.fromJson(mResult, VerifyBean.class);
                if (mBean !=null && mBean.getCode() == 200) {
                    mVerifyListener.onVerifySuccess();
                    AppSp.putString(Constants.USER_NAME, userName);
                    ToastUtil.toast("登录成功");
                }else {
                    mVerifyListener.onVerifyFail();
                    ToastUtil.toast("登录失败");
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

    public void setOnVerifyListener(OnVerifyListener mListener) {
        mVerifyListener = mListener;
    }

    public interface OnVerifyListener {
        void onVerifySuccess();

        void onVerifyFail();
    }
}
