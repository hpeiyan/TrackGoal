package club.peiyan.goaltrack.plan;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import club.peiyan.goaltrack.AlarmBroadcastReceiver;
import club.peiyan.goaltrack.GoalApplication;
import club.peiyan.goaltrack.MainActivity;
import club.peiyan.goaltrack.R;
import club.peiyan.goaltrack.data.AlarmBean;
import club.peiyan.goaltrack.data.DBHelper;
import club.peiyan.goaltrack.data.GoalBean;
import club.peiyan.goaltrack.utils.AppSp;
import club.peiyan.goaltrack.utils.DialogUtil;
import club.peiyan.goaltrack.utils.LogUtil;

import static android.content.Context.ALARM_SERVICE;
import static club.peiyan.goaltrack.AlarmBroadcastReceiver.CONTENT;
import static club.peiyan.goaltrack.AlarmBroadcastReceiver.TITLE;
import static club.peiyan.goaltrack.data.Constants.LATEST_GOAL;
import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_MULTIPLE;

/**
 * Created by HPY.
 * Time: 2018/7/6.
 * Desc:
 */

public class DialogFragmentCreatePlan extends DialogFragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private static final String FRAG_TAG = "DialogFragmentCreatePlan";
    private static final String GOAL_NAME = "goal_name";
    private static final String GOAL_LEVEL = "goal_level";
    private static final String GOAL_PRENT = "goal_parent";
    public static final String ROOT_PARENT = "rootParent";
    private static final String GOAL_START_DATE = "startDate";
    private static final String GOAL_END_DATE = "endDate";
    private static final String GOAL_ITEMS = "items";
    private static final String GOAL_ID = "id";
    private static final String EDIT_MODE = "isEditMode";
    Unbinder unbinder;
    @BindView(R.id.tvGoalName)
    EditText mTvGoalName;
    @BindView(R.id.tvStartDateShow)
    TextView mTvStartDateShow;
    @BindView(R.id.tvEndDateShow)
    TextView mTvEndDateShow;
    @BindView(R.id.btnAddItem)
    TextView mBtnAddItem;
    @BindView(R.id.llParent)
    LinearLayout mLlParent;
    @BindView(R.id.ivCancel)
    ImageView mTvCancel;
    @BindView(R.id.ivSave)
    ImageView mTvSave;
    @BindView(R.id.tvNotion)
    CheckedTextView mTvNotion;
    @BindView(R.id.swNotion)
    Switch mSwNotion;
    @BindView(R.id.llNotion)
    LinearLayout mLlNotion;

    private View mRootView;
    private ArrayList<EditText> mItemViewList = new ArrayList<>();
    private String mGoalName;
    private int mLevel = -1;
    private String mParent;
    private String mStart;
    private String mEnd;
    private String mItems;
    private int mId = -1;
    private boolean isEditMode = false;
    //    private TextView mTvNotionDate;
    private static final String[] mAlarmModes = new String[]{"每天", "自由选择"};
    private List<AlarmBean> mAlarmBeanList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mBundle = getArguments();
        mGoalName = mBundle.getString(GOAL_NAME);
        String parent = mBundle.getString(GOAL_PRENT);
        mStart = mBundle.getString(GOAL_START_DATE);
        mEnd = mBundle.getString(GOAL_END_DATE);
        mItems = mBundle.getString(GOAL_ITEMS);
        isEditMode = mBundle.getBoolean(EDIT_MODE, false);
        mAlarmBeanList = new ArrayList<>();

        if (parent != null && !parent.isEmpty()) {
            mParent = parent;
        } else {
            mParent = ROOT_PARENT;
        }
        mLevel = mBundle.getInt(GOAL_LEVEL);
        mId = mBundle.getInt(GOAL_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_create_plan, null);
        unbinder = ButterKnife.bind(this, mRootView);
        initView();
        return mRootView;
    }

    private void initView() {
        checkSetText(mTvGoalName, mGoalName);
        checkSetText(mTvStartDateShow, mStart);
        checkSetText(mTvEndDateShow, mEnd);
        if (mItems != null && !mItems.isEmpty()) {
            String[] mItems = this.mItems.split("\n");
            if (mItems != null && mItems.length > 0) {
                for (int i = 0; i < mItems.length; i++) {
                    addEditItemView(mItems[i]);
                }
            }
        }
        mSwNotion.setOnCheckedChangeListener(this);
    }

    private void checkSetText(TextView mTextView, String content) {
        if (!TextUtils.isEmpty(content)) {
            mTextView.setText(content);
        }
    }

    public static void showDialog(FragmentManager fm, int mLevel) {
        showDialog(fm, -1, null, null, mLevel, null, null, null, false);
    }

    public static void showDialog(FragmentManager fm, GoalBean mBean) {
        String mStart = mBean.getStart();
        String mOver = mBean.getOver();
        showDialog(fm, mBean.getId(), mBean.getTitle(), mBean.getParent(), mBean.getLevel(), mStart, mOver, mBean.getItems(), true);
    }

    public static void showDialog(FragmentManager fm, String mSubTitle, String mParentTitle, int mLevel, String start, String end) {
        showDialog(fm, -1, mSubTitle, mParentTitle, mLevel, start, end, null, false);
    }

    public static void showDialog(FragmentManager fm, int id, @Nullable String goalName, @Nullable String mParent, int level,
                                  String start, String end, String items, boolean isEditMode) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(FRAG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);


        Bundle arg = new Bundle();
        arg.putString(GOAL_NAME, goalName);
        arg.putString(GOAL_PRENT, mParent);
        arg.putString(GOAL_START_DATE, start);
        arg.putString(GOAL_END_DATE, end);
        arg.putString(GOAL_ITEMS, items);
        arg.putInt(GOAL_LEVEL, level);
        arg.putInt(GOAL_ID, id);
        arg.putBoolean(EDIT_MODE, isEditMode);
        DialogFragment newFragment = DialogFragmentCreatePlan.newInstance();
        newFragment.setArguments(arg);
        newFragment.show(ft, FRAG_TAG);
    }

    private static DialogFragment newInstance() {
        DialogFragmentCreatePlan mPlan = new DialogFragmentCreatePlan();
        mPlan.setCancelable(false);
        return mPlan;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void showDatePick(DatePickerDialog.OnDateSetListener mListener, String client) {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DATE);

        if (client != null && !TextUtils.isEmpty(client)) {
            String[] mStrings = client.split("/");
            if (mStrings != null && mStrings.length == 3) {
                year = Integer.parseInt(mStrings[0]);
                month = Integer.parseInt(mStrings[1]) - 1;
                day = Integer.parseInt(mStrings[2]);
            }
        }

        DatePickerDialog mDatePickerDialog = new DatePickerDialog(getActivity(), mListener, year, month, day);
        mDatePickerDialog.show();
    }

    @OnClick({R.id.tvStartDateShow, R.id.tvEndDateShow, R.id.btnAddItem, R.id.ivCancel, R.id.ivSave, R.id.tvNotion})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvStartDateShow:
                showDatePick((view1, year, month, dayOfMonth) -> {
                    month += 1;
                    mTvStartDateShow.setText(year + "/" + month + "/" + dayOfMonth);
                }, mStart);
                break;
            case R.id.tvEndDateShow:
                showDatePick((view12, year, month, dayOfMonth) -> {
                    month += 1;
                    mTvEndDateShow.setText(year + "/" + month + "/" + dayOfMonth);
                }, mEnd);
                break;
            case R.id.btnAddItem:
                addEditItemView(null);
                break;
            case R.id.ivCancel:
                DialogUtil.showDialog(getActivity(), () -> dismiss());
                break;
            case R.id.ivSave:
                startAlarm();
                saveData();
                break;
            case R.id.tvNotion:
                if (mSwNotion.isChecked() && mTvNotion.isChecked()) {
                    addNotionView();
                }
                break;
        }
    }

    private void startAlarm() {
        if (mSwNotion.isChecked() && mAlarmBeanList.size() > 0) {
            for (AlarmBean bean : mAlarmBeanList) {
                List<CalendarDay> mDates = bean.getSelectedDates();
                List<Integer> mRequestCodes = bean.getRequestCodes();
                if (mDates != null && mDates.size() > 0) {
                    if (mRequestCodes != null && mRequestCodes.size() > 0 && mRequestCodes.size() == mDates.size()) {
                        for (int i = 0; i < mDates.size(); i++) {
                            CalendarDay day = mDates.get(i);
                            Calendar mCalendar = Calendar.getInstance();
                            mCalendar.set(day.getYear(), day.getMonth(), day.getDay(),
                                    bean.getHour(), bean.getMinute());
                            alarm(mCalendar.getTimeInMillis(), mRequestCodes.get(i));
                        }
                    }
                } else {
                    // TODO: 2018/7/19 每天的情况
                }
            }
        }
    }

    private void alarm(long timeInMillis, int requestCode) {
        if (timeInMillis > System.currentTimeMillis()) {
            Intent intent = new Intent(getActivity(), AlarmBroadcastReceiver.class);
            Bundle mBundle = new Bundle();
            String mTitle = mTvGoalName.getText().toString().trim();
            if (!TextUtils.isEmpty(mTitle)) {
                mBundle.putString(TITLE, mTitle);
            }
            mBundle.putString(CONTENT, String.format("\uD83D\uDE00 千里之行，始于足下，行动吧", mTitle));
            intent.putExtras(mBundle);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    GoalApplication.getContext(), requestCode, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
            LogUtil.logi((timeInMillis - System.currentTimeMillis()) / 1000 + "");
            LogUtil.logi(requestCode + "");
        }
    }

    private void addNotionView() {
        final View mView = View.inflate(getActivity(), R.layout.layout_add_notion, null);

        AlarmBean mAlarmBean = new AlarmBean();
        mAlarmBean.setParentView(mView);
        mAlarmBeanList.add(mAlarmBean);

        mView.findViewById(R.id.ivCutNotion).setOnClickListener(v -> {
            mLlNotion.removeView((View) v.getParent());
            if (mAlarmBeanList != null && mAlarmBeanList.size() > 0) {
                for (AlarmBean bean : mAlarmBeanList) {
                    if (bean.getParentView() == v.getParent()) {
                        mAlarmBeanList.remove(bean);
                        break;
                    }
                }
            }
        });
        mView.findViewById(R.id.tvNotionTime).setOnClickListener(v -> showTimePickerDialog(v));

        mView.findViewById(R.id.tvNotionDate).setOnClickListener(v -> DialogUtil.showSingleChoiceDialog(getActivity(), mAlarmModes, (dialog, which) -> {
            dialog.dismiss();
            if (which == 1) {
                showMultipleDatePick(((TextView) v));
            } else {
                ((TextView) v).setText("每天");
            }
        }));

        mLlNotion.addView(mView);
    }

    private void showTimePickerDialog(final View mV) {
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        TimePickerDialog mDialog = new TimePickerDialog(getActivity(), (view, hourOfDay, minute1) -> {
            ((TextView) mV).setText(hourOfDay + ":" + minute1);
            if (mAlarmBeanList != null && mAlarmBeanList.size() > 0) {
                for (AlarmBean bean : mAlarmBeanList) {
                    if (bean.getParentView() == mV.getParent().getParent()) {
                        bean.setHour(hourOfDay);
                        bean.setMinute(minute1);
                        break;
                    }
                }
            }
        }, hour, minute, true);
        mDialog.show();
    }

    private void addEditItemView(@Nullable String content) {
        View mView = View.inflate(getActivity(), R.layout.edit_item_layout, null);
        EditText editItem = mView.findViewById(R.id.editItem);
        mView.findViewById(R.id.ivCutPlan).setOnClickListener(this);
        mItemViewList.add(editItem);
        checkSetText(editItem, content);
        int mChildCount = mLlParent.getChildCount();
        mLlParent.addView(mView, mChildCount - 1);
    }

    private void saveData() {
        Activity mActivity = getActivity();
        if (mActivity instanceof MainActivity) {
            MainActivity mMainActivity = (MainActivity) mActivity;
            DBHelper mDBHelper = mMainActivity.getDBHelper();

            String start = mTvStartDateShow.getText().toString().trim();
            String end = mTvEndDateShow.getText().toString();
            StringBuilder mBuilder = new StringBuilder();
            String title = mTvGoalName.getText().toString().trim();
            boolean isSuccess;
            for (EditText met : mItemViewList) {
                String mItem = met.getText().toString();
                mBuilder.append(mItem + "\n");
                if (mMainActivity.getDBHelper().isHadInDB(mItem) && !isEditMode) {
                    Toast.makeText(mMainActivity, String.format("%s计划已经在其他地方存档啦", mItem), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            String mItems = mBuilder.toString().trim();
            start.trim();
            end.trim();

            if (title.isEmpty() && mItems.isEmpty()) {
                Toast.makeText(mMainActivity, "写点啥再保存呗", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isEditMode) {
                isSuccess = mDBHelper.insertGoal(mLevel, mParent, title, start, end, mItems.trim(), System.currentTimeMillis(), 1);
            } else {
                isSuccess = mDBHelper.updateGoal(mLevel, mParent, title, start, end, mItems.trim(), System.currentTimeMillis(), 3);
            }
            Toast.makeText(mActivity, isSuccess ? "已保存" : "保存异常", Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                if (mLevel == 1) {
                    AppSp.putString(LATEST_GOAL, title);
                    SubMenu mSubMenu = mMainActivity.getGoalSubMenu();
                    if (mSubMenu != null) {
                        GoalBean mBean = mDBHelper.getGoalByTitle(title);
                        if (mBean != null) {
                            MenuItem mMenuItem = mSubMenu.findItem(mBean.getId());
                            if (mMenuItem == null) {
                                MenuItem mItem = mSubMenu.add(R.id.goal, mBean.getId(), mBean.getId(), title);
                                mItem.setIcon(R.mipmap.ic_attach_file_black_24dp);
                            }
                        }
                    }
                }
                dismiss();
                mMainActivity.notifyDataSetChange(title);
            }
        }
    }

    public void setEditMode(boolean mEditMode) {
        isEditMode = mEditMode;
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mTvNotion.setChecked(isChecked);
        if (isChecked && mLlNotion.getChildCount() == 1) {
            addNotionView();
        }

        if (isChecked) {
            mTvNotion.setText("添加提醒");
        } else {
            mTvNotion.setText("提醒");
        }

        if (!isChecked) {
            int mCount = mLlNotion.getChildCount();
            if (mCount > 1) {
                mLlNotion.removeViews(1, mCount - 1);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivCutPlan:
                mLlParent.removeView((View) v.getParent());
                mItemViewList.remove((View) ((View) v.getParent()).findViewById(R.id.editItem));
                break;
        }
    }

    private void showMultipleDatePick(TextView mV) {
        final MaterialCalendarView mCalendarView = new MaterialCalendarView(getActivity());
        mCalendarView.setSelectionMode(SELECTION_MODE_MULTIPLE);
        DialogUtil.showDialogWithView(getActivity(), mCalendarView, "no", "ok", new DialogUtil.DialogListener() {
            @Override
            public void onNegClickListener() {

            }

            @Override
            public void onPosClickListener() {
                List<CalendarDay> mSelectedDates = mCalendarView.getSelectedDates();
                List<Integer> randomRequestCode = new ArrayList<>();
                StringBuilder mStringBuilder = new StringBuilder();
                for (CalendarDay day : mSelectedDates) {
                    int mYear = day.getYear();
                    int mMonth = day.getMonth() + 1;
                    int mDay = day.getDay();
                    mStringBuilder.append(mYear + "/" + mMonth + "/" + mDay + " ");
                }
                if (mV != null) {
                    mV.setText(mStringBuilder.toString().trim());
                }
                if (mAlarmBeanList != null && mAlarmBeanList.size() > 0) {
                    Random rand = new Random();
                    for (AlarmBean bean : mAlarmBeanList) {
                        if (bean.getParentView() == mV.getParent().getParent()) {
                            randomRequestCode.add(rand.nextInt(100000));
                            bean.setSelectedDates(mSelectedDates);
                            bean.setRequestCodes(randomRequestCode);
                            break;
                        }
                    }
                }
            }
        });
    }
}
