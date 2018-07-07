package club.peiyan.goaltrack.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.peiyan.goaltrack.MainActivity;
import club.peiyan.goaltrack.R;
import club.peiyan.goaltrack.Utils.DialogUtil;
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
        holder.mTvGoalName.setText(mBean.getTitle());
        holder.mTvTimeSpend.setText(12 + "个月");
        String mTrim = mBean.getItems().trim();
        if (!mTrim.isEmpty()) {
            mItems = mTrim.split("\n");
        }
        int mChildCount = holder.mLlParent.getChildCount();
        if (mChildCount > 2) {
            holder.mLlParent.removeViews(2, mChildCount - 2);
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
                                    for (GoalBean bean : mSubAndContactParentGoal) {
                                        mMainActivity.getDBHelper().deleteGoal(bean.getId());
                                    }
                                }
                                mMainActivity.getDBHelper().deleteGoal(mBean.getId());
                                mMainActivity.notifyDataSetChange();
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
        DialogFragmentCreatePlan.showDialog(mMainActivity.getFragmentManager(), subTitle, parentTitle, level + 1);
    }

    public void clearAllCustomView() {
        for (int i = 0; i < mHashMap.size(); i++) {
            GoalViewHolder mHolder = mHashMap.get(i);
            int mCount = mHolder.mLlParent.getChildCount();
            mHolder.mLlParent.removeViews(2, mCount - 2);
        }
        mHashMap.clear();
    }

    static class GoalViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvGoalName)
        TextView mTvGoalName;
        @BindView(R.id.tvTimeSpend)
        TextView mTvTimeSpend;
        @BindView(R.id.llParent)
        LinearLayout mLlParent;

        GoalViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
