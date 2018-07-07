package club.peiyan.goaltrack.data;

/**
 * Created by HPY.
 * Time: 2018/7/6.
 * Desc:
 */

public class GoalBean {
    private int id;
    private int level;
    private String parent;
    private String title;
    private String start;
    private String over;
    private String items;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String mTitle) {
        title = mTitle;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String mStart) {
        start = mStart;
    }

    public String getOver() {
        return over;
    }

    public void setOver(String mOver) {
        over = mOver;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String mItems) {
        items = mItems;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String mParent) {
        parent = mParent;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long mTimestamp) {
        timestamp = mTimestamp;
    }
}
