package club.peiyan.goaltrack.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HPY.
 * Time: 2018/7/8.
 * Desc:
 */

public class ScoreBean implements Parcelable {
    private int id;
    private int level;
    private String parent;
    private String title;
    private String date;
    private int score;
    private long timestamp;

    public ScoreBean() {
    }

    protected ScoreBean(Parcel in) {
        id = in.readInt();
        level = in.readInt();
        parent = in.readString();
        title = in.readString();
        date = in.readString();
        score = in.readInt();
        timestamp = in.readLong();
    }

    public static final Creator<ScoreBean> CREATOR = new Creator<ScoreBean>() {
        @Override
        public ScoreBean createFromParcel(Parcel in) {
            return new ScoreBean(in);
        }

        @Override
        public ScoreBean[] newArray(int size) {
            return new ScoreBean[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int mId) {
        id = mId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int mLevel) {
        level = mLevel;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String mParent) {
        parent = mParent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String mTitle) {
        title = mTitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String mDate) {
        date = mDate;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int mScore) {
        score = mScore;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long mTimestamp) {
        timestamp = mTimestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(level);
        dest.writeString(parent);
        dest.writeString(title);
        dest.writeString(date);
        dest.writeInt(score);
        dest.writeLong(timestamp);
    }
}
