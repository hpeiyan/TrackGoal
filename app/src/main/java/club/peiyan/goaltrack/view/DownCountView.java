package club.peiyan.goaltrack.view;

import android.app.TimePickerDialog;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.peiyan.goaltrack.MainActivity;
import club.peiyan.goaltrack.R;
import club.peiyan.goaltrack.data.GoalBean;
import club.peiyan.goaltrack.data.ScoreBean;
import club.peiyan.goaltrack.utils.CalendaUtils;
import club.peiyan.goaltrack.utils.DialogUtil;
import club.peiyan.goaltrack.utils.ListUtil;
import club.peiyan.goaltrack.utils.TimeUtil;

/**
 * Created by HPY.
 * Time: 2018/7/21.
 * Desc:
 */

public class DownCountView {

    private final View mRootView;

    private final MainActivity mActivity;
    @BindView(R.id.tvCostTime)
    TextView mTvCostTime;
    @BindView(R.id.rlCost)
    RelativeLayout mRlCost;
    @BindView(R.id.ivTimeCount)
    ImageView mIvTimeCount;
    @BindView(R.id.tvRemainTime)
    TextView mTvRemainTime;
    @BindView(R.id.ivClear)
    ImageView mIvClear;
    private CountDownTimer mDownTimer;
    private SimpleDateFormat mTimeFormat = new SimpleDateFormat("HH:mm:ss");
    private long mMillisUntilFinished;
    private long mCostTimeMills;
    private final GoalBean mGoalBean;


    public DownCountView(MainActivity mMainActivity, GoalBean mBean) {
        mActivity = mMainActivity;
        mGoalBean = mBean;

        mRootView = View.inflate(mMainActivity, R.layout.layout_downcount, null);
        ButterKnife.bind(this, mRootView);

        initCostTime();
        initView();
    }

    private void initCostTime() {
        if (mGoalBean != null) {
            ArrayList<ScoreBean> mBeanArrayList = mActivity.getDBHelper()
                    .getScoreToday(mGoalBean.getTitle(), mGoalBean.getParent());
            if (!ListUtil.isEmpty(mBeanArrayList)) {
                ScoreBean mScoreBean = mBeanArrayList.get(0);
                if (mScoreBean != null) {
                    String mCostTime = TimeUtil.formateCostTime(mScoreBean.getScore());
                    mTvCostTime.setText(mCostTime);
                }
            }
        }
    }

    private void initView() {
        mIvTimeCount.setTag(R.id.time_count_tag, 2);
        mTvRemainTime.setOnClickListener((v -> {
            if ((Integer) mIvTimeCount.getTag(R.id.time_count_tag) == 2) {
                showTimePickerDialog();
            }
        }));

        mIvTimeCount.setOnClickListener((v -> {
            if (mMillisUntilFinished > 0) {
                Integer mTag = (Integer) mIvTimeCount.getTag(R.id.time_count_tag);
                if (mTag == 1) {
                    mDownTimer.cancel();
                    mIvTimeCount.setImageDrawable(mActivity.getResources().getDrawable(R.mipmap.ic_play_circle_outline_black_24dp));
                    mIvTimeCount.setTag(R.id.time_count_tag, 3);//pause
                } else {
                    downCountTime(mMillisUntilFinished);
                    mIvTimeCount.setImageDrawable(mActivity.getResources().getDrawable(R.mipmap.ic_pause_circle_outline_black_24dp));
                    mIvTimeCount.setTag(R.id.time_count_tag, 1);//play
                }
            } else {
                showTimePickerDialog();
            }
        }));

        mIvClear.setOnClickListener((v -> {
            DialogUtil.showSingleDialog(mActivity, null, "取消计时任务吗？", "继续计时", "我要退出", new DialogUtil.DialogListener() {
                @Override
                public void onNegClickListener() {

                }

                @Override
                public void onPosClickListener() {
                    if (mDownTimer != null) {
                        mDownTimer.cancel();
                        mDownTimer.onFinish();
                    }
                }
            });
        }));
    }

    public View getRootView() {
        return mRootView;
    }

    private void showTimePickerDialog() {
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        TimePickerDialog mDialog = new TimePickerDialog(mActivity, (view, hourOfDay, minute1) -> {
            int mCurrentHours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int mCurrentMinutes = Calendar.getInstance().get(Calendar.MINUTE);
            int time = ((hourOfDay + 16 - mCurrentHours) * 60 + (minute1 - mCurrentMinutes)) * 60 * 1000;
            downCountTime(time);
        }, hour, minute, true);
        mDialog.show();
    }

    private void downCountTime(long mTime) {
        mDownTimer = new CountDownTimer(mTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mMillisUntilFinished = millisUntilFinished;
                mCostTimeMills = mTime - millisUntilFinished;
                mTvRemainTime.setText(mTimeFormat.format(new Date(millisUntilFinished)));
                mIvTimeCount.setImageDrawable(mActivity.getResources().getDrawable(R.mipmap.ic_pause_circle_outline_black_24dp));
                mIvTimeCount.setTag(R.id.time_count_tag, 1);//play
            }

            @Override
            public void onFinish() {
                mTvRemainTime.setText(mActivity.getString(R.string.start_downcount));
                mIvTimeCount.setImageDrawable(mActivity.getResources().getDrawable(R.mipmap.ic_timer_black_24dp));
                mIvTimeCount.setTag(R.id.time_count_tag, 2);//finish
                mMillisUntilFinished = 0;
                mIvClear.setVisibility(View.GONE);
//                if (mCostTimeMills > 15 * 60 * 1000) {
                if (mCostTimeMills > 15) {
                    // TODO: 2018/7/21 后续改成15分钟
                    if (mGoalBean != null) {
                        mActivity.getDBHelper().insertScore(mGoalBean.getLevel(),
                                mGoalBean.getParent(), mGoalBean.getTitle(),
                                CalendaUtils.getCurrntDate(), mCostTimeMills, System.currentTimeMillis());
                        mActivity.getDBHelper().updateScore(mGoalBean.getLevel(), mGoalBean.getParent(),
                                mGoalBean.getTitle(), CalendaUtils.getCurrntDate(), mCostTimeMills, System.currentTimeMillis());
                        mActivity.notifyDataSetChange(null);
                    }
                }
            }
        }.start();
        mIvTimeCount.setImageDrawable(mActivity.getResources().getDrawable(R.mipmap.ic_pause_circle_outline_black_24dp));
        mIvTimeCount.setTag(R.id.time_count_tag, 1);//play
        mIvClear.setVisibility(View.VISIBLE);
    }
}