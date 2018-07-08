package club.peiyan.goaltrack.plan;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import club.peiyan.goaltrack.MainActivity;
import club.peiyan.goaltrack.R;
import club.peiyan.goaltrack.data.GoalBean;
import club.peiyan.goaltrack.view.GoalsAdapter;
import club.peiyan.goaltrack.view.MyRecycleView;

/**
 * Created by HPY.
 * Time: 2018/7/8.
 * Desc:
 */

public class TodayFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    @BindView(R.id.rvGoal)
    MyRecycleView mRvGoal;
    @BindView(R.id.constraintLayout)
    RelativeLayout mConstraintLayout;
    Unbinder unbinder;

    private GoalsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static ArrayList<GoalBean> sGoalBeans;

    public TodayFragment() {
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
        mAdapter.setData(sGoalBeans);
        mRvGoal.setItemAnimator(null);
        mRvGoal.setAdapter(mAdapter);
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
}
