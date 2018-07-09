package club.peiyan.goaltrack.plan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import club.peiyan.goaltrack.MainActivity;
import club.peiyan.goaltrack.R;
import club.peiyan.goaltrack.data.DBHelper;
import club.peiyan.goaltrack.data.ScoreBean;
import club.peiyan.goaltrack.utils.CalendaUtils;

/**
 * Created by HPY.
 * Time: 2018/7/8.
 * Desc:
 */

public class YesterdayFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.tvScoreTitle)
    TextView mTvScoreTitle;
    @BindView(R.id.tvScoreShow)
    TextView mTvScoreShow;
    @BindView(R.id.llScoreLine)
    LinearLayout mLlScoreLine;
    @BindView(R.id.llParent)
    RelativeLayout mLlParent;
    @BindView(R.id.nonData)
    View errorView;

    private MainActivity mMainActivity;
    private DBHelper mDBHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivity = (MainActivity) getActivity();
        mDBHelper = mMainActivity.getDBHelper();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.yesterday_main, null);
        unbinder = ButterKnife.bind(this, mView);
        initView();
        return mView;
    }

    private void initView() {
        mTvScoreTitle.setText(getString(R.string.yesterday_score));
        ArrayList<ScoreBean> mScoreBeans = mDBHelper.getScoreByTime(CalendaUtils.getYesterday());
        int score = 0;
        int totalSie = 0;
        if (mScoreBeans != null && mScoreBeans.size() > 0) {
            for (ScoreBean bean : mScoreBeans) {
                if (bean == null) continue;
                score += bean.getScore();
                View mView = View.inflate(mMainActivity, R.layout.score_line_view, null);
                TextView itemName = mView.findViewById(R.id.tvItemName);
                itemName.setText(bean.getTitle());
                ProgressBar pb = mView.findViewById(R.id.pbGoal);
                pb.setProgress(bean.getScore());
                mLlScoreLine.addView(mView);
                totalSie++;
            }
        }
        if (totalSie == 0) {
//            View mView = View.inflate(mMainActivity, R.layout.dont_caculate, null);
//            mViewHolder.mLlScoreLine.addView(mView);
            mLlParent.setVisibility(View.GONE);
            errorView.setVisibility(View.VISIBLE);
        } else {
            mTvScoreShow.setText(score + "/" + 10 * totalSie);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
