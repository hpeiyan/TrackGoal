package club.peiyan.goaltrack.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

import club.peiyan.goaltrack.utils.CalendaUtils;

/**
 * Created by HPY.
 * Time: 2018/7/6.
 * Desc:
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserTrackGoal.db";
    public static final String TRACK_GOAL_TABLE = "track_goal";
    public static final String GOAL_SCORE_TABLE = "goal_score";

    public static final String TRACK_GOAL_ID = "id";
    public static final String TRACK_GOAL_PARENT = "parent";
    public static final String TRACK_GOAL_LEVEL = "level";
    public static final String TRACK_GOAL_TITLE = "title";
    public static final String TRACK_GOAL_START_TIME = "start";
    public static final String TRACK_GOAL_END_TIME = "over";
    public static final String TRACK_GOAL_ITEMS = "items";
    public static final String TRACK_GOAL_TIMESTAMP = "timestamp";

    public static final String GOAL_SCORE_ID = "id";
    public static final String GOAL_SCORE_DATE = "date";
    public static final String GOAL_SCORE_PARENT = "parent";
    public static final String GOAL_SCORE_LEVEL = "level";
    public static final String GOAL_SCORE_TITLE = "title";
    public static final String GOAL_SCORE_SCORE = "score";
    public static final String GOAL_SCORE_TIMESTAMP = "timestamp";

    private static final String TAG = "DBHelper";
    private final Context mContext;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table track_goal " +
                        "(id integer primary key, parent text,level integer,title text,start text, over text,items text,timestamp integer,status integer)"
        );

        db.execSQL(
                "create table goal_score " +
                        "(id integer primary key, date text,parent text,level integer,title text,score integer,timestamp integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS track_goal");
//        onCreate(db);
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


    public ArrayList<GoalBean> getGoalByLevel(int level) {
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

    public GoalBean getGoalByID(int id) {
        ArrayList<GoalBean> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from track_goal where id=" + id + "", null);
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
        if (array_list.size() > 0) {
            return array_list.get(0);
        }
        return null;
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

    public boolean updateGoal(Integer id, int level, String parent, String title, String start, String over, String items, long timestamp) {
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
    public GoalBean getGoalByTitle(String title) {
        if (title == null || TextUtils.isEmpty(title)) return null;

        ArrayList<GoalBean> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from track_goal where title=?", new String[]{title});
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
        ArrayList<GoalBean> mParentGoals = getGoalByLevel(1);
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
        if (mItems != null && mItems.length > 0) {
            for (String item : mItems) {
                GoalBean mGoalBean = getGoalByTitle(item);
                if (mGoalBean != null) {
                    mSingleAllGoalBeans.add(mGoalBean);
                }
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
                GoalBean mGoal2 = getGoalByTitle(title1);
                if (mGoal2 == null) continue;
                mSingleAllGoalBeans.add(mGoal2);

                String[] mSplit2 = mGoal2.getItems().split("\n");
                for (String title2 : mSplit2) {
                    //level 3
                    GoalBean mGoal3 = getGoalByTitle(title2);
                    if (mGoal3 == null) continue;
                    mSingleAllGoalBeans.add(mGoal3);

                    String[] mSplit3 = mGoal3.getItems().split("\n");
                    for (String title3 : mSplit3) {
                        //level 4
                        GoalBean mGoal4 = getGoalByTitle(title3);
                        if (mGoal4 == null) continue;
                        mSingleAllGoalBeans.add(mGoal4);

                        String[] mSplit4 = mGoal4.getItems().split("\n");
                        for (String title4 : mSplit4) {
                            //level 5
                            GoalBean mGoal5 = getGoalByTitle(title4);
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
                GoalBean mGoal = getGoalByTitle(item);
                if (mGoal != null) return true;
            }
        }
        return false;
    }


    public boolean isHadInDB(String mItem) {
        GoalBean mGoal = getGoalByTitle(mItem);
        return mGoal != null;
    }

    /**
     * 以上是 track_goal 的表操作
     * 以下是 goal_score 的操作
     */
    /*
    *  db.execSQL(
                "create table goal_achieve " +
                        "(id integer primary key, date text,parent text,level integer,title text,score integer,timestamp integer)"
        );
    * */
    public boolean insertScore(int level, String parent, String title, String date, int score, long timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GOAL_SCORE_DATE, date);
        contentValues.put(GOAL_SCORE_LEVEL, level);
        contentValues.put(GOAL_SCORE_PARENT, parent);
        contentValues.put(GOAL_SCORE_TITLE, title);
        contentValues.put(GOAL_SCORE_SCORE, score);
        contentValues.put(GOAL_SCORE_TIMESTAMP, timestamp);
        db.insert(GOAL_SCORE_TABLE, null, contentValues);
        return true;
    }

    public boolean updateScore(Integer id, int level, String parent, String title, String date, int score, long timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GOAL_SCORE_DATE, date);
        contentValues.put(GOAL_SCORE_LEVEL, level);
        contentValues.put(GOAL_SCORE_PARENT, parent);
        contentValues.put(GOAL_SCORE_TITLE, title);
        contentValues.put(GOAL_SCORE_SCORE, score);
        contentValues.put(GOAL_SCORE_TIMESTAMP, timestamp);
        db.update(GOAL_SCORE_TABLE, contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }


    @Nullable
    public ScoreBean getScoreByTitle(String title) {
        if (title == null || TextUtils.isEmpty(title)) return null;

        ArrayList<ScoreBean> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from goal_score where title=?", new String[]{title});
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            ScoreBean mBean = new ScoreBean();
            mBean.setId(res.getInt(res.getColumnIndex(GOAL_SCORE_ID)));
            mBean.setLevel(res.getInt(res.getColumnIndex(GOAL_SCORE_LEVEL)));
            mBean.setParent(res.getString(res.getColumnIndex(GOAL_SCORE_PARENT)));
            mBean.setTitle(res.getString(res.getColumnIndex(GOAL_SCORE_TITLE)));
            mBean.setDate(res.getString(res.getColumnIndex(GOAL_SCORE_DATE)));
            mBean.setScore(res.getInt(res.getColumnIndex(GOAL_SCORE_SCORE)));
            mBean.setTimestamp(res.getInt(res.getColumnIndex(GOAL_SCORE_TIMESTAMP)));
            array_list.add(mBean);
            res.moveToNext();
        }
        if (array_list.size() > 0)
            return array_list.get(0);
        return null;
    }

    public Integer deleteScore(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(GOAL_SCORE_TABLE,
                "title = ? ",
                new String[]{title});
    }

    public ArrayList<ScoreBean> getScoreByTime(String mDay) {
        ArrayList<ScoreBean> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from goal_score where date=?", new String[]{mDay});
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            ScoreBean mBean = new ScoreBean();
            mBean.setId(res.getInt(res.getColumnIndex(GOAL_SCORE_ID)));
            mBean.setLevel(res.getInt(res.getColumnIndex(GOAL_SCORE_LEVEL)));
            mBean.setParent(res.getString(res.getColumnIndex(GOAL_SCORE_PARENT)));
            mBean.setTitle(res.getString(res.getColumnIndex(GOAL_SCORE_TITLE)));
            mBean.setDate(res.getString(res.getColumnIndex(GOAL_SCORE_DATE)));
            mBean.setScore(res.getInt(res.getColumnIndex(GOAL_SCORE_SCORE)));
            mBean.setTimestamp(res.getInt(res.getColumnIndex(GOAL_SCORE_TIMESTAMP)));
            array_list.add(mBean);
            res.moveToNext();
        }
        if (array_list.size() > 0)
            return array_list;
        return null;
    }
}
