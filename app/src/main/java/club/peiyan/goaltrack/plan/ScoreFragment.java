package club.peiyan.goaltrack.plan;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import club.peiyan.goaltrack.MainActivity;
import club.peiyan.goaltrack.R;
import club.peiyan.goaltrack.data.DBHelper;
import club.peiyan.goaltrack.data.ScoreBean;
import club.peiyan.goaltrack.utils.CalendaUtils;
import club.peiyan.goaltrack.view.ScoreAdapter;

/**
 * Created by HPY.
 * Time: 2018/7/8.
 * Desc:
 */

public class ScoreFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.rlScore)
    RecyclerView mRlScore;
    @BindView(R.id.nonData)
    View errorView;

    private MainActivity mMainActivity;
    private DBHelper mDBHelper;
    private ArrayList mPastScoreList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.yesterday_main, null);
        unbinder = ButterKnife.bind(this, mView);
        mMainActivity = (MainActivity) getActivity();
        mDBHelper = mMainActivity.getDBHelper();
        initData();
        initView();
        return mView;
    }

    private void initData() {
        mPastScoreList = new ArrayList();
        if (mDBHelper != null) {
            for (int i = 1; i < 7; i++) {
                ArrayList<ScoreBean> mScoreBeans = mDBHelper.getScoreByTime(CalendaUtils.getDate(i));
                if (mScoreBeans != null && mScoreBeans.size() > 0) {
                    mPastScoreList.add(mScoreBeans);
                }
            }
        }
    }

    private void initView() {
        if (mPastScoreList.size() == 0) {
            errorView.setVisibility(View.VISIBLE);
        }
        initRecycleView();
    }

    private void initRecycleView() {
        mRlScore.setHasFixedSize(true);
        mRlScore.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(4, 4, 4, 4);//设置itemView中内容相对边框左，上，右，下距离
            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRlScore.setLayoutManager(mLayoutManager);
        ScoreAdapter mAdapter = new ScoreAdapter();
        mAdapter.setData(mPastScoreList);
        mRlScore.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
