package club.peiyan.goaltrack.view;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.peiyan.goaltrack.MainActivity;
import club.peiyan.goaltrack.R;
import club.peiyan.goaltrack.data.DBHelper;
import club.peiyan.goaltrack.data.GoalBean;
import club.peiyan.goaltrack.plan.DialogFragmentCreatePlan;
import club.peiyan.goaltrack.plan.GoalFragment;
import club.peiyan.goaltrack.utils.ListUtil;
import club.peiyan.goaltrack.utils.ToastUtil;

/**
 * Created by HPY.
 * Time: 2018/7/21.
 * Desc:
 */

public class SubItemView implements View.OnClickListener {

    private final MainActivity mActivity;
    private final DBHelper mDBHelper;
    @BindView(R.id.topLine)
    View mTopLine;
    @BindView(R.id.lineLeft)
    View mLineLeft;
    @BindView(R.id.tvItem)
    TextView mTvItem;
    @BindView(R.id.rlItem)
    RelativeLayout mRlItem;
    @BindView(R.id.tvSubEdit)
    TextView mTvSubEdit;
    private View mRootView;
    private final GoalFragment mGoalFragment;
    private String mItem;
    private String mParentGoal;

    private static final int[] leftDrawableRes = new int[]{
            R.drawable.shape_left_line_parent, R.drawable.shape_left_line_2,
            R.drawable.shape_left_line_3, R.drawable.shape_left_line_4
    };

    public SubItemView(MainActivity mMainActivity, String item,
                       String parentGoal, GoalBean mBean, int mViewType) {
        mActivity = mMainActivity;
        mDBHelper = mActivity.getDBHelper();
        mGoalFragment = mActivity.getGoalFragment();
        mItem = item;
        mParentGoal = parentGoal;
        mRootView = mActivity.getLayoutInflater().inflate(R.layout.sub_item_check, null);
        ButterKnife.bind(this, mRootView);

        mLineLeft.setBackground(mActivity.getResources().getDrawable(leftDrawableRes[mViewType]));
        mRlItem.setOnClickListener(this);
        mTvItem.setText(item);
        mTvItem.setOnClickListener(this);
        mTvItem.setTag(R.id.sub_title, item);
        mTvItem.setTag(R.id.parent_title, mBean.getTitle());
        mTvItem.setTag(R.id.level, mBean.getLevel());
        mTvItem.setTag(R.id.startDate, mBean.getStart());
        mTvItem.setTag(R.id.overDate, mBean.getOver());
        mTvSubEdit.setOnClickListener(this);
    }

    public View getRootView() {
        return mRootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlItem:
                scrollToSubGoal();
                break;
            case R.id.tvItem:
                break;
            case R.id.tvSubEdit:
                createSubGoal(mTvItem);
                break;
        }
    }

    private void scrollToSubGoal() {
        ArrayList<GoalBean> mGoals = mActivity.getSingleAllGoals();
        if (!ListUtil.isEmpty(mGoals)) {
            for (int i = 0; i < mGoals.size(); i++) {
                GoalBean goal = mGoals.get(i);
                if (goal.getTitle().equals(mItem) &&
                        goal.getParent().equals(mParentGoal)) {

                    if (mGoalFragment != null &&
                            mGoalFragment.getRvGoal() != null) {
                        mGoalFragment.getRvGoal().smoothScrollToPosition(i);
                    }
                    break;
                }
            }
        }
    }

    private void createSubGoal(View v) {
        String subTitle = (String) v.getTag(R.id.sub_title);
        String parentTitle = (String) v.getTag(R.id.parent_title);
        int level = (int) v.getTag(R.id.level);
        String start = (String) v.getTag(R.id.startDate);
        String over = (String) v.getTag(R.id.overDate);
        if (level == 4) {
            ToastUtil.toast("计划太细了，适可而止");
            return;
        }
        GoalBean mGoalBean = mDBHelper.getGoalByTitle(subTitle, parentTitle);
        if (mGoalBean != null) {
            DialogFragmentCreatePlan.showDialog(mActivity.getFragmentManager(), mGoalBean);
        } else {
            DialogFragmentCreatePlan.showDialog(mActivity.getFragmentManager(), subTitle, parentTitle, level + 1, start, over);
        }
    }
}
