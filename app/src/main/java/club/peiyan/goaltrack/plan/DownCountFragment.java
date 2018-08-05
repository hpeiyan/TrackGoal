package club.peiyan.goaltrack.plan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.UnitPosition;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import club.peiyan.goaltrack.DownCountService;
import club.peiyan.goaltrack.MainActivity;
import club.peiyan.goaltrack.R;
import club.peiyan.goaltrack.event.PauseEvent;
import club.peiyan.goaltrack.listener.DownCountListener;
import club.peiyan.goaltrack.utils.CalendarUtils;
import club.peiyan.goaltrack.utils.DialogUtil;
import club.peiyan.goaltrack.utils.TimeUtil;
import club.peiyan.goaltrack.utils.ThreadUtil;

import static club.peiyan.goaltrack.DownCountService.COUNT_FINISH;
import static club.peiyan.goaltrack.DownCountService.COUNT_STOP;
import static club.peiyan.goaltrack.DownCountService.DOWN_COUNT;
import static club.peiyan.goaltrack.DownCountService.DOWN_COUNT_TAG;

/**
 * Created by HPY.
 * Time: 2018/7/8.
 * Desc:
 */

public class DownCountFragment extends Fragment implements DownCountListener {

    Unbinder unbinder;
    @BindView(R.id.tvDownCount)
    TextView mTvDownCount;
    @BindView(R.id.ivPausePlay)
    ImageView mIvPausePlay;
    @BindView(R.id.ivClose)
    ImageView mIvClose;
    @BindView(R.id.rlDownCount)
    RelativeLayout mRlDownCount;
    @BindView(R.id.cpv)
    CircleProgressView mCircleView;
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    private MainActivity mMainActivity;
    private String[] mTags;
    private long mCount;
    private long mOrigin;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivity = (MainActivity) getActivity();
        mMainActivity.setDownCountListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_down_count, null);
        unbinder = ButterKnife.bind(this, mView);
        initView();
        return mView;
    }

    private void initView() {
        mCircleView.setShowTextWhileSpinning(true); // Show/hide text in spinning mode
        mCircleView.setUnitPosition(UnitPosition.RIGHT_TOP);
        mCircleView.setUnit("%");
        mCircleView.setUnitVisible(true);
        mCircleView.setUnitScale(0.9f);
        mCircleView.setTextScale(0.9f);
        mCircleView.setTextColorAuto(true);
        mCircleView.setAutoTextSize(true);
        mCircleView.setFillCircleColor(getResources().getColor(R.color.colorPrimaryLight));
        mCircleView.setRimColor(getResources().getColor(R.color.md_grey_700));
        mCircleView.setBarColor(getResources().getColor(R.color.colorPrimaryDark));
        mCircleView.setInnerContourColor(getResources().getColor(R.color.md_grey_700));
        mCircleView.setOuterContourColor(getResources().getColor(R.color.md_grey_700));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("DownCountFragment"); //统计页面("MainScreen"为页面名称，可自定义)
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("DownCountFragment");
    }

    @Override
    public void onTick(long count, long countOrigin, String[] tags) {
        if (mOrigin == 0 || mOrigin < countOrigin) {
            mOrigin = countOrigin;
        }
        String mTitle = "";
        if (tags != null && tags.length == 3) {
            mTitle = tags[0];
        }
        mIvPausePlay.setImageDrawable(getResources().getDrawable(R.mipmap.ic_pause_circle_outline_white_24dp));
        mTvTitle.setText(mTitle);
        String mCountTime = "<big><big><big>" + TimeUtil.formatDownTime(count) + "</big></big></big>";
        mTvDownCount.setText(Html.fromHtml(mCountTime));
        int mProgress = (int) ((100 * (mOrigin - count) / (mOrigin - TimeUtil.THRESHOLD)));
        mCircleView.setValueAnimated(mProgress, 100);
    }


    @Override
    public void onFinish(boolean isFinish, String[] tags) {
        if (isFinish) {
            if (tags != null
                    && tags.length == 3) {
//                if (mCostTimeMills > 15 * 60 * 1000) {
                if (mMainActivity.getCostTimeMills() > 15) {
                    // TODO: 2018/7/21 后续改成15分钟
                    mMainActivity.getDBHelper().updateScore(Integer.parseInt(tags[2]), tags[1], tags[0], CalendarUtils.getCurrntDate(),
                            mMainActivity.getCostTimeMills(), System.currentTimeMillis());
                }
            }
            mMainActivity.notifyDataSetChange(null);
            mIvPausePlay.setImageDrawable(getResources().getDrawable(R.mipmap.ic_pause_circle_outline_white_24dp));
            isPause = false;
            mOrigin = 0;
        }
    }

    private boolean isPause = false;// now status

    @OnClick({R.id.ivPausePlay, R.id.ivClose})
    public void onViewClicked(View view) {
        mTags = mMainActivity.getTags();
        mCount = mMainActivity.getCount();
        switch (view.getId()) {
            case R.id.ivPausePlay:
                if (isPause) {
                    mIvPausePlay.setImageDrawable(getResources().getDrawable(R.mipmap.ic_pause_circle_outline_white_24dp));
//                    continueDownCount();
                    mMainActivity.continueDownCount();
                } else {
                    mIvPausePlay.setImageDrawable(getResources().getDrawable(R.mipmap.ic_play_circle_outline_white_24dp));
//                    pauseDownCount();
                    mMainActivity.pauseDownCount();
                    EventBus.getDefault().post(new PauseEvent());
                    ThreadUtil.uiPostDelay(() -> mMainActivity.notifyDataSetChange(null), 200);
                }
                isPause = !isPause;
                break;
            case R.id.ivClose:
                DialogUtil.showSingleDialog(mMainActivity, null, "取消计时任务吗？", "继续计时", "我要取消", new DialogUtil.DialogListener() {
                    @Override
                    public void onNegClickListener() {

                    }

                    @Override
                    public void onPosClickListener() {
//                        finishDownCount();
                        mMainActivity.finishDownCount();
                    }
                });
                break;
        }
    }

    private void pauseDownCount() {
        Intent mIntent = new Intent(mMainActivity, DownCountService.class);
        mIntent.putExtra(COUNT_STOP, true);
        mIntent.putExtra(DOWN_COUNT_TAG, new String[]{mTags[0],
                mTags[1],
                String.valueOf(mTags[2])});
        mMainActivity.startService(mIntent);
    }

    private void finishDownCount() {
        Intent mIntent = new Intent(mMainActivity, DownCountService.class);
        mIntent.putExtra(COUNT_FINISH, true);
        mIntent.putExtra(DOWN_COUNT_TAG, new String[]{mTags[0],
                mTags[1],
                String.valueOf(mTags[2])});
        mMainActivity.startService(mIntent);
    }

    private void continueDownCount() {
        Intent mIntent = new Intent(mMainActivity, DownCountService.class);
        mIntent.putExtra(DOWN_COUNT, mCount);
        mIntent.putExtra(DOWN_COUNT_TAG, new String[]{mTags[0],
                mTags[1],
                String.valueOf(mTags[2])});
        mMainActivity.startService(mIntent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPauseEvent(PauseEvent event) {
        mIvPausePlay.setImageDrawable(getResources()
                .getDrawable(R.mipmap.ic_play_circle_outline_white_24dp));
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
