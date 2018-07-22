package club.peiyan.goaltrack;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import club.peiyan.goaltrack.data.Constants;
import club.peiyan.goaltrack.data.DBHelper;
import club.peiyan.goaltrack.data.GoalBean;
import club.peiyan.goaltrack.event.PauseEvent;
import club.peiyan.goaltrack.listener.DownCountListener;
import club.peiyan.goaltrack.netTask.SyncDataTask;
import club.peiyan.goaltrack.plan.DialogFragmentCreatePlan;
import club.peiyan.goaltrack.plan.GoalFragment;
import club.peiyan.goaltrack.plan.ScoreFragment;
import club.peiyan.goaltrack.utils.AppSp;
import club.peiyan.goaltrack.utils.CalendaUtils;
import club.peiyan.goaltrack.utils.DialogUtil;
import club.peiyan.goaltrack.utils.ListUtil;
import club.peiyan.goaltrack.utils.TimeUtil;
import club.peiyan.goaltrack.utils.ToastUtil;
import club.peiyan.goaltrack.utils.UIThread;
import club.peiyan.goaltrack.utils.ViewUtil;
import club.peiyan.goaltrack.view.SectionsPagerAdapter;

import static club.peiyan.goaltrack.DownCountService.COUNT_FINISH;
import static club.peiyan.goaltrack.DownCountService.COUNT_STOP;
import static club.peiyan.goaltrack.DownCountService.DOWN_COUNT;
import static club.peiyan.goaltrack.DownCountService.DOWN_COUNT_ORIGIN;
import static club.peiyan.goaltrack.DownCountService.DOWN_COUNT_TAG;
import static club.peiyan.goaltrack.data.Constants.LATEST_GOAL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener,
        SyncDataTask.OnSyncListener,
        ServiceConnection, DownCountListener {

    private static final String TAG = "MainActivity";
    private static final String SYNC_DATA = "sync_data";
    private static final int FEEDBACKID = 1024;
    private static final int ADD_GOAL_ID = 1086;
    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final String ACTION_SNOOZE = "ACTION_SNOOZE";
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
    @BindView(R.id.tvDownCount)
    TextView mTvDownCount;
    @BindView(R.id.ivPausePlay)
    ImageView mIvPausePlay;
    @BindView(R.id.ivClose)
    ImageView mIvClose;
    @BindView(R.id.rlDownCount)
    RelativeLayout mRlDownCount;
    @BindView(R.id.pbSync)
    ProgressBar mPbSync;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private DBHelper mDBHelper;

    private ArrayList<GoalBean> mSingleAllGoals = new ArrayList<>();
    private GoalBean mLatestParentGoal;
    private ArrayList<GoalBean> mParentGoals;
    private SubMenu mGoalSubMenu;
    private boolean mMode = true;//False预览模式, True编辑模式
    private GoalFragment mGoalFragment;
    private MenuItem mModeMenu;

    private static final int[] titleRes = new int[]{R.string.score, R.string.goal};
    private static final int[] imgRes = new int[]{R.mipmap.ic_headset_black_24dp, R.mipmap.ic_toys_black_24dp,
            R.mipmap.ic_beenhere_black_24dp, R.mipmap.ic_local_offer_black_24dp,
            R.mipmap.ic_content_paste_black_24dp, R.mipmap.ic_send_black_24dp};
    private TextView mTvUserName;
    private SubMenu mAppSubMenu;
    private DownCountService mService;
    private ArrayList<DownCountListener> mListenerList = new ArrayList<>();

    private long mCostTimeMills;
    private String[] mTags;
    private long mCount;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mTags = intent.getStringArrayExtra(DOWN_COUNT_TAG);
            boolean mIsFinish = intent.getBooleanExtra(COUNT_FINISH, false);
            if (mListenerList.size() > 0) {
                for (DownCountListener mDownCountListener : mListenerList) {
                    if (mDownCountListener != null) {
                        if (mIsFinish) {
                            mDownCountListener.onFinish(mIsFinish, mTags);
                        } else {
                            mCount = intent.getLongExtra(DOWN_COUNT, 0);
                            long mOrigin = intent.getLongExtra(DOWN_COUNT_ORIGIN, 0);
                            mDownCountListener.onTick(mCount, mOrigin, mTags);
                            mCostTimeMills = mOrigin - mCount;
                        }
                    }
                }
            }

        }
    };

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
        mFab.setOnClickListener(view -> createParentPlan());
        boolean isSyncData = getIntent().getBooleanExtra(SYNC_DATA, false);
        if (isSyncData) {
            setSyncPBVisible(true);
            startSync(this);
        }
        setDownCountListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void setSyncPBVisible(final boolean isShow) {
        mRlSyncPB.post(() -> mRlSyncPB.setVisibility(isShow ? View.VISIBLE : View.GONE));
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setTitle(titleRes[1]);

        mNavView.setNavigationItemSelectedListener(this);
        mGoalSubMenu = mNavView.getMenu().addSubMenu("目标");
        mAppSubMenu = mNavView.getMenu().addSubMenu("App");
        initMenuItem("");

        ScoreFragment mScoreFragment = new ScoreFragment();
        mGoalFragment = new GoalFragment();
        mGoalFragment.setActivity(this);
        mGoalFragment.setData(mSingleAllGoals);

        ArrayList<Fragment> mFragments = new ArrayList<>();
        mFragments.add(mScoreFragment);
        mFragments.add(mGoalFragment);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.setData(mFragments);
        mContainer.setAdapter(mSectionsPagerAdapter);
        mContainer.setCurrentItem(1);
        mContainer.addOnPageChangeListener(this);

        View mHeaderView = mNavView.getHeaderView(0);
        mTvUserName = mHeaderView.findViewById(R.id.tvUserName);
        mTvUserName.setText(AppSp.getString(Constants.USER_NAME, "佚名"));

        if (ListUtil.isEmpty(mParentGoals)) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    public void setMode(boolean mMode) {
        this.mMode = mMode;
    }

    private void initMenuItem(String mGoalTitle) {
        mGoalSubMenu.clear();
        if (mParentGoals != null && mParentGoals.size() > 0) {
            for (int i = 0; i < mParentGoals.size(); i++) {
                GoalBean bean = mParentGoals.get(i);
                MenuItem mItem = mGoalSubMenu.add(R.id.goal, bean.getId(), bean.getId(), bean.getTitle());
                mItem.setIcon(imgRes[i % 6]);
                if (bean.getTitle().equals(mGoalTitle)) {
                    mItem.setChecked(true);
                }
                if (i == 0 && TextUtils.isEmpty(mGoalTitle)) {
                    mItem.setChecked(true);
                }
            }
        }
        MenuItem mItem = mGoalSubMenu.add(R.id.add_goal, ADD_GOAL_ID, ADD_GOAL_ID, "创建计划");
        mItem.setIcon(R.mipmap.ic_add_black_24dp);
        mAppSubMenu.clear();
        MenuItem mMenuItem = mAppSubMenu.add(R.id.app, FEEDBACKID, 1, getString(R.string.feedback));
        mMenuItem.setIcon(R.mipmap.ic_send_black_24dp);
    }

    public SubMenu getGoalSubMenu() {
        return mGoalSubMenu;
    }

    public void initDataBase() {
        if (mParentGoals != null) mParentGoals.clear();
        mParentGoals = mDBHelper.getGoalByLevel(1);
        setLatestParentGoal(mDBHelper.getGoalByTitle(AppSp.getString(LATEST_GOAL, ""), "rootParent"));
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
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
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
                setSyncPBVisible(true);
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
        mGoalFragment.getRvGoal().setEditMode(!mMode);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case FEEDBACKID:
                FeedBackActivity.startFeedbackActivity(MainActivity.this);
                break;
            case ADD_GOAL_ID:
                createParentPlan();
                break;
            default:
                for (GoalBean bean : mParentGoals) {
                    if (item.getTitle().equals(bean.getTitle())) {
                        item.setChecked(true);
                        AppSp.putString(LATEST_GOAL, bean.getTitle());
                        notifyDataSetChange(bean.getTitle());
                        break;
                    }
                }
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
            mGoalFragment.hideOrShowPromp(true);
        } else {
            mGoalFragment.hideOrShowPromp(false);
        }
        mGoalFragment.getAdapter().setData(mSingleAllGoals);
        mGoalFragment.getAdapter().notifyDataSetChanged();
        initMenuItem(goalTitle);
        if (mContainer.getCurrentItem() != 1) {
            mContainer.setCurrentItem(1, true);
        }
        if (goalTitle != null && !TextUtils.isEmpty(goalTitle)) {
            for (int i = 0; i < mSingleAllGoals.size(); i++) {
                if (goalTitle.equals(mSingleAllGoals.get(i).getTitle())) {
                    mGoalFragment.getRvGoal().scrollToPosition(i);
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

    public GoalFragment getGoalFragment() {
        return mGoalFragment;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        DownCountService.MyBinder b = (DownCountService.MyBinder) service;
        mService = b.getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mService = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, DownCountService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
        registerReceiver(mReceiver, new IntentFilter(
                DownCountService.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
        unregisterReceiver(mReceiver);
    }

    public void setDownCountListener(DownCountListener mListener) {
        mListenerList.add(mListener);
    }

    public DownCountService getService() {
        return mService;
    }

    public String[] getTags() {
        return mTags;
    }

    public long getCount() {
        return mCount;
    }

    @Override
    public void onTick(long count, long countOrigin, String[] tags) {
        String mTitle = "";
        if (tags != null && tags.length == 3) {
            mTitle = tags[0];
        }
        mIvPausePlay.setImageDrawable(getResources().getDrawable(R.mipmap.ic_pause_circle_outline_white_24dp));
        String countHtml = mTitle + ":    " + "<big><big><big>" + TimeUtil.formatDownTime(count) + "</big></big></big>";
        mTvDownCount.setText(Html.fromHtml(countHtml));
        if (!ViewUtil.isVisible(mRlDownCount)) {
            mRlDownCount.setVisibility(View.VISIBLE);
            getSupportActionBar().hide();
        }
    }

    @Override
    public void onFinish(boolean isFinish, String[] tags) {
        if (isFinish) {
            if (ViewUtil.isVisible(mRlDownCount)) {
                mRlDownCount.setVisibility(View.GONE);
                getSupportActionBar().show();
            }
            if (tags != null
                    && tags.length == 3) {
//                if (mCostTimeMills > 15 * 60 * 1000) {
                if (mCostTimeMills > 15) {
                    // TODO: 2018/7/21 后续改成15分钟
                    mDBHelper.updateScore(Integer.parseInt(tags[2]), tags[1], tags[0], CalendaUtils.getCurrntDate(),
                            mCostTimeMills, System.currentTimeMillis());
                }
            }
            notifyDataSetChange(null);
            mIvPausePlay.setImageDrawable(getResources().getDrawable(R.mipmap.ic_pause_circle_outline_white_24dp));
            isPause = false;
        }
    }

    private boolean isPause = false;// now status

    @OnClick({R.id.ivPausePlay, R.id.ivClose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivPausePlay:
                if (isPause) {
                    mIvPausePlay.setImageDrawable(getResources().getDrawable(R.mipmap.ic_pause_circle_outline_white_24dp));
                    continueDownCount();
                } else {
                    mIvPausePlay.setImageDrawable(getResources().getDrawable(R.mipmap.ic_play_circle_outline_white_24dp));
                    pauseDownCount();
                    UIThread.postDelay(() -> notifyDataSetChange(null), 200);
                }
                isPause = !isPause;
                break;
            case R.id.ivClose:
                DialogUtil.showSingleDialog(this, null, "取消计时任务吗？", "继续计时", "我要取消", new DialogUtil.DialogListener() {
                    @Override
                    public void onNegClickListener() {

                    }

                    @Override
                    public void onPosClickListener() {
                        finishDownCount();
                    }
                });
                break;
        }
    }

    private void pauseDownCount() {
        Intent mIntent = new Intent(this, DownCountService.class);
        mIntent.putExtra(COUNT_STOP, true);
        mIntent.putExtra(DOWN_COUNT_TAG, new String[]{mTags[0],
                mTags[1],
                String.valueOf(mTags[2])});
        startService(mIntent);
    }

    private void finishDownCount() {
        Intent mIntent = new Intent(this, DownCountService.class);
        mIntent.putExtra(COUNT_FINISH, true);
        mIntent.putExtra(DOWN_COUNT_TAG, new String[]{mTags[0],
                mTags[1],
                String.valueOf(mTags[2])});
        startService(mIntent);
    }

    private void continueDownCount() {
        Intent mIntent = new Intent(this, DownCountService.class);
        mIntent.putExtra(DOWN_COUNT, mCount);
        mIntent.putExtra(DOWN_COUNT_TAG, new String[]{mTags[0],
                mTags[1],
                String.valueOf(mTags[2])});
        startService(mIntent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPauseEvent(PauseEvent event) {
        mIvPausePlay.setImageDrawable(getResources()
                .getDrawable(R.mipmap.ic_play_circle_outline_white_24dp));
    }

}
