package club.peiyan.goaltrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import club.peiyan.goaltrack.data.Constants;
import club.peiyan.goaltrack.utils.AppSp;
import club.peiyan.goaltrack.utils.ViewUtil;

/**
 * Created by HPY.
 * Time: 2018/7/16.
 * Desc:
 */

public class TeachActivity extends BaseActivity {


    @BindView(R.id.tvArrowSync)
    TextView mTvArrowSync;
    @BindView(R.id.llSync)
    LinearLayout mLlSync;
    @BindView(R.id.tvGoal)
    TextView mTvGoal;
    @BindView(R.id.llGoal)
    LinearLayout mLlGoal;
    @BindView(R.id.tvArrowDown)
    TextView mTvArrowDown;
    @BindView(R.id.llCreat)
    LinearLayout mLlCreat;
    @BindView(R.id.llLook)
    LinearLayout mLlLook;
    @BindView(R.id.llEdit)
    LinearLayout mLlEdit;
    @BindView(R.id.tvNext)
    TextView mTvNext;

    private int stepIndex = 1;
    private View[] mViews;
    private Animation mAnimation;

    public static void startTeachActivity(MainActivity mMainActivity) {
        Intent mIntent = new Intent(mMainActivity, TeachActivity.class);
        mMainActivity.startActivity(mIntent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.teach_layout);
        ButterKnife.bind(this);
        mViews = new View[]{mLlCreat, mLlSync, mLlEdit, mLlLook, mLlGoal};
        mTvGoal.setText("目标列表\n应用设置");
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.shake_up_down);
        mAnimation.setRepeatMode(Animation.RESTART);
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setInterpolator(new LinearInterpolator());
        mLlCreat.startAnimation(mAnimation);
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

    @OnClick(R.id.tvNext)
    public void onViewClicked() {
        if (stepIndex == mViews.length) {
            AppSp.putBoolean(Constants.HAD_TEACHERS, true);
            finish();
            return;
        }
        mViews[stepIndex - 1].clearAnimation();
        ViewUtil.setGone(mViews[stepIndex - 1]);
        ViewUtil.setVisible(mViews[stepIndex]);
        mViews[stepIndex].startAnimation(mAnimation);
        if (stepIndex == mViews.length - 1) {
            mTvNext.setText("开始目标");
        }
        stepIndex++;
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
