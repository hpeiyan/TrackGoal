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
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.peiyan.goaltrack.data.Constants;
import club.peiyan.goaltrack.data.DBHelper;
import club.peiyan.goaltrack.data.GoalBean;
import club.peiyan.goaltrack.plan.DialogFragmentCreatePlan;
import club.peiyan.goaltrack.plan.TodayFragment;
import club.peiyan.goaltrack.plan.TomorrowFragment;
import club.peiyan.goaltrack.plan.YesterdayFragment;
import club.peiyan.goaltrack.sync.SyncDataTask;
import club.peiyan.goaltrack.utils.AppSp;
import club.peiyan.goaltrack.utils.ToastUtil;
import club.peiyan.goaltrack.view.SectionsPagerAdapter;

import static club.peiyan.goaltrack.data.Constants.LATEST_GOAL;

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

    private ArrayList<GoalBean> mSingleAllGoals = new ArrayList<>();
    private GoalBean mLatestParentGoal;
    private ArrayList<GoalBean> mParentGoals;
    private SubMenu mGoalSubMenu;
    private boolean mMode = true;//False预览模式, True编辑模式
    private TodayFragment mTodayFragment;
    private MenuItem mModeMenu;

    private static final int[] titleRes = new int[]{R.string.yesterday, R.string.today, R.string.tomorrow};
    private static final int[] imgRes = new int[]{R.mipmap.ic_headset_black_24dp, R.mipmap.ic_toys_black_24dp,
            R.mipmap.ic_beenhere_black_24dp, R.mipmap.ic_local_offer_black_24dp,
            R.mipmap.ic_content_paste_black_24dp, R.mipmap.ic_send_black_24dp};
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
        initDataBase();
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
        if (mSingleAllGoals != null && mSingleAllGoals.size() > 0) {
            setMode((Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 20) ? true : false);
        }
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setTitle(titleRes[1]);

        mNavView.setNavigationItemSelectedListener(this);
        mGoalSubMenu = mNavView.getMenu().addSubMenu("目标");
        initMenuItem();
        TomorrowFragment mTomorrowFragment = new TomorrowFragment();
        YesterdayFragment mYesterdayFragment = new YesterdayFragment();
        mTodayFragment = new TodayFragment();
        mTodayFragment.setActivity(this);
        mTodayFragment.setData(mSingleAllGoals);

        ArrayList<Fragment> mFragments = new ArrayList<>();
        mFragments.add(mYesterdayFragment);
        mFragments.add(mTodayFragment);
        mFragments.add(mTomorrowFragment);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.setData(mFragments);
        mContainer.setAdapter(mSectionsPagerAdapter);
        mContainer.setCurrentItem(1);
        mContainer.addOnPageChangeListener(this);

        View mHeaderView = mNavView.getHeaderView(0);
        mTvUserName = mHeaderView.findViewById(R.id.tvUserName);
        mTvUserName.setText(AppSp.getString(Constants.USER_NAME, "BenChur"));
    }

    public void setMode(boolean mMode) {
        this.mMode = mMode;
    }

    private void initMenuItem() {
        mGoalSubMenu.clear();
        if (mParentGoals != null && mParentGoals.size() > 0) {
            for (int i = 0; i < mParentGoals.size(); i++) {
                GoalBean bean = mParentGoals.get(i);
                MenuItem mItem = mGoalSubMenu.add(R.id.goal, bean.getId(), bean.getId(), bean.getTitle());
                mItem.setIcon(imgRes[i % 6]);
            }
        }
    }

    public SubMenu getGoalSubMenu() {
        return mGoalSubMenu;
    }

    public void initDataBase() {
        if (mParentGoals != null) mParentGoals.clear();
        mParentGoals = mDBHelper.getGoalByLevel(1);
        setLatestParentGoal(mDBHelper.getGoalByTitle(AppSp.getString(LATEST_GOAL, "")));
        if (mLatestParentGoal == null) {
            setLatestParentGoal(mDBHelper.getNearestParentGoal());
        }
        if (mLatestParentGoal == null) {
            mSingleAllGoals.clear();
            return;
        }
        mSingleAllGoals = mDBHelper.getSingleAllGoal(mLatestParentGoal);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        mModeMenu = menu.getItem(1);
        initAppMode(mModeMenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_mode:
                item.setChecked(!item.isChecked());
                setMode(item.isChecked());//触发开关之后的状态
                initAppMode(item);
                break;
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

    private void initAppMode(MenuItem item) {
        item.setChecked(mMode);
        item.setIcon(mMode ? R.mipmap.ic_lock_open_white_24dp : R.mipmap.ic_lock_outline_white_24dp);
        mFab.setVisibility(mMode ? View.VISIBLE : View.GONE);
        mTodayFragment.getRvGoal().setEditMode(!mMode);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_about:
                break;
            case R.id.nav_share:
                break;
            default:
                for (GoalBean bean : mParentGoals) {
                    if (item.getTitle().equals(bean.getTitle())) {
                        AppSp.putString(LATEST_GOAL, bean.getTitle());
                        break;
                    }
                }
                notifyDataSetChange(null);
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public DBHelper getDBHelper() {
        return mDBHelper;
    }

    public void notifyDataSetChange(@Nullable String goalTitle) {
        initDataBase();
        if (mSingleAllGoals.size() <= 0) {
            mTodayFragment.hideOrShowPromp(true);
        } else {
            mTodayFragment.hideOrShowPromp(false);
        }
        mTodayFragment.getAdapter().setData(mSingleAllGoals);
        mTodayFragment.getAdapter().notifyDataSetChanged();
        initMenuItem();
        if (mContainer.getCurrentItem() != 1) {
            mContainer.setCurrentItem(1, true);
        }
        if (goalTitle != null && !TextUtils.isEmpty(goalTitle)) {
            for (int i = 0; i < mSingleAllGoals.size(); i++) {
                if (goalTitle.equals(mSingleAllGoals.get(i).getTitle())) {
                    mTodayFragment.getRvGoal().scrollToPosition(i);
                    break;
                }
            }
        }
    }

    /**
     * 获取当前所有的Goal
     *
     * @return
     */
    public ArrayList<GoalBean> getSingleAllGoals() {
        return mSingleAllGoals;
    }

    /**
     * 获取展示的parent goal
     *
     * @return
     */
    public GoalBean getLatestParentGoal() {
        return mLatestParentGoal;
    }

    public void setLatestParentGoal(GoalBean mLatestParentGoal) {
        this.mLatestParentGoal = mLatestParentGoal;
        if (mLatestParentGoal != null) {
            AppSp.putString(LATEST_GOAL, mLatestParentGoal.getTitle());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLatestParentGoal != null) {
            AppSp.putString(LATEST_GOAL, mLatestParentGoal.getTitle());
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position != 1) {
            mModeMenu.setVisible(false);
            mFab.setVisibility(View.GONE);
        } else {
            mModeMenu.setVisible(true);
            mFab.setVisibility(mMode ? View.VISIBLE : View.GONE);
        }
        getSupportActionBar().setTitle(titleRes[position]);
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
