package club.peiyan.goaltrack.data;

import android.view.View;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;

/**
 * Created by HPY.
 * Time: 2018/7/18.
 * Desc:
 */

public class AlarmBean {
    private View parentView;
    private String day = "每天";
    private int hour = 9;
    private int minute = 0;
    private List<CalendarDay> mSelectedDates;
    private List<Integer> mRequestCodes;

    public View getParentView() {
        return parentView;
    }

    public void setParentView(View mParentView) {
        parentView = mParentView;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String mDay) {
        day = mDay;
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
}
