package club.peiyan.goaltrack.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.peiyan.goaltrack.MainActivity;
import club.peiyan.goaltrack.R;
import club.peiyan.goaltrack.data.DBHelper;
import club.peiyan.goaltrack.data.GoalBean;
import club.peiyan.goaltrack.plan.DialogFragmentCreatePlan;
import club.peiyan.goaltrack.utils.DialogUtil;

/**
 * Created by HPY.
 * Time: 2018/7/7.
 * Desc:
 */

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.MyViewHolder> {

    private static final String TAG = "GoalsAdapter";
    private ArrayList<GoalBean> mData;
    private final MainActivity mMainActivity;
    private final DBHelper mDBHelper;
    private static final int[] colorRes = new int[]{
            R.color.root_goal_color, R.color.sub_2_goal_color,
            R.color.sub_3_goal_color, R.color.sub_4_goal_color
    };

    private static final int[] pbDrawableRes = new int[]{
            R.drawable.progressbar_parent, R.drawable.progressbar_2,
            R.drawable.progressbar_3, R.drawable.progressbar_4
    };

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
            case 0:
                mHolder = new GoalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parent_goal, parent, false));
                break;
            case 1:
                mHolder = new GoalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_2_goal, parent, false));
                break;
            case 2:
                mHolder = new GoalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_3_goal, parent, false));
                break;
            case 3:
                mHolder = new GoalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_4_goal, parent, false));
                break;
            default:
                mHolder = new GoalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parent_goal, parent, false));
        }
        return mHolder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder mHolder, int position) {
        int mViewType = getItemViewType(position);
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
            totalHoldDay = deltaYear * 365 + deltaMonth * 30 + deltaDay + 1;// bias 1 day

            int year = Integer.parseInt(mEndDates[0]) - Integer.parseInt(mStartDates[0]);
            int month = Integer.parseInt(mEndDates[1]) - Integer.parseInt(mStartDates[1]);
            int day = Integer.parseInt(mEndDates[2]) - Integer.parseInt(mStartDates[2]);
            totalNeedSpend = year * 365 + month * 30 + day + 1;// bias 1 day
        }

        holder.mPbGoal.setMax(totalNeedSpend);
        holder.mPbGoal.setProgressDrawable(mContext
                .getResources()
                .getDrawable(pbDrawableRes[mViewType]));
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

        /*add sub goal View*/
        if (mItems != null && mItems.length > 0) {
            for (final String item : mItems) {

                View mRootView = new SubItemView(mMainActivity, item,
                        mBean.getTitle(), mBean, mViewType)
                        .getRootView();
                holder.mLlParent.addView(mRootView);
            }
        } else {
            View mRootView = new DownCountView(mMainActivity, mBean).getRootView();
            holder.mLlParent.addView(mRootView);

        }
        holder.btnEdit.setOnClickListener(v -> {
            holder.swMenu.smoothClose();
            DialogFragmentCreatePlan.showDialog(mMainActivity.getFragmentManager(), mBean);
        });

        holder.btnDelete.setOnClickListener(v -> {
            holder.swMenu.smoothClose();
            final boolean mHasSubGoal = mMainActivity.getDBHelper().isHasSubGoal(mBean);
            String mTitle = String.format("确定放弃%s？", mBean.getTitle());
            String mMessage = "注意：\n移除这个计划的话，\n它的子计划将全部被移除。";
            if (!mHasSubGoal) {
                mMessage = mTitle;
                mTitle = "";
            }
            DialogUtil.showSingleDialog(mContext, mTitle, mMessage,
                    "否",
                    "我要删除", new DialogUtil.DialogListener() {
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
                                        bean.setStatus(2);
                                        mMainActivity.getDBHelper().updateGoal(bean);
                                    }
                                }
                            }
                            for (String item : mBean.getItemSplit()) {
                                mDBHelper.deleteScore(item);
                            }
                            mBean.setStatus(2);
                            mMainActivity.getDBHelper().updateGoal(mBean);
                            mMainActivity.notifyDataSetChange(null);
                        }
                    });
        });
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getLevel() - 1;
    }

    @Override
    public int getItemCount() {
        return mData.size();
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
        @BindView(R.id.btnEdit)
        Button btnEdit;
        @BindView(R.id.btnDelete)
        Button btnDelete;
        @BindView(R.id.swMenu)
        SwipeMenuLayout swMenu;

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
