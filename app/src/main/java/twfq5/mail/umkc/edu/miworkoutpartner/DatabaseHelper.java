package twfq5.mail.umkc.edu.miworkoutpartner;

/**
 * Created by Tanner on 9/16/2015.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper
{

    private static final String LOGTAG = "miworkoutpartner";
    private static DatabaseHelper sInstance = null;

    public static final String DATABASE_NAME = "miworkoutpartner.db";
    public static final int DATABASE_VERSION = 17;

    //*********Workout Table Information***********
    public static final String TABLE_WORKOUTS = "workouts";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ID = "id";

    private static final String TABLE_WORKOUTS_CREATE =
                    "CREATE TABLE " + TABLE_WORKOUTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT" + ");";

    //*********Exercise Table Information***********
    public static final String TABLE_EXERCISES = "exercises";
    public static final String COLUMN_EXERCISE_NAME = "exercise_name";
    public static final String COLUMN_EXERCISE_ID = "id";
    public static final String COLUMN_EXERCISE_WORKOUTID = "workout_id";

    private static final String TABLE_EXERCISES_CREATE =
                    "CREATE TABLE " + TABLE_EXERCISES + " (" +
                    COLUMN_EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EXERCISE_NAME + " TEXT, " + COLUMN_EXERCISE_WORKOUTID + " INTEGER" + ");";

    //*********Set Table Information***********
    public static final String TABLE_SETS = "sets";
    public static final String COLUMN_SET_ID = "id";
    public static final String COLUMN_SET_REPS = "set_reps";
    public static final String COLUMN_SET_WEIGHT = "set_weight";
    public static final String COLUMN_SET_EXERCISEID = "exercise_id";
    public static final String COLUMN_SET_COMPLETED = "set_completed";

    private static final String TABLE_SETS_CREATE =
            "CREATE TABLE " + TABLE_SETS + " (" +
                    COLUMN_SET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_SET_REPS + " INTEGER, " + COLUMN_SET_EXERCISEID + " INTEGER, " +
                    COLUMN_SET_WEIGHT + " INTEGER, " + COLUMN_SET_COMPLETED + " INTEGER" + ");";

    //*********MaxLift Table Information***********
    public static final String TABLE_MAXES = "maxes";
    public static final String COLUMN_MAX_ID = "id";
    public static final String COLUMN_MAX_NAME = "max_name";
    public static final String COLUMN_MAX_WEIGHT = "max_weight";
    public static final String COLUMN_MAX_DATE = "max_date";

    private static final String TABLE_MAXES_CREATE =
            "CREATE TABLE " + TABLE_MAXES + " (" +
                    COLUMN_MAX_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_MAX_WEIGHT + " INTEGER, " + COLUMN_MAX_DATE + " TEXT, " +
                    COLUMN_MAX_NAME + " TEXT" + ");";

    //*********Exercise Video Table Information***********
    public static final String TABLE_EXERCISE_VIDEOS = "exercisevideos";
    public static final String COLUMN_EXERCISE_VIDEOS_ID = "id";
    public static final String COLUMN_EXERCISE_VIDEOS_NAME = "video_name";
    public static final String COLUMN_EXERCISE_VIDEOS_URL = "video_url";

    private static final String TABLE_EXERCISE_VIDEOS_CREATE =
            "CREATE TABLE " + TABLE_EXERCISE_VIDEOS + " (" +
                    COLUMN_EXERCISE_VIDEOS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EXERCISE_VIDEOS_NAME + " TEXT, " + COLUMN_EXERCISE_VIDEOS_URL +
                    " TEXT" + ");";

    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    private DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(TABLE_WORKOUTS_CREATE);
        Log.i(LOGTAG, "Workouts table has been created");
        db.execSQL(TABLE_EXERCISES_CREATE);
        Log.i(LOGTAG, "Exercises table has been created");
        db.execSQL(TABLE_SETS_CREATE);
        Log.i(LOGTAG, "Sets table has been created");
        db.execSQL(TABLE_MAXES_CREATE);
        Log.i(LOGTAG, "Maxes table has been created");
        db.execSQL(TABLE_EXERCISE_VIDEOS_CREATE);
        Log.i(LOGTAG, "Exercise videos table has been created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETS);
        db.execSQL("DROP TABLE IF EXISTS "  + TABLE_MAXES);
        db.execSQL("DROP TABLE IF EXISTS "  + TABLE_EXERCISE_VIDEOS);
        onCreate(db);
    }
}
