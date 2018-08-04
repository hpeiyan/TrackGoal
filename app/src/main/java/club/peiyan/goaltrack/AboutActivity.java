package club.peiyan.goaltrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

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
}
