package club.peiyan.goaltrack;

import android.os.Bundle;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.peiyan.goaltrack.data.DBHelper;
import club.peiyan.goaltrack.data.GoalBean;
import club.peiyan.goaltrack.plan.DialogFragmentCreatePlan;
import club.peiyan.goaltrack.plan.TodayFragment;
import club.peiyan.goaltrack.plan.TomorrowFragment;
import club.peiyan.goaltrack.plan.YesterdayFragment;
import club.peiyan.goaltrack.utils.AppSp;
import club.peiyan.goaltrack.view.SectionsPagerAdapter;

import static club.peiyan.goaltrack.data.Constants.LATEST_GOAL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

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
    private SectionsPagerAdapter mSectionsPagerAdapter;


    private DBHelper mDBHelper;

    private ArrayList<GoalBean> mSingleAllGoals = new ArrayList<>();
    private GoalBean mLatestParentGoal;
    private ArrayList<GoalBean> mParentGoals;
    private SubMenu mGoalSubMenu;
    private boolean mMode = true;//False预览模式, True编辑模式
    private TodayFragment mTodayFragment;
    private MenuItem mModeMenu;

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
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavView.setNavigationItemSelectedListener(this);
        mGoalSubMenu = mNavView.getMenu().addSubMenu("目标");
        if (mParentGoals != null && mParentGoals.size() > 0) {
            for (GoalBean bean : mParentGoals) {
                MenuItem mItem = mGoalSubMenu.add(R.id.goal, bean.getId(), bean.getId(), bean.getTitle());
                mItem.setIcon(R.mipmap.ic_attach_file_black_24dp);
            }
        }

        TomorrowFragment mTomorrowFragment = new TomorrowFragment();
        YesterdayFragment mYesterdayFragment = new YesterdayFragment();
        mTodayFragment = new TodayFragment();
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

    }

    public SubMenu getGoalSubMenu() {
        return mGoalSubMenu;
    }

    public void initDataBase() {
        mLatestParentGoal = mDBHelper.getGoalByTitle(AppSp.getString(LATEST_GOAL, ""));
        if (mLatestParentGoal == null) {
            mSingleAllGoals.clear();
            return;
        }
        mSingleAllGoals = mDBHelper.getSingleAllGoal(mLatestParentGoal);
        mParentGoals = mDBHelper.getGoalByLevel(1);
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
        mModeMenu = menu.getItem(0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_mode:
                item.setChecked(!item.isChecked());
                mMode = item.isChecked();//触发开关之后的状态
                item.setIcon(mMode ? R.mipmap.ic_lock_open_white_24dp : R.mipmap.ic_lock_outline_white_24dp);
                mFab.setVisibility(mMode ? View.VISIBLE : View.GONE);
                mTodayFragment.getRvGoal().setEditMode(!mMode);
                break;
        }
        return super.onOptionsItemSelected(item);
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
                    if (bean.getId() == id) {
                        AppSp.putString(LATEST_GOAL, bean.getTitle());
                        break;
                    }
                }
                notifyDataSetChange();
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public DBHelper getDBHelper() {
        return mDBHelper;
    }

    public void notifyDataSetChange() {
        initDataBase();
        mTodayFragment.getAdapter().setData(mSingleAllGoals);
        mTodayFragment.getAdapter().notifyDataSetChanged();
        if (mContainer.getCurrentItem() != 1) {
            mContainer.setCurrentItem(1, true);
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
        AppSp.putString(LATEST_GOAL, mLatestParentGoal.getTitle());
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
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
