package twfq5.mail.umkc.edu.miworkoutpartner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import static android.database.DatabaseUtils.queryNumEntries;

/**
 * Created by Tanner on 10/7/2015.
 */
public class DataSourceExercise {
    private static final String LOGTAG = "miworkoutpartner";
    private static DataSourceExercise instance = null;

    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    private DataSourceExercise(Context context)
    {
        dbhelper = DatabaseHelper.getInstance(context.getApplicationContext());
    }

    public synchronized static DataSourceExercise instance(Context context)
    {
        if( instance == null )
        {
            instance = new DataSourceExercise(context);
        }
        return instance;
    }

    public static final String[] allColumnsExercise =
            {
                    DatabaseHelper.COLUMN_EXERCISE_ID,
                    DatabaseHelper.COLUMN_EXERCISE_NAME,
                    DatabaseHelper.COLUMN_EXERCISE_WORKOUTID,
            };

    public void open() {
        Log.i(LOGTAG, "Exercise Database Opened");
        database = dbhelper.getWritableDatabase();
    }

    public void close() {
        Log.i(LOGTAG, "Exercise Database Closed");
        dbhelper.close();
    }

    //*********Exercise Table Manipulation***********

    //Throws Exception if Exercise with same Name and workoutID already exist in the database
    //Function inserts an exercise into the EXERCISES table
    //Exercise passed in has all of it's information set besides it's id
    //Returns an exercise with everything set including it's id
    public Exercise createExerciseEntry(Exercise exercise) throws Exception
    {
        if(exerciseExists(exercise))
        {
            Exception e = new Exception("Invalid Input");
            throw e;
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_EXERCISE_NAME, exercise.get_exercisename());
        values.put(DatabaseHelper.COLUMN_EXERCISE_WORKOUTID, exercise.get_workoutid());
        long insertid = database.insert(DatabaseHelper.TABLE_EXERCISES, null, values);
        exercise.set_id(insertid);

        return exercise;
    }

    //Throws Exception if the Exercise ID is not in the database
    //Function removes a row from the EXERCISES table based on the id column
    //Returns true if row was removed else returns false
    public boolean removeExercise(long exercise_id) throws Exception
    {
        if(validExerciseID(exercise_id) == false)
        {
            Exception e = new Exception("Invalid Input");
            throw e;
        }

        String where = DatabaseHelper.COLUMN_EXERCISE_ID + "=" + exercise_id;
        int result = database.delete(DatabaseHelper.TABLE_EXERCISES, where, null);
        return (result == 1);
    }

    //Function removes rows (exercise) from the EXERCISES table based on it's workoutID column
    //Returns true if row was removed else returns false
    public boolean removeAssociatedExercises(long workout_id) {
        String where = DatabaseHelper.COLUMN_EXERCISE_WORKOUTID + "=" + workout_id;
        int result = database.delete(DatabaseHelper.TABLE_EXERCISES, where, null);
        return (result == 1);
    }

    //Throws Exception if Exercise with same Name and workoutID already exist in the EXERCISES table
    //Function updates a row with a specific exercise id information
    //Returns true if row was updated else returns false
    public boolean updateExercise(Exercise exercise) throws Exception
    {
        if(exerciseExists(exercise))
        {
            Exception e = new Exception("Invalid Input");
            throw e;
        }

        ContentValues values = new ContentValues();
        String where = DatabaseHelper.COLUMN_EXERCISE_ID + "=" + exercise.get_id();
        values.put(DatabaseHelper.COLUMN_EXERCISE_NAME, exercise.get_exercisename());
        int result = database.update(DatabaseHelper.TABLE_EXERCISES, values, where, null);
        return (result == 1);
    }

    public long countExerciseRows() {
        long result = queryNumEntries(database, DatabaseHelper.TABLE_EXERCISES);
        Log.i(LOGTAG, "There are " + result + " rows in exercise database");
        return result;
    }

    //Function finds all rows in the EXERCISES table that have a matching workoutid
    // and returns them in an ArrayList of Exercises
    public List<Exercise> findCertainExercises(long workoutID) {
        List<Exercise> exercises = new ArrayList<Exercise>();
        String rawQuery = "SELECT * FROM " + DatabaseHelper.TABLE_WORKOUTS + " INNER JOIN " +
                DatabaseHelper.TABLE_EXERCISES + " ON " + DatabaseHelper.TABLE_WORKOUTS +
                "." + DatabaseHelper.COLUMN_ID + " = " + DatabaseHelper.TABLE_EXERCISES +
                "." + DatabaseHelper.COLUMN_EXERCISE_WORKOUTID + " WHERE " +
                DatabaseHelper.COLUMN_EXERCISE_WORKOUTID + " = " + workoutID + ";";

        Cursor cursor = database.rawQuery(rawQuery, null);
        Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Exercise exercise = new Exercise();
                exercise.set_id(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXERCISE_ID)));
                exercise.set_exercisename(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXERCISE_NAME)));
                exercise.set_workoutid(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXERCISE_WORKOUTID)));

                exercises.add(exercise);
            }
        }
        cursor.close();
        return exercises;
    }

    //Function finds all rows in the EXERCISES table that have a matching workoutid
    // and returns them in an ArrayList of Strings
    public ArrayList<String> findCertainExercisesToString(long workout_id) {
        ArrayList<String> exerciseNames = new ArrayList<String>();
        String rawQuery = "SELECT * FROM " + DatabaseHelper.TABLE_WORKOUTS + " INNER JOIN " +
                DatabaseHelper.TABLE_EXERCISES + " ON " + DatabaseHelper.TABLE_WORKOUTS +
                "." + DatabaseHelper.COLUMN_ID + " = " + DatabaseHelper.TABLE_EXERCISES +
                "." + DatabaseHelper.COLUMN_EXERCISE_WORKOUTID + " WHERE " +
                DatabaseHelper.COLUMN_EXERCISE_WORKOUTID + " = " + workout_id + ";";

        Cursor cursor = database.rawQuery(rawQuery, null);
        Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                exerciseNames.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXERCISE_NAME)));
            }
        }
        cursor.close();
        return exerciseNames;
    }

    //Checks if an exercise with the same name already exists in the EXERCISES table
    public boolean exerciseExists(Exercise exercise)
    {
        String where = DatabaseHelper.COLUMN_EXERCISE_NAME + "=" + "\"" + exercise.get_exercisename() + "\""
                       + " AND " + DatabaseHelper.COLUMN_EXERCISE_WORKOUTID + "=" + exercise.get_workoutid()
                       + ";";
        Cursor cursor = database.query(DatabaseHelper.TABLE_EXERCISES, allColumnsExercise, where, null, null, null, null);
        if(cursor.getCount() > 0)
        {
            cursor.close();
            return true;
        }
        else
        {
            cursor.close();
            return false;
        }
    }

    //Makes sure that the id from an exercise is actually in the EXERCISES table
    public boolean validExerciseID(long exercise_id)
    {
        String where = DatabaseHelper.COLUMN_EXERCISE_ID + "=" + exercise_id +";";
        Cursor cursor = database.query(DatabaseHelper.TABLE_EXERCISES, allColumnsExercise, where, null, null, null, null);
        if(cursor.getCount() > 0)
        {
            cursor.close();
            return true;
        }
        else
        {
            cursor.close();
            return false;
        }
    }
}