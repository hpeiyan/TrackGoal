package club.peiyan.goaltrack.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by HPY.
 * Time: 2018/7/8.
 * Desc:
 */

public class ScoreList implements Parcelable {

    private ArrayList<ScoreBean> mScoreBeans;

    public ScoreList(ArrayList<ScoreBean> mScoreBeans) {
        this.mScoreBeans = mScoreBeans;
    }

    protected ScoreList(Parcel in) {
        mScoreBeans = in.createTypedArrayList(ScoreBean.CREATOR);
    }

    public static final Creator<ScoreList> CREATOR = new Creator<ScoreList>() {
        @Override
        public ScoreList createFromParcel(Parcel in) {
            return new ScoreList(in);
        }

        @Override
        public ScoreList[] newArray(int size) {
            return new ScoreList[size];
        }
    };

    public ArrayList<ScoreBean> getScoreBeans() {
        return mScoreBeans;
    }

    public void setScoreBeans(ArrayList<ScoreBean> mScoreBeans) {
        this.mScoreBeans = mScoreBeans;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mScoreBeans);
    }
}