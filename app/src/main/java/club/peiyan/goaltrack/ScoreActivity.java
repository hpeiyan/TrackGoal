package club.peiyan.goaltrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.ButterKnife;
import club.peiyan.goaltrack.data.ScoreList;
import club.peiyan.goaltrack.plan.ScoreFragment;

/**
 * Created by HPY.
 * Time: 2018/7/16.
 * Desc:
 */

public class ScoreActivity extends AppCompatActivity {


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
//        getSupportActionBar().setTitle("分数");
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
}
