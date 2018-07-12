package club.peiyan.goaltrack.netTask;

import com.google.gson.Gson;

import java.io.IOException;

import club.peiyan.goaltrack.data.RegisterBean;
import club.peiyan.goaltrack.utils.AppSp;
import club.peiyan.goaltrack.utils.HttpClientUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import static club.peiyan.goaltrack.data.Constants.HOST;
import static club.peiyan.goaltrack.data.Constants.USER_ID;

/**
 * Created by HPY.
 * Time: 2018/7/12.
 * Desc:
 */

public class RegisterTask implements Runnable {
    private String url = HOST + "api/register";

    @Override
    public void run() {
        Request mRequest = new Request.Builder().url(url).get().build();
        HttpClientUtil.getClient().newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String mResult = response.body().string();
                Gson mGson = new Gson();
                RegisterBean mBean = mGson.fromJson(mResult, RegisterBean.class);
                if (mBean.getCode() == 200) {
                    String mId = mBean.getData().getId();
                    AppSp.putString(USER_ID, mId);
                }
            }
        });
    }
}
