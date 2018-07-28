package club.peiyan.goaltrack.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.peiyan.goaltrack.GoalApplication;
import club.peiyan.goaltrack.R;
import club.peiyan.goaltrack.data.ScoreBean;
import club.peiyan.goaltrack.data.ScoreList;

/**
 * Created by HPY.
 * Time: 2018/7/16.
 * Desc:
 */

public class ScoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<ScoreList> mScoreList;

    public void setData(ArrayList<ScoreList> mPastScoreList) {
        mScoreList = mPastScoreList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mInflate = View.inflate(parent.getContext(), R.layout.score_item_layout, null);
        return new ViewHolder(mInflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ScoreList mScoreBeans = mScoreList.get(position);
        ViewHolder mHolder = (ViewHolder) holder;
        int score = 0;
        int totalSie = 0;
        if (mScoreBeans != null) {
            if (mScoreBeans.getScoreBeans().size() > 0) {
                for (ScoreBean bean : mScoreBeans.getScoreBeans()) {
                    if (bean == null) continue;
                    score += bean.getScore();
                    View mView = View.inflate(GoalApplication.getContext(), R.layout.score_line_view, null);
                    TextView itemName = mView.findViewById(R.id.tvItemName);
                    itemName.setText(bean.getTitle());
                    ProgressBar pb = mView.findViewById(R.id.pbGoal);
                    pb.setProgress(bean.getScore());
                    mHolder.mLlScoreLine.addView(mView);
                    totalSie++;
                    mHolder.mTvScoreTitle.setText(bean.getDate());
                }
                mHolder.mTvScoreShow.setText(score + "/" + totalSie * 10);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mScoreList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvScoreTitle)
        TextView mTvScoreTitle;
        @BindView(R.id.tvScoreShow)
        TextView mTvScoreShow;
        @BindView(R.id.llScoreLine)
        LinearLayout mLlScoreLine;
        @BindView(R.id.llParent)
        RelativeLayout mLlParent;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
