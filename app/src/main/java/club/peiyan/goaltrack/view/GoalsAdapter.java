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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.peiyan.goaltrack.MainActivity;
import club.peiyan.goaltrack.R;
import club.peiyan.goaltrack.utils.DialogUtil;
import club.peiyan.goaltrack.data.GoalBean;
import club.peiyan.goaltrack.plan.DialogFragmentCreatePlan;

import static android.view.View.inflate;

/**
 * Created by HPY.
 * Time: 2018/7/7.
 * Desc:
 */

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.GoalViewHolder> implements View.OnClickListener {

    private ArrayList<GoalBean> mData;
    private final MainActivity mMainActivity;
    private HashMap<Integer, GoalViewHolder> mHashMap = new HashMap<>();
    private static long clickTimestamp = System.currentTimeMillis();

    public GoalsAdapter(MainActivity mMainActivity) {
        this.mMainActivity = mMainActivity;
    }

    public void setData(ArrayList<GoalBean> mData) {
        this.mData = mData;
    }

    @Override
    public GoalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
            default:
                mHolder = new GoalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parent_goal, parent, false));
        }
        return mHolder;
    }


    @Override
    public void onBindViewHolder(GoalViewHolder holder, final int position) {
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
            for (String item : mItems) {
                View mView = inflate(mContext, R.layout.sub_item_check, null);
                TextView tvItem = mView.findViewById(R.id.tvItem);
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
                        mBean.getTitle()), mHasSubGoal ? "注意：\n移除这个计划的话，\n它的二级计划也将被移除。" : "",
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
                                            mMainActivity.getDBHelper().deleteGoal(bean.getId());
                                        }
                                    }
                                }
                                mMainActivity.getDBHelper().deleteGoal(mBean.getId());
                                mMainActivity.notifyDataSetChange(null);
                            }
                        });
                return true;
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
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

    static class GoalViewHolder extends RecyclerView.ViewHolder {
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

}
