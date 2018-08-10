package club.peiyan.goaltrack.plan;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import club.peiyan.goaltrack.MainActivity;
import club.peiyan.goaltrack.R;
import club.peiyan.goaltrack.data.Constants;
import club.peiyan.goaltrack.data.GoalBean;
import club.peiyan.goaltrack.netTask.SyncDataTask;
import club.peiyan.goaltrack.utils.AppSp;
import club.peiyan.goaltrack.utils.ThreadUtil;
import club.peiyan.goaltrack.view.GoalsAdapter;
import club.peiyan.goaltrack.view.MyRecycleView;

/**
 * Created by HPY.
 * Time: 2018/7/8.
 * Desc:
 */

public class GoalFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, SyncDataTask.OnSyncListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    Unbinder unbinder;

    @BindView(R.id.rvGoal)
    MyRecycleView mRvGoal;
    @BindView(R.id.createData)
    View mCreatePromp;
    @BindView(R.id.tvArrowDown)
    TextView mTvArrowDown;
    @BindView(R.id.constraintLayout)
    SwipeRefreshLayout mConstraintLayout;

    private GoalsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static ArrayList<GoalBean> sGoalBeans;
    private MainActivity mActivity;

    public GoalFragment() {
    }


    public static void setData(ArrayList<GoalBean> mSingleAllGoals) {
        sGoalBeans = mSingleAllGoals;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        initRecycleView();
        mConstraintLayout.setColorSchemeResources(R.color.colorAccent);
        mConstraintLayout.setOnRefreshListener(this);
        mConstraintLayout.setClickable(false);

    }

    private void initRecycleView() {
        mRvGoal.setHasFixedSize(true);
        mRvGoal.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(4, 4, 4, 4);//设置itemView中内容相对边框左，上，右，下距离
            }
        });
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRvGoal.setLayoutManager(mLayoutManager);
        mAdapter = new GoalsAdapter((MainActivity) getActivity());
        if (sGoalBeans == null) {
            hideOrShowPromp(true);
        } else {
            if (sGoalBeans.size() <= 0) {
                hideOrShowPromp(true);
            }
        }
        mAdapter.setData(sGoalBeans);
        mRvGoal.setItemAnimator(null);
        mRvGoal.setAdapter(mAdapter);
    }

    public void hideOrShowPromp(boolean isShow) {
        mCreatePromp.setVisibility(isShow ? View.VISIBLE : View.GONE);
        mRvGoal.setVisibility(isShow ? View.GONE : View.VISIBLE);
        mConstraintLayout.setEnabled(!isShow);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public MyRecycleView getRvGoal() {
        return mRvGoal;
    }

    public GoalsAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void onRefresh() {
        if (AppSp.getBoolean(Constants.IS_REGISTER, false)) {
            mActivity.startSync(this);
        } else {
            mActivity.notifyDataSetChange("");
            ThreadUtil.uiPostDelay(() -> {
                setPullStatus(false);
            }, 2000);
        }
    }

    @Override
    public void onSuccess() {
        setPullStatus(false);
    }

    private void setPullStatus(final boolean isStop) {
        mConstraintLayout.post(() -> mConstraintLayout.setRefreshing(isStop));
    }

    @Override
    public void onFail() {
        setPullStatus(false);
    }

    public void setActivity(MainActivity mMainActivity) {
        mActivity = mMainActivity;
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("GoalFragment"); //统计页面("MainScreen"为页面名称，可自定义)
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("GoalFragment");
    }
}
