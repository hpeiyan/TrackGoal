package club.peiyan.goaltrack.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.peiyan.goaltrack.MainActivity;
import club.peiyan.goaltrack.R;
import club.peiyan.goaltrack.data.DBHelper;
import club.peiyan.goaltrack.data.GoalBean;
import club.peiyan.goaltrack.data.ScoreBean;
import club.peiyan.goaltrack.plan.DialogFragmentCreatePlan;
import club.peiyan.goaltrack.utils.CalendaUtils;
import club.peiyan.goaltrack.utils.DialogUtil;

import static android.view.View.inflate;

/**
 * Created by HPY.
 * Time: 2018/7/7.
 * Desc:
 */

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.MyViewHolder> implements View.OnClickListener {

    private static final String TAG = "GoalsAdapter";
    //    private static final int TYPE_HEADER = -1;
    private ArrayList<GoalBean> mData;
    private final MainActivity mMainActivity;
    private static long clickTimestamp = System.currentTimeMillis();
    private final DBHelper mDBHelper;

    public GoalsAdapter(MainActivity mMainActivity) {
        this.mMainActivity = mMainActivity;
        mDBHelper = mMainActivity.getDBHelper();
    }

    public void setData(ArrayList<GoalBean> mData) {
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GoalViewHolder mHolder;
        switch (viewType) {
            case 1:
                mHolder = new GoalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parent_goal, parent, false));
                break;
            case 2:
                mHolder = new GoalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_2_goal, parent, false));
                break;
            case 3:
                mHolder = new GoalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_3_goal, parent, false));
                break;
            case 4:
                mHolder = new GoalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_4_goal, parent, false));
                break;
