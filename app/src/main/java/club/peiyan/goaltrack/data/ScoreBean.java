package club.peiyan.goaltrack.data;

/**
 * Created by HPY.
 * Time: 2018/7/8.
 * Desc:
 */

public class ScoreBean {
    private int id;
    private int level;
    private String parent;
    private String title;
    private String date;
    private int score;
    private long timestamp;

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
}
