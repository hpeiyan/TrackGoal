package club.peiyan.goaltrack;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.peiyan.goaltrack.data.Constants;
import club.peiyan.goaltrack.utils.AppSp;
import club.peiyan.goaltrack.utils.LogUtil;

/**
 * Created by HPY.
 * Time: 2018/7/16.
 * Desc:
 */

public class SettingActivity extends BaseActivity {


    @BindView(R.id.tvFinishTry)
    TextView mTvFinishTry;
    @BindView(R.id.swFinish)
    Switch mSwFinish;
    @BindView(R.id.edtDays)
    EditText mEtScoreDays;
    @BindView(R.id.swAddGoal)
    Switch mSwAddGoal;

    public static void startSettingActivity(MainActivity mMainActivity) {
        Intent mIntent = new Intent(mMainActivity, SettingActivity.class);
        mMainActivity.startActivity(mIntent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("设置");
        initView();
    }

    private void initView() {
        mTvFinishTry.setOnClickListener(v -> {
            MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alarm);
            mPlayer.start();
        });

        mSwFinish.setChecked(AppSp.getBoolean(Constants.ALARM_ON, true));
        mSwFinish.setOnCheckedChangeListener((buttonView, isChecked) -> {
            AppSp.putBoolean(Constants.ALARM_ON, isChecked);
        });

        mSwAddGoal.setChecked(AppSp.getBoolean(Constants.SHOW_ADD, false));
        mSwAddGoal.setOnCheckedChangeListener((buttonView, isChecked) -> {
            AppSp.putBoolean(Constants.SHOW_ADD, isChecked);
        });

        mEtScoreDays.setText(Constants.getScoreShowDay() + "");
        mEtScoreDays.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String mDays = s.toString().trim();
                if (mDays.length() > 0) {
                    LogUtil.logi(mDays);
                    Constants.setScoreShowDay(Integer.parseInt(mDays));
                }
            }
        });
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
