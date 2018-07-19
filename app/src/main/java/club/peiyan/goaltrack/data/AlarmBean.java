package club.peiyan.goaltrack.data;

import android.view.View;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;
import java.util.Random;

/**
 * Created by HPY.
 * Time: 2018/7/18.
 * Desc:
 */

public class AlarmBean {
    private View parentView;
    private int hour = 9;
    private int minute = 0;
    private int requestCode = new Random().nextInt(100000);//每天模式的RequestCode
    private List<CalendarDay> mSelectedDates;//自由选择模式的选择日期
    private List<Integer> mRequestCodes;//自由选择模式的RequestCode

    public View getParentView() {
        return parentView;
    }

    public void setParentView(View mParentView) {
        parentView = mParentView;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int mHour) {
        hour = mHour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int mMinute) {
        minute = mMinute;
    }

    public List<CalendarDay> getSelectedDates() {
        return mSelectedDates;
    }

    public void setSelectedDates(List<CalendarDay> mSelectedDates) {
        this.mSelectedDates = mSelectedDates;
    }

    public List<Integer> getRequestCodes() {
        return mRequestCodes;
    }

    public void setRequestCodes(List<Integer> mRequestCodes) {
        this.mRequestCodes = mRequestCodes;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int mRequestCode) {
        requestCode = mRequestCode;
    }
}
