package club.peiyan.goaltrack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.base.bj.paysdk.domain.TrPayResult;
import com.base.bj.paysdk.listener.PayResultListener;
import com.base.bj.paysdk.utils.TrPay;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import club.peiyan.goaltrack.utils.ChannelUtil;
import club.peiyan.goaltrack.utils.ToastUtil;

/**
 * Created by HPY.
 * Time: 2018/7/16.
 * Desc:
 */

public class AboutActivity extends BaseActivity {


    public static void startAboutActivity(MainActivity mMainActivity) {
        Intent mIntent = new Intent(mMainActivity, AboutActivity.class);
        mMainActivity.startActivity(mIntent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Goal Track");

        TrPay.getInstance(this).
                initPaySdk("d4e21e86ad2f4d1fbaa7e05a4ca3bd48",
                        ChannelUtil.getAppMetaData(getBaseContext(), "UMENG_CHANNEL"));
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void payV2(View mView) {
        ToastUtil.toast("pay");
        double mOrder = Math.random() * 1000;
        TrPay.getInstance(this).callPay("打赏", String.valueOf(mOrder), 1l, null, null, "goaltrack@gmail.com", new PayResultListener() {
            @Override
            public void onPayFinish(Context mContext, String mS, int resultCode, String mS1, int mI1, Long mLong, String mS2) {
                if (resultCode == TrPayResult.RESULT_CODE_SUCC.getId()) {
                    //支付成功逻辑处理
                    ToastUtil.toast("支付失败");
                } else if (resultCode == TrPayResult.RESULT_CODE_FAIL.getId()) {
                    //支付失败逻辑处理
                    ToastUtil.toast("支付成功");
                    TrPay.getInstance(AboutActivity.this).closePayView();
                }
            }
        });
    }

}