//            case TYPE_HEADER:
//                return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_header_item, parent, false));
            default:
                mHolder = new GoalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parent_goal, parent, false));
        }
        return mHolder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder mHolder, int position) {
//        if (getItemViewType(position) == TYPE_HEADER) {
//            HeaderViewHolder mViewHolder = (HeaderViewHolder) mHolder;
//
//            ArrayList<ScoreBean> mScoreBeans = mDBHelper.getScoreByTime(CalendaUtils.getCurrntDate());
//            int mChildCount = mViewHolder.mLlScoreLine.getChildCount();
//            if (mChildCount > 0) {
//                mViewHolder.mLlScoreLine.removeAllViews();
//            }
//
//            int score = 0;
//            int totalSie = 0;
//            if (mScoreBeans != null && mScoreBeans.size() > 0) {
//                for (ScoreBean bean : mScoreBeans) {
//                    if (bean == null) continue;
//                    View mView = View.inflate(mMainActivity, R.layout.score_line_view, null);
//                    TextView itemName = mView.findViewById(R.id.tvItemName);
//                    itemName.setText(bean.getTitle());
//                    ProgressBar pb = mView.findViewById(R.id.pbGoal);
//                    pb.setProgress(bean.getScore());
//                    mViewHolder.mLlScoreLine.addView(mView);
//                    score += bean.getScore();
//                    totalSie++;
//                }
//            }
//            if (totalSie == 0) {
//                View mView = View.inflate(mMainActivity, R.layout.dont_caculate, null);
//                mViewHolder.mLlScoreLine.addView(mView);
//            } else {
//                mViewHolder.mScoreShow.setText(score + "/" + 10 * totalSie);
//            }
//            return;
//        }

//        position -= 1;
        GoalViewHolder holder = (GoalViewHolder) mHolder;
        String[] mItems = new String[0];
        final Context mContext = holder.mLlParent.getContext();
        final GoalBean mBean = mData.get(position);
        String mStart = mBean.getStart();
        String mOver = mBean.getOver();
        StringBuilder mBuilder = new StringBuilder();

        String[] mStartDates = new String[0];
        String[] mEndDates = new String[0];

        if (mStart != null && !TextUtils.isEmpty(mStart)) {
            mBuilder.append(mStart + "  ⇀  ");
            mStartDates = mStart.split("/");
        }
        if (mOver != null && !TextUtils.isEmpty(mOver)) {
            if (mBuilder.length() > 5) {
                mBuilder.append(mOver);
                mEndDates = mOver.split("/");
            }
        }
        int totalHoldDay = 0;
        int totalNeedSpend = 0;

        if (mStartDates.length == 3 && mEndDates.length == 3) {
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
            int currentDay = Calendar.getInstance().get(Calendar.DATE);
            int deltaYear = currentYear - Integer.parseInt(mStartDates[0]);
            int deltaMonth = currentMonth - Integer.parseInt(mStartDates[1]);
            int deltaDay = currentDay - Integer.parseInt(mStartDates[2]);
            totalHoldDay = deltaYear * 365 + deltaMonth * 30 + deltaDay;

            int year = Integer.parseInt(mEndDates[0]) - Integer.parseInt(mStartDates[0]);
            int month = Integer.parseInt(mEndDates[1]) - Integer.parseInt(mStartDates[1]);
            int day = Integer.parseInt(mEndDates[2]) - Integer.parseInt(mStartDates[2]);
            totalNeedSpend = year * 365 + month * 30 + day;
        }

        holder.mPbGoal.setMax(totalNeedSpend);
        holder.mPbGoal.setProgress(totalHoldDay);

        holder.mTvTimeSpend.setText(mBuilder.toString().trim());

        String holdHtml = "<small><small><small><small><small><small>" + (totalHoldDay <= 0 ? "离开始" : "坚持的") + "</small></small></small></small></small></small>" + Math.abs(totalHoldDay) + "<small><small><small><small><small><small>天 </small></small></small></small></small></small>";
        String downHtml = "<small><small><small><small><small><small>只剩下 </small></small></small></small></small></small>" + (totalNeedSpend - totalHoldDay) + "<small><small><small><small><small><small>天 </small></small></small></small></small></small>";
        holder.mTvDownCount.setText(Html.fromHtml(downHtml));
        holder.mTvHoldCount.setText(Html.fromHtml(holdHtml));
        holder.mTvGoalName.setText(mBean.getTitle());
        String mTrim = mBean.getItems().trim();
        if (!mTrim.isEmpty()) {
            mItems = mTrim.split("\n");
        }
        int mChildCount = holder.mLlParent.getChildCount();
        if (mChildCount > 3) {
            holder.mLlParent.removeViews(3, mChildCount - 3);
        }
        if (mItems != null && mItems.length > 0) {
            for (final String item : mItems) {
                View mView = inflate(mContext, R.layout.sub_item_check, null);
                TextView tvItem = mView.findViewById(R.id.tvItem);
                SeekBar sb = mView.findViewById(R.id.sbProgress);

                final ScoreBean mScoreBean = mDBHelper.getScoreByTitle(item);
                if (mScoreBean != null) {
                    sb.setProgress(mScoreBean.getScore());
                }
                sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        int mProgress = seekBar.getProgress();
                        ScoreBean mScoreByTitle = mDBHelper.getScoreByTitle(item);
                        boolean isSuccess;
                        if (mScoreByTitle == null) {
                            isSuccess = mDBHelper.insertScore(-1, "", item, CalendaUtils.getCurrntDate(), mProgress, System.currentTimeMillis());
                        } else {
                            isSuccess = mDBHelper.updateScore(mScoreByTitle.getId(), -1, "", item, CalendaUtils.getCurrntDate(), mProgress, System.currentTimeMillis());
                        }
                        if (isSuccess) {
                            notifyDataSetChanged();
                        }
                    }
                });

                tvItem.setText(item);
                tvItem.setOnClickListener(this);
                tvItem.setTag(R.id.sub_title, item);
                tvItem.setTag(R.id.parent_title, mBean.getTitle());
                tvItem.setTag(R.id.level, mBean.getLevel());
                tvItem.setTag(R.id.startDate, mBean.getStart());
                tvItem.setTag(R.id.overDate, mBean.getOver());
                holder.mLlParent.addView(mView);
            }
        }
        holder.mLlParent.setTag(mBean);

        holder.mLlParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long mDeltaTime = System.currentTimeMillis() - clickTimestamp;
                if (mDeltaTime < 300) {
                    GoalBean mBean = (GoalBean) v.getTag();
                    DialogFragmentCreatePlan.showDialog(mMainActivity.getFragmentManager(), mBean);
                }
                clickTimestamp = System.currentTimeMillis();
            }
        });

        holder.mLlParent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final boolean mHasSubGoal = mMainActivity.getDBHelper().isHasSubGoal(mBean);
                DialogUtil.showSingleDialog(mContext, String.format("确定放弃%s计划？",
                        mBean.getTitle()), mHasSubGoal ? "注意：\n移除这个计划的话，\n它的子计划将全部被移除。" : "",
                        "开玩笑",
                        "认真脸", new DialogUtil.DialogListener() {
                            @Override
                            public void onNegClickListener() {

                            }

                            @Override
                            public void onPosClickListener() {
                                if (mHasSubGoal) {
                                    ArrayList<GoalBean> mSubAndContactParentGoal
                                            = mMainActivity.getDBHelper().getSubAndContactParentGoal(mBean);
                                    if (mSubAndContactParentGoal != null && mSubAndContactParentGoal.size() > 0) {
                                        for (GoalBean bean : mSubAndContactParentGoal) {
                                            for (String item : bean.getItemSplit()) {
                                                mDBHelper.deleteScore(item);
                                            }
                                            mMainActivity.getDBHelper().deleteGoal(bean.getTitle());
                                        }
                                    }
                                }
                                for (String item : mBean.getItemSplit()) {
                                    mDBHelper.deleteScore(item);
                                }
                                mMainActivity.getDBHelper().deleteGoal(mBean.getTitle());
                                mMainActivity.notifyDataSetChange(null);
                            }
                        });
                return true;
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
//        if (position == 0) return TYPE_HEADER;
        return mData.get(position).getLevel();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        String subTitle = (String) v.getTag(R.id.sub_title);
        String parentTitle = (String) v.getTag(R.id.parent_title);
        int level = (int) v.getTag(R.id.level);
        String start = (String) v.getTag(R.id.startDate);
        String over = (String) v.getTag(R.id.overDate);
        if (level == 4) {
            Toast.makeText(mMainActivity, "计划太细了，适可而止", Toast.LENGTH_SHORT).show();
            return;
        }
        GoalBean mGoalBean = mMainActivity.getDBHelper().getGoalByTitle(subTitle);
        if (mGoalBean != null) {
            DialogFragmentCreatePlan.showDialog(mMainActivity.getFragmentManager(), mGoalBean);
        } else {
            DialogFragmentCreatePlan.showDialog(mMainActivity.getFragmentManager(), subTitle, parentTitle, level + 1, start, over);
        }
    }


    static class GoalViewHolder extends MyViewHolder {
        @BindView(R.id.tvGoalName)
        TextView mTvGoalName;
        @BindView(R.id.tvTimeSpend)
        TextView mTvTimeSpend;
        @BindView(R.id.tvDownCount)
        TextView mTvDownCount;
        @BindView(R.id.tvHoldCount)
        TextView mTvHoldCount;
        @BindView(R.id.llParent)
        LinearLayout mLlParent;
        @BindView(R.id.pbGoal)
        ProgressBar mPbGoal;

        GoalViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        MyViewHolder(View view) {
            super(view);
        }
    }

    static class HeaderViewHolder extends MyViewHolder {
        @BindView(R.id.llScoreLine)
        LinearLayout mLlScoreLine;

        @BindView(R.id.tvScoreShow)
        TextView mScoreShow;

        HeaderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
