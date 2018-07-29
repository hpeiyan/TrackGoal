package club.peiyan.goaltrack.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.List;

import club.peiyan.goaltrack.utils.AppSp;
import club.peiyan.goaltrack.utils.CalendaUtils;
import club.peiyan.goaltrack.utils.ListUtil;

import static club.peiyan.goaltrack.data.Constants.USER_NAME;

/**
 * Created by HPY.
 * Time: 2018/7/6.
 * Desc:
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserTrackGoal.db";
    public static final String TRACK_GOAL_TABLE = "track_goal";
    public static final String GOAL_SCORE_TABLE = "goal_score";
    public static final String GOAL_ALARM_TABLE = "goal_alarm";

    public static final String TRACK_GOAL_ID = "id";
    public static final String TRACK_GOAL_PARENT = "parent";
    public static final String TRACK_GOAL_LEVEL = "level";
    public static final String TRACK_GOAL_TITLE = "title";
    public static final String TRACK_GOAL_START_TIME = "start";
    public static final String TRACK_GOAL_END_TIME = "over";
    public static final String TRACK_GOAL_ITEMS = "items";
    public static final String TRACK_GOAL_TIMESTAMP = "timestamp";
    public static final String TRACK_GOAL_STATUS = "status";
    public static final String TRACK_GOAL_USER_NAME = "username";

    public static final String GOAL_SCORE_ID = "id";
    public static final String GOAL_SCORE_DATE = "date";
    public static final String GOAL_SCORE_PARENT = "parent";
    public static final String GOAL_SCORE_LEVEL = "level";
    public static final String GOAL_SCORE_TITLE = "title";
    public static final String GOAL_SCORE_SCORE = "score";
    public static final String GOAL_SCORE_TIMESTAMP = "timestamp";


    public static final String GOAL_ALARM_ID = "id";
    public static final String GOAL_ALARM_TITLE = "title";
    public static final String GOAL_ALARM_PARENT = "parent";
    public static final String GOAL_ALARM_HOUR = "hour";
    public static final String GOAL_ALARM_MINUTE = "minute";
    public static final String GOAL_ALARM_REQUEST_CODE = "requestCode";
    public static final String GOAL_ALARM_SELECT_DATES = "select_dates";
    public static final String GOAL_ALARM_REQUEST_CODES = "request_codes";

    private static final String TAG = "DBHelper";
    private final Context mContext;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table track_goal " +
                        "(id integer primary key, parent text,level integer,title text,start text, over text,items text,timestamp integer,status integer,username text)"
        );

        db.execSQL(
                "create table goal_score " +
                        "(id integer primary key, date text,parent text,level integer,title text,score integer,timestamp integer)"
        );

        db.execSQL(
                "create table goal_alarm " +
                        "(id integer primary key, title text,parent text,hour integer,minute integer,requestCode integer,select_dates text,request_codes text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS track_goal");
        db.execSQL("DROP TABLE IF EXISTS goal_score");
        db.execSQL("DROP TABLE IF EXISTS goal_alarm");
        onCreate(db);
    }

    public boolean insertGoal(int level, String parent, String title, String start, String over, String items, long timestamp, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRACK_GOAL_LEVEL, level);
        contentValues.put(TRACK_GOAL_PARENT, parent);
        contentValues.put(TRACK_GOAL_TITLE, title);
        contentValues.put(TRACK_GOAL_START_TIME, start);
        contentValues.put(TRACK_GOAL_END_TIME, over);
        contentValues.put(TRACK_GOAL_ITEMS, items);
        contentValues.put(TRACK_GOAL_TIMESTAMP, timestamp);
        contentValues.put(TRACK_GOAL_STATUS, status);
        contentValues.put(TRACK_GOAL_USER_NAME, AppSp.getString(USER_NAME, ""));
        db.insert(TRACK_GOAL_TABLE, null, contentValues);
        return true;
    }


    public ArrayList<GoalBean> getGoalByLevel(int level) {
        ArrayList<GoalBean> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from track_goal where level=" + level + " and status != 2", null);
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
            mBean.setStatus(res.getInt(res.getColumnIndex(TRACK_GOAL_STATUS)));
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
            mBean.setStatus(res.getInt(res.getColumnIndex(TRACK_GOAL_STATUS)));
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

    public void insertGoal(GoalBean mBean) {
        insertGoal(mBean.getLevel(), mBean.getParent(), mBean.getTitle(), mBean.getStart(),
                mBean.getOver(), mBean.getItems(), mBean.getTimestamp(), mBean.getStatus());
    }

    public void updateGoal(GoalBean mBean) {
        updateGoal(mBean.getLevel(), mBean.getParent(), mBean.getTitle(), mBean.getStart(),
                mBean.getOver(), mBean.getItems(), mBean.getTimestamp(), mBean.getStatus());
    }

    public boolean updateGoal(int level, String parent, String title, String start, String over, String items, long timestamp, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRACK_GOAL_LEVEL, level);
        contentValues.put(TRACK_GOAL_PARENT, parent);
        contentValues.put(TRACK_GOAL_TITLE, title);
        contentValues.put(TRACK_GOAL_START_TIME, start);
        contentValues.put(TRACK_GOAL_END_TIME, over);
        contentValues.put(TRACK_GOAL_ITEMS, items);
        contentValues.put(TRACK_GOAL_TIMESTAMP, timestamp);
        contentValues.put(TRACK_GOAL_STATUS, status);
        contentValues.put(TRACK_GOAL_USER_NAME, AppSp.getString(Constants.USER_NAME, ""));
        db.update(TRACK_GOAL_TABLE, contentValues, "title = ? ", new String[]{title});
        return true;
    }

    public Integer deleteGoal(String mTitle) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TRACK_GOAL_TABLE,
                "title = ? ",
                new String[]{mTitle});
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
            mBean.setStatus(res.getInt(res.getColumnIndex(TRACK_GOAL_STATUS)));
            array_list.add(mBean);
            res.moveToNext();
        }
        return array_list;
    }

    @Nullable
    public GoalBean getGoalByTitle(String title, String mRootParent) {
        if (title == null || TextUtils.isEmpty(title)) return null;

        ArrayList<GoalBean> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from track_goal where title=? and status != ? and parent=?", new String[]{title, "2", mRootParent});
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
            mBean.setStatus(res.getInt(res.getColumnIndex(TRACK_GOAL_STATUS)));
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
        if (mNearestParentGoal == null) return null;
        String[] mItems = mNearestParentGoal.getItems().split("\n");
        ArrayList<GoalBean> mSingleAllGoalBeans = new ArrayList<>();
        if (mItems != null && mItems.length > 0) {
            for (String item : mItems) {
                GoalBean mGoalBean = getGoalByTitle(item, mNearestParentGoal.getTitle());
                if (mGoalBean != null) {
                    mSingleAllGoalBeans.add(mGoalBean);
                }
            }
        }
        return mSingleAllGoalBeans;
    }

    public ArrayList<GoalBean> getSingleAllGoal(GoalBean mNearestParentGoal) {
        if (mNearestParentGoal.getItems() == null) return null;
        String[] mItems = mNearestParentGoal.getItems().split("\n");
        ArrayList<GoalBean> mSingleAllGoalBeans = new ArrayList<>();
        mSingleAllGoalBeans.add(mNearestParentGoal);// level 1

        if (mItems != null && mItems.length > 0) {
            for (String title1 : mItems) {
                // level 2
                GoalBean mGoal2 = getGoalByTitle(title1, mNearestParentGoal.getTitle());
                if (mGoal2 == null) continue;
                mSingleAllGoalBeans.add(mGoal2);

                String[] mSplit2 = mGoal2.getItems().split("\n");
                for (String title2 : mSplit2) {
                    //level 3
                    GoalBean mGoal3 = getGoalByTitle(title2, mGoal2.getTitle());
                    if (mGoal3 == null) continue;
                    mSingleAllGoalBeans.add(mGoal3);

                    String[] mSplit3 = mGoal3.getItems().split("\n");
                    for (String title3 : mSplit3) {
                        //level 4
                        GoalBean mGoal4 = getGoalByTitle(title3, mGoal3.getTitle());
                        if (mGoal4 == null) continue;
                        mSingleAllGoalBeans.add(mGoal4);

                        String[] mSplit4 = mGoal4.getItems().split("\n");
                        for (String title4 : mSplit4) {
                            //level 5
                            GoalBean mGoal5 = getGoalByTitle(title4, mGoal4.getTitle());
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
                GoalBean mGoal = getGoalByTitle(item, mGoalBean.getTitle());
                if (mGoal != null) return true;
            }
        }
        return false;
    }


    public boolean isParentHadInDB(String mItem) {
        GoalBean mGoal = getGoalByTitle(mItem, "rootParent");
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
    public boolean insertScore(int level, String parent, String title, String date, long score, long timestamp) {
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

    public boolean updateScore(int level, String parent, String title, String date, long deltaScore, long timestamp) {
        ScoreBean mScoreBean = getScoreByTitle(title, parent);
        if (mScoreBean == null) {
            insertScore(level, parent, title, date, deltaScore, timestamp);
        } else {
            deltaScore += mScoreBean.getScore();
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(GOAL_SCORE_DATE, date);
            contentValues.put(GOAL_SCORE_LEVEL, level);
            contentValues.put(GOAL_SCORE_PARENT, parent);
            contentValues.put(GOAL_SCORE_TITLE, title);
            contentValues.put(GOAL_SCORE_SCORE, deltaScore);
            contentValues.put(GOAL_SCORE_TIMESTAMP, timestamp);
            db.update(GOAL_SCORE_TABLE, contentValues, "title = ? and parent = ? ", new String[]{title, parent});
        }
        return true;
    }


    @Nullable
    public ScoreBean getScoreByTitle(String title, String mParent) {
        if (title == null || TextUtils.isEmpty(title)) return null;

        ArrayList<ScoreBean> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from goal_score where title=? and parent=?", new String[]{title, mParent});
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

    @Nullable
    public ScoreBean getScoreByTitleDate(String title, String date) {
        if (title == null || TextUtils.isEmpty(title)) return null;

        ArrayList<ScoreBean> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from goal_score where title=? and date=?", new String[]{title, date});
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

    public ArrayList<ScoreBean> getScoreToday(String title, String parent) {
        ArrayList<ScoreBean> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from goal_score where date=? and title=? and parent=?"
                , new String[]{CalendaUtils.getCurrntDate(), title, parent});
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


    public ArrayList<ScoreBean> getAllScore() {
        ArrayList<ScoreBean> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from goal_score", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            ScoreBean mBean = new ScoreBean();
            mBean.setLevel(res.getInt(res.getColumnIndex(GOAL_SCORE_LEVEL)));
            mBean.setParent(res.getString(res.getColumnIndex(GOAL_SCORE_PARENT)));
            mBean.setTitle(res.getString(res.getColumnIndex(GOAL_SCORE_TITLE)));
            mBean.setDate(res.getString(res.getColumnIndex(GOAL_SCORE_DATE)));
            mBean.setScore(res.getLong(res.getColumnIndex(GOAL_SCORE_SCORE)));
            mBean.setTimestamp(res.getInt(res.getColumnIndex(GOAL_SCORE_TIMESTAMP)));
            array_list.add(mBean);
            res.moveToNext();
        }
        return array_list;
    }

    /* Alarm Database */
    public boolean insertAlarm(int hour, int minute, int requestCode, String title,String parent, String select_dates, String request_codes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GOAL_ALARM_HOUR, hour);
        contentValues.put(GOAL_ALARM_MINUTE, minute);
        contentValues.put(GOAL_ALARM_REQUEST_CODE, requestCode);
        contentValues.put(GOAL_ALARM_SELECT_DATES, select_dates);
        contentValues.put(GOAL_ALARM_TITLE, title);
        contentValues.put(GOAL_ALARM_PARENT, parent);
        contentValues.put(GOAL_ALARM_REQUEST_CODES, request_codes);
        db.insert(GOAL_ALARM_TABLE, null, contentValues);
        return true;
    }

    public ArrayList<AlarmBean> getAlarmByTitle(String title,String parent) {
        if (title == null || TextUtils.isEmpty(title)) return null;

        ArrayList<AlarmBean> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from goal_alarm where title=? and parent=?", new String[]{title,parent});
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            AlarmBean mBean = new AlarmBean();
            mBean.setHour(res.getInt(res.getColumnIndex(GOAL_ALARM_HOUR)));
            mBean.setMinute(res.getInt(res.getColumnIndex(GOAL_ALARM_MINUTE)));
            mBean.setRequestCode(res.getInt(res.getColumnIndex(GOAL_ALARM_REQUEST_CODE)));
            String codes = res.getString(res.getColumnIndex(GOAL_ALARM_REQUEST_CODES));
            List<Integer> codeList = new ArrayList<>();
            if (!TextUtils.isEmpty(codes)) {
                String[] mSplit = codes.split("\n");
                for (String code : mSplit) {
                    codeList.add(Integer.parseInt(code));
                }
            }
            mBean.setRequestCodes(codeList);

            String dates = res.getString(res.getColumnIndex(GOAL_ALARM_SELECT_DATES));
            List<CalendarDay> dateList = new ArrayList<>();
            if (!TextUtils.isEmpty(dates)) {
                String[] mSplit = dates.split("\n");
                if (mSplit.length > 0) {
                    for (String dateString : mSplit) {
                        String[] mStrings = dateString.split("/");
                        if (mStrings.length == 3) {
                            int year = Integer.parseInt(mStrings[0]);
                            int month = Integer.parseInt(mStrings[1]);
                            int day = Integer.parseInt(mStrings[2]);
                            CalendarDay mCalendarDay = CalendarDay.from(year, month, day);
                            dateList.add(mCalendarDay);
                        }
                    }
                }
            }
            mBean.setSelectedDates(dateList);
            array_list.add(mBean);
            res.moveToNext();
        }
        return array_list;
    }

    public int deleteAlarm(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(GOAL_ALARM_TABLE,
                "title = ? ",
                new String[]{title});
    }
}
