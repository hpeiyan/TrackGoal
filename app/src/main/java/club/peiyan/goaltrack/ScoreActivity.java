package club.peiyan.goaltrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.ButterKnife;
import club.peiyan.goaltrack.data.ScoreList;
import club.peiyan.goaltrack.plan.ScoreFragment;
import club.peiyan.goaltrack.utils.LogUtil;

/**
 * Created by HPY.
 * Time: 2018/7/16.
 * Desc:
 */

public class ScoreActivity extends BaseActivity {


    private ArrayList<ScoreList> mListExtra;

    public static void startScoreActivity(MainActivity mMainActivity, ArrayList<ScoreList> mPastScoreList) {
        Intent mIntent = new Intent(mMainActivity, ScoreActivity.class);
        mIntent.putParcelableArrayListExtra("data", mPastScoreList);
        mMainActivity.startActivity(mIntent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_layout);
        Intent mIntent = getIntent();
        mListExtra = mIntent.getParcelableArrayListExtra("data");
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        if (savedInstanceState != null) return;
        getSupportFragmentManager().beginTransaction().add(R.id.flScore, new ScoreFragment()).commit();
    }

    public ArrayList<ScoreList> getListExtra() {
        return mListExtra;
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("test",1);
        LogUtil.logi("onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int mInt = savedInstanceState.getInt("test");
        LogUtil.logi("onRestoreInstanceState: "+mInt);
    }

    //
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT
//                || newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//
//        }
//
//    }
}
