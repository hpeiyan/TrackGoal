package club.peiyan.goaltrack;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.peiyan.goaltrack.data.Constants;
import club.peiyan.goaltrack.data.DBHelper;
import club.peiyan.goaltrack.utils.AppSp;
import club.peiyan.goaltrack.utils.DialogUtil;
import club.peiyan.goaltrack.utils.LogUtil;

/**
 * Created by HPY.
 * Time: 2018/7/16.
 * Desc:
 */

public class SettingActivity extends BaseActivity {


    public static final int SETTING_REQUEST_CODE = 10086;
    @BindView(R.id.tvFinishTry)
    TextView mTvFinishTry;
    @BindView(R.id.swFinish)
    Switch mSwFinish;
    @BindView(R.id.edtDays)
    EditText mEtScoreDays;
    @BindView(R.id.swAddGoal)
    Switch mSwAddGoal;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.swScreenOn)
    Switch mSwScreenOn;
    @BindView(R.id.rlUser)
    RelativeLayout mRlUser;
    @BindView(R.id.swVibratorOn)
    Switch mSwVibratorOn;
    @BindView(R.id.ivUser)
    ImageView mIvUser;

    public static void startSettingActivity(MainActivity mMainActivity) {
        Intent mIntent = new Intent(mMainActivity, SettingActivity.class);
        mMainActivity.startActivityForResult(mIntent, SETTING_REQUEST_CODE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
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

        mSwVibratorOn.setChecked(AppSp.getBoolean(Constants.VIBRATOR_ON, true));
        mSwVibratorOn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            AppSp.putBoolean(Constants.VIBRATOR_ON, isChecked);
        });

        mSwAddGoal.setChecked(AppSp.getBoolean(Constants.SHOW_ADD, true));
        mSwAddGoal.setOnCheckedChangeListener((buttonView, isChecked) -> {
            AppSp.putBoolean(Constants.SHOW_ADD, isChecked);
        });

        mSwScreenOn.setChecked(AppSp.getBoolean(Constants.SCREEN_ON, true));
        mSwScreenOn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            AppSp.putBoolean(Constants.SCREEN_ON, isChecked);
        });

        mRlUser.setOnClickListener(v -> {
            if (AppSp.getBoolean(Constants.IS_REGISTER, false)) {
                View mView = getLayoutInflater().inflate(R.layout.layout_quite_login, null);
                AlertDialog mDialog = DialogUtil.showDialogWithViewWithoutListener(this, mView);
                mView.findViewById(R.id.tvQuite).setOnClickListener(v1 -> {
                    new DBHelper(SettingActivity.this).deleteAllGoal();
                    AppSp.clear(new String[]{Constants.IS_REGISTER, Constants.USER_NAME});
                    File mDBFile = getDatabasePath("UserTrackGoal.db");
                    mDBFile.delete();
                    ReLoginActivity.startReLoginActivityForce(SettingActivity.this);
                    if (mDialog != null) mDialog.dismiss();
                });
                mView.findViewById(R.id.tvDismiss).setOnClickListener(v1 -> {
                    if (mDialog != null) mDialog.dismiss();
                });
            } else {
                View mView = getLayoutInflater().inflate(R.layout.layout_login_apply, null);
                AlertDialog mDialog = DialogUtil.showDialogWithViewWithoutListener(this, mView);
                mView.findViewById(R.id.tvLogin).setOnClickListener(v1 -> {
                    ReLoginActivity.startReLoginActivityForce(SettingActivity.this);
                    if (mDialog != null) mDialog.dismiss();
                });
                mView.findViewById(R.id.tvApply).setOnClickListener(v1 -> {
                    FeedBackActivity.startFeedbackActivity(SettingActivity.this);
                    if (mDialog != null) mDialog.dismiss();
                });
                mView.findViewById(R.id.tvDismiss).setOnClickListener(v1 -> {
                    if (mDialog != null) mDialog.dismiss();
                });
            }
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
