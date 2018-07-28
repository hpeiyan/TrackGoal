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

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import club.peiyan.goaltrack.MainActivity;
import club.peiyan.goaltrack.R;
import club.peiyan.goaltrack.ScoreActivity;
import club.peiyan.goaltrack.data.DBHelper;
import club.peiyan.goaltrack.data.ScoreList;
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
    private ArrayList<ScoreList> mPastScoreList;

    public ScoreFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.yesterday_main, null);
        unbinder = ButterKnife.bind(this, mView);
//        initData();
        mPastScoreList = ((ScoreActivity) getActivity()).getListExtra();
        initView();
        return mView;
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

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ScoreFragment"); //统计页面("MainScreen"为页面名称，可自定义)
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ScoreFragment");
    }
}
