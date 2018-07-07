package club.peiyan.goaltrack.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by HPY.
 * Time: 2018/7/6.
 * Desc:
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserTrackGoal.db";
    public static final String TRACK_GOAL_TABLE = "track_goal";

    public static final String TRACK_GOAL_ID = "id";
    public static final String TRACK_GOAL_PARENT = "parent";
    public static final String TRACK_GOAL_LEVEL = "level";
    public static final String TRACK_GOAL_TITLE = "title";
    public static final String TRACK_GOAL_START_TIME = "start";
    public static final String TRACK_GOAL_END_TIME = "over";
    public static final String TRACK_GOAL_ITEMS = "items";
    public static final String TRACK_GOAL_TIMESTAMP = "timestamp";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table track_goal " +
                        "(id integer primary key, parent text,level integer,title text,start text, over text,items text,timestamp integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS track_goal");
        onCreate(db);
    }

    public boolean insertGoal(int level, String parent, String title, String start, String over, String items, long timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRACK_GOAL_LEVEL, level);
        contentValues.put(TRACK_GOAL_PARENT, parent);
        contentValues.put(TRACK_GOAL_TITLE, title);
        contentValues.put(TRACK_GOAL_START_TIME, start);
        contentValues.put(TRACK_GOAL_END_TIME, over);
        contentValues.put(TRACK_GOAL_ITEMS, items);
        contentValues.put(TRACK_GOAL_TIMESTAMP, timestamp);
        db.insert(TRACK_GOAL_TABLE, null, contentValues);
        return true;
    }


    public ArrayList<GoalBean> getGoal(int level) {
        ArrayList<GoalBean> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from track_goal where level=" + level + "", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            GoalBean mBean = new GoalBean();
            mBean.setId(res.getInt(res.getColumnIndex(TRACK_GOAL_ID)));
            mBean.setLevel(res.getInt(res.getColumnIndex(TRACK_GOAL_LEVEL)));
            mBean.setParent(res.getString(res.getColumnIndex(TRACK_GOAL_PARENT)));
            mBean.setTitle(res.getString(res.getColumnIndex(TRACK_GOAL_TITLE)));
            mBean.setStart(res.getString(res.getColumnIndex(TRACK_GOAL_START_TIME)));
            mBean.setOver(res.getString(res.getColumnIndex(TRACK_GOAL_END_TIME)));
            mBean.setItems(res.getString(res.getColumnIndex(TRACK_GOAL_ITEMS)));
            mBean.setTimestamp(res.getInt(res.getColumnIndex(TRACK_GOAL_TIMESTAMP)));
            array_list.add(mBean);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<GoalBean> getParentGoal() {
        ArrayList<GoalBean> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from track_goal where level=" + "1" + "", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            GoalBean mBean = new GoalBean();
            mBean.setId(res.getInt(res.getColumnIndex(TRACK_GOAL_ID)));
            mBean.setLevel(res.getInt(res.getColumnIndex(TRACK_GOAL_LEVEL)));
            mBean.setParent(res.getString(res.getColumnIndex(TRACK_GOAL_PARENT)));
            mBean.setTitle(res.getString(res.getColumnIndex(TRACK_GOAL_TITLE)));
            mBean.setStart(res.getString(res.getColumnIndex(TRACK_GOAL_START_TIME)));
            mBean.setOver(res.getString(res.getColumnIndex(TRACK_GOAL_END_TIME)));
            mBean.setItems(res.getString(res.getColumnIndex(TRACK_GOAL_ITEMS)));
            mBean.setTimestamp(res.getInt(res.getColumnIndex(TRACK_GOAL_TIMESTAMP)));
            array_list.add(mBean);
            res.moveToNext();
        }
        return array_list;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TRACK_GOAL_TABLE);
        return numRows;
    }

    public boolean updateGoal(Integer id,int level, String parent, String title, String start, String over, String items, long timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRACK_GOAL_LEVEL, level);
        contentValues.put(TRACK_GOAL_PARENT, parent);
        contentValues.put(TRACK_GOAL_TITLE, title);
        contentValues.put(TRACK_GOAL_START_TIME, start);
        contentValues.put(TRACK_GOAL_END_TIME, over);
        contentValues.put(TRACK_GOAL_ITEMS, items);
        contentValues.put(TRACK_GOAL_TIMESTAMP, timestamp);
        db.update(TRACK_GOAL_TABLE, contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deleteGoal(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TRACK_GOAL_TABLE,
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList<GoalBean> getAllGoals() {
        ArrayList<GoalBean> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from track_goal", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            GoalBean mBean = new GoalBean();
            mBean.setId(res.getInt(res.getColumnIndex(TRACK_GOAL_ID)));
            mBean.setLevel(res.getInt(res.getColumnIndex(TRACK_GOAL_LEVEL)));
            mBean.setParent(res.getString(res.getColumnIndex(TRACK_GOAL_PARENT)));
            mBean.setTitle(res.getString(res.getColumnIndex(TRACK_GOAL_TITLE)));
            mBean.setStart(res.getString(res.getColumnIndex(TRACK_GOAL_START_TIME)));
            mBean.setOver(res.getString(res.getColumnIndex(TRACK_GOAL_END_TIME)));
            mBean.setItems(res.getString(res.getColumnIndex(TRACK_GOAL_ITEMS)));
            mBean.setTimestamp(res.getInt(res.getColumnIndex(TRACK_GOAL_TIMESTAMP)));
            array_list.add(mBean);
            res.moveToNext();
        }
        return array_list;
    }

    @Nullable
    public GoalBean getGoal(String title) {
        ArrayList<GoalBean> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from track_goal where title='" + title + "'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            GoalBean mBean = new GoalBean();
            mBean.setId(res.getInt(res.getColumnIndex(TRACK_GOAL_ID)));
            mBean.setLevel(res.getInt(res.getColumnIndex(TRACK_GOAL_LEVEL)));
            mBean.setParent(res.getString(res.getColumnIndex(TRACK_GOAL_PARENT)));
            mBean.setTitle(res.getString(res.getColumnIndex(TRACK_GOAL_TITLE)));
            mBean.setStart(res.getString(res.getColumnIndex(TRACK_GOAL_START_TIME)));
            mBean.setOver(res.getString(res.getColumnIndex(TRACK_GOAL_END_TIME)));
            mBean.setItems(res.getString(res.getColumnIndex(TRACK_GOAL_ITEMS)));
            mBean.setTimestamp(res.getInt(res.getColumnIndex(TRACK_GOAL_TIMESTAMP)));
            array_list.add(mBean);
            res.moveToNext();
        }
        if (array_list.size() > 0)
            return array_list.get(0);
        return null;
    }

    public GoalBean getNearestParentGoal() {
        ArrayList<GoalBean> mParentGoals = getGoal(1);
        if (mParentGoals == null || mParentGoals.size() == 0) return null;

        int index = 0;
        long deltaTime = mParentGoals.get(0).getTimestamp();
        for (int i = 0; i < mParentGoals.size(); i++) {
            long mTimestamp = mParentGoals.get(i).getTimestamp();
            if (mTimestamp > deltaTime) {
//                顺序排序
                index = i;
                deltaTime = mTimestamp;
            }
        }
        return mParentGoals.get(index);
    }

    public ArrayList<GoalBean> getSubAndContactParentGoal(GoalBean mNearestParentGoal) {
        String[] mItems = mNearestParentGoal.getItems().split("\n");
        ArrayList<GoalBean> mSingleAllGoalBeans = new ArrayList<>();
        mSingleAllGoalBeans.add(mNearestParentGoal);
        if (mItems != null && mItems.length > 0) {
            for (String item : mItems) {
                mSingleAllGoalBeans.add(getGoal(item));
            }
        }
        return mSingleAllGoalBeans;
    }

    public ArrayList<GoalBean> getSingleAllGoal(GoalBean mNearestParentGoal) {
        String[] mItems = mNearestParentGoal.getItems().split("\n");
        ArrayList<GoalBean> mSingleAllGoalBeans = new ArrayList<>();
        mSingleAllGoalBeans.add(mNearestParentGoal);// level 1

        if (mItems != null && mItems.length > 0) {
            for (String title1 : mItems) {
                // level 2
                GoalBean mGoal2 = getGoal(title1);
                if (mGoal2 == null) continue;
                mSingleAllGoalBeans.add(mGoal2);

                String[] mSplit2 = mGoal2.getItems().split("\n");
                for (String title2 : mSplit2) {
                    //level 3
                    GoalBean mGoal3 = getGoal(title2);
                    if (mGoal3 == null) continue;
                    mSingleAllGoalBeans.add(mGoal3);

                    String[] mSplit3 = mGoal3.getItems().split("\n");
                    for (String title3 : mSplit3) {
                        //level 4
                        GoalBean mGoal4 = getGoal(title3);
                        if (mGoal4 == null) continue;
                        mSingleAllGoalBeans.add(mGoal4);

                        String[] mSplit4 = mGoal4.getItems().split("\n");
                        for (String title4 : mSplit4) {
                            //level 5
                            GoalBean mGoal5 = getGoal(title4);
                            if (mGoal5 == null) continue;
                            mSingleAllGoalBeans.add(mGoal5);
                        }
                    }
                }
            }
        }
        return mSingleAllGoalBeans;
    }

    public boolean isHasSubGoal(GoalBean mGoalBean) {
        String[] mItems = mGoalBean.getItems().split("\n");
        if (mItems != null && mItems.length > 0) {
            for (String item : mItems) {
                GoalBean mGoal = getGoal(item);
                if (mGoal != null) return true;
            }
        }
        return false;
    }


    public boolean isHadInDB(String mItem) {
        GoalBean mGoal = getGoal(mItem);
        return mGoal != null;
    }
}
