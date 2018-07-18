package club.peiyan.goaltrack.plan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import club.peiyan.goaltrack.MainActivity;
import club.peiyan.goaltrack.R;
import club.peiyan.goaltrack.data.DBHelper;
import club.peiyan.goaltrack.data.GoalBean;
import club.peiyan.goaltrack.utils.AppSp;
import club.peiyan.goaltrack.utils.DialogUtil;

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
    private AlertDialog mSelectModeDialog;
    private TextView mTvNotionDate;

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
                saveData();
                break;
            case R.id.tvNotion:
                if (mSwNotion.isChecked() && mTvNotion.isChecked()) {
                    addNotionView();
                }
                break;
        }
    }

    private void addNotionView() {
        final View mView = View.inflate(getActivity(), R.layout.layout_add_notion, null);
        mView.findViewById(R.id.ivCutNotion).setOnClickListener(this);
        TextView tvNotionTime = mView.findViewById(R.id.tvNotionTime);
        tvNotionTime.setOnClickListener(this);

        mTvNotionDate = mView.findViewById(R.id.tvNotionDate);
        mTvNotionDate.setOnClickListener(this);

        mLlNotion.addView(mView);
    }

    private void showTimePickerDialog(final View mV) {
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        TimePickerDialog mDialog = new TimePickerDialog(getActivity(), (view, hourOfDay, minute1) -> ((TextView) mV).setText(hourOfDay + ":" + minute1), hour, minute, false);
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
            case R.id.ivCutNotion:
                mLlNotion.removeView((View) v.getParent());
                break;
            case R.id.tvNotionDate:
                View mView = View.inflate(getActivity(), R.layout.layout_notion_which_date_mode, null);
                mView.findViewById(R.id.tvLiterary).setOnClickListener(this);
                mView.findViewById(R.id.tvEveryDay).setOnClickListener(this);
                mSelectModeDialog = DialogUtil.showDolViewWithoutButton(getActivity(), mView);
                break;
            case R.id.tvNotionTime:
                showTimePickerDialog(v);
                break;

            case R.id.tvEveryDay:
                if (mSelectModeDialog != null) mSelectModeDialog.dismiss();
                break;

            case R.id.tvLiterary:
                if (mSelectModeDialog != null) mSelectModeDialog.dismiss();
                showMultipleDatePick();
                break;
            case R.id.ivCutPlan:
                mLlParent.removeView((View) v.getParent());
                mItemViewList.remove((View) ((View) v.getParent()).findViewById(R.id.editItem));
                break;
        }
    }

    private void showMultipleDatePick() {
        final MaterialCalendarView mCalendarView = new MaterialCalendarView(getActivity());
        mCalendarView.setSelectionMode(SELECTION_MODE_MULTIPLE);
        DialogUtil.showDialogWithView(getActivity(), mCalendarView, "no", "ok", new DialogUtil.DialogListener() {
            @Override
            public void onNegClickListener() {

            }

            @Override
            public void onPosClickListener() {
                List<CalendarDay> mSelectedDates = mCalendarView.getSelectedDates();
                StringBuilder mStringBuilder = new StringBuilder();
                for (CalendarDay day : mSelectedDates) {
                    int mYear = day.getYear();
                    int mMonth = day.getMonth();
                    int mDay = day.getDay();
                    mStringBuilder.append(mYear + "/" + mMonth + "/" + mDay + " ");
                }
                if (mTvNotionDate != null) {
                    mTvNotionDate.setText(mStringBuilder.toString().trim());
                }
            }
        });
    }
}
