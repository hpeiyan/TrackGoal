package club.peiyan.goaltrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.peiyan.goaltrack.data.Constants;
import club.peiyan.goaltrack.data.DBHelper;
import club.peiyan.goaltrack.data.GoalBean;
import club.peiyan.goaltrack.plan.DialogFragmentCreatePlan;
import club.peiyan.goaltrack.plan.GoalFragment;
import club.peiyan.goaltrack.plan.MyScoreFragment;
import club.peiyan.goaltrack.sync.SyncDataTask;
import club.peiyan.goaltrack.utils.AppSp;
import club.peiyan.goaltrack.utils.ToastUtil;
import club.peiyan.goaltrack.view.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener, SyncDataTask.OnSyncListener {

    private static final String TAG = "MainActivity";
    private static final String SYNC_DATA = "sync_data";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.container)
    ViewPager mContainer;
    @BindView(R.id.rlSyncPB)
    RelativeLayout mRlSyncPB;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private DBHelper mDBHelper;
    private SubMenu mGoalSubMenu;

    private static ArrayList<String> barTitleNames = new ArrayList<>();
    private TextView mTvUserName;

    public static void startMainActivity(ReLoginActivity mActivity, String mName, boolean isSyncData) {
        AppSp.putString(Constants.USER_NAME, mName);
        Intent mIntent = new Intent(mActivity, MainActivity.class);
        mIntent.putExtra(SYNC_DATA, isSyncData);
        mActivity.startActivity(mIntent);
        if (!mActivity.isDestroyed()) {
            mActivity.finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDBHelper = new DBHelper(this);
        initView();
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createParentPlan();
            }
        });
        boolean isSyncData = getIntent().getBooleanExtra(SYNC_DATA, false);
        if (isSyncData) {
            setSyncPBVisible(true);
            startSync(this);
        }
    }

    private void setSyncPBVisible(final boolean isShow) {
        mRlSyncPB.post(new Runnable() {
            @Override
            public void run() {
                mRlSyncPB.setVisibility(isShow ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void initView() {

        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mNavView.setNavigationItemSelectedListener(this);
        View mHeaderView = mNavView.getHeaderView(0);
        mTvUserName = mHeaderView.findViewById(R.id.tvUserName);
        mTvUserName.setText(AppSp.getString(Constants.USER_NAME, "BenChur"));

        barTitleNames.add(getString(R.string.score));
        ArrayList<GoalBean> mAllGoals = mDBHelper.getAllGoals();
        ArrayList<Integer> mLevelList = new ArrayList<>();
        for (GoalBean mBean : mAllGoals) {
            if (!mLevelList.contains(mBean.getLevel())) {
                mLevelList.add(mBean.getLevel());
            }
        }
        Collections.sort(mLevelList);
        ArrayList<Fragment> mFragments = new ArrayList<>();
        mFragments.add(new MyScoreFragment());
        if (mLevelList.size() > 0) {
            for (int index : mLevelList) {
                barTitleNames.add(index + "级目标");
                GoalFragment mFragment = new GoalFragment();
                mFragment.setActivity(this);
                ArrayList<GoalBean> mGoalByLevel = mDBHelper.getGoalByLevel(index);
                mFragment.setData(mGoalByLevel);
                mFragments.add(mFragment);
            }
        }
        getSupportActionBar().setTitle(barTitleNames.get(1));
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.setData(mFragments);
        mContainer.setAdapter(mSectionsPagerAdapter);
        mContainer.setCurrentItem(1);
        mContainer.addOnPageChangeListener(this);

    }

    public SubMenu getGoalSubMenu() {
        return mGoalSubMenu;
    }


    private void createParentPlan() {
        DialogFragmentCreatePlan.showDialog(getFragmentManager(), 1);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sync:
                startSync(this);
        }
        return super.onOptionsItemSelected(item);
    }

    public void startSync(SyncDataTask.OnSyncListener mListener) {
        SyncDataTask mTask = new SyncDataTask(MainActivity.this);
        mTask.setOnSyncListener(mListener);
        mTask.setSyncData(mDBHelper.getAllGoals());
        new Thread(mTask).start();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.policy:
                break;
            case R.id.goal:
                break;
            case R.id.prompt:
                break;
            case R.id.setting:
                break;
            case R.id.feedback:
                break;
            default:
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public DBHelper getDBHelper() {
        return mDBHelper;
    }

    public void notifyDataSetChange(@Nullable String goalTitle) {

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        getSupportActionBar().setTitle(barTitleNames.get(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSuccess() {
        ToastUtil.toast("同步成功");
        setSyncPBVisible(false);
    }

    @Override
    public void onFail() {
        ToastUtil.toast("同步失败");
        setSyncPBVisible(false);
    }
}
