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
public class DataSourceWorkout
{
    private static final String LOGTAG = "miworkoutpartner";
    private static DataSourceWorkout instance = null;

    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    private DataSourceWorkout(Context context)
    {
        dbhelper = DatabaseHelper.getInstance(context.getApplicationContext());
    }

    public synchronized static DataSourceWorkout instance(Context context)
    {
        if( instance == null )
        {
            instance = new DataSourceWorkout(context);
        }
        return instance;
    }

    public static final String[] allColumnsWorkout =
            {
                    DatabaseHelper.COLUMN_ID,
                    DatabaseHelper.COLUMN_NAME
            };

    public void open() {
        Log.i(LOGTAG, "Database Opened");
        database = dbhelper.getWritableDatabase();
    }

    public void close() {
        Log.i(LOGTAG, "Database Closed");
        dbhelper.close();
    }

    //*********Workout Table Manipulation***********

    //Throws Exception if the Workout is already in the database
    //Function inserts an Workout into the Workout table
    //Workout passed in has all information set besides it's id
    //Returns a Workout with everything set including it's id
    public Workout createWorkoutEntry(Workout workout) throws Exception {

        if(workoutExists(workout))
        {
            Exception e = new Exception("Invalid Input");
            throw e;
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, workout.get_workoutname());
        long insertid = database.insert(DatabaseHelper.TABLE_WORKOUTS, null, values);
        workout.set_id(insertid);

        return workout;
    }

    //Throws Exception if the workout ID is not in the database
    //Function removes a row from the WORKOUTS table based on the id column
    //Returns true if row was removed else returns false
    public boolean removeWorkout(long workout_id) throws Exception
    {
        if(validWorkoutID(workout_id) == false)
        {
            Exception e = new Exception("Invalid Input");
            throw e;
        }

        String where = DatabaseHelper.COLUMN_ID + "=" + workout_id;
        int result = database.delete(DatabaseHelper.TABLE_WORKOUTS, where, null);
        return (result == 1);
    }

    //Throws Exception if the Workout is already in the database
    //Function updates a row's, with a specific id, information
    //Returns true if row was updated else returns false
    public boolean updateWorkout(Workout workout) throws Exception {

        if(workoutExists(workout))
        {
            Exception e = new Exception("Invalid Input");
            throw e;
        }

        ContentValues values = new ContentValues();
        String where = DatabaseHelper.COLUMN_ID + "=" + workout.get_id();
        values.put(DatabaseHelper.COLUMN_NAME, workout.get_workoutname());
        int result = database.update(DatabaseHelper.TABLE_WORKOUTS, values, where, null);
        return (result == 1);
    }

    //Function finds all rows in the WORKOUTS table and returns them in an ArrayList of Workout
    public List<Workout> findAllWorkouts() {
        List<Workout> workouts = new ArrayList<Workout>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_WORKOUTS, allColumnsWorkout, null, null, null, null, null);

        Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Workout workout = new Workout();
                workout.set_id(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
                workout.set_workoutname(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)));

                workouts.add(workout);
            }
        }
        cursor.close();
        return workouts;
    }

    public long countAllWorkouts()
    {
        long result = queryNumEntries(database, DatabaseHelper.TABLE_WORKOUTS);
        Log.i(LOGTAG, "There are " + result + " rows in workout database");
        return result;
    }

    //Checks if a Workout with the same name already exists in the WORKOUT table
    public boolean workoutExists(Workout workout)
    {
        String where = DatabaseHelper.COLUMN_NAME + "=" + "\"" + workout.get_workoutname() + "\"";
        Cursor cursor = database.query(DatabaseHelper.TABLE_WORKOUTS, allColumnsWorkout, where, null, null, null, null);
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

    //Makes sure that the id from a Workout is actually in the WORKOUTS table
    public boolean validWorkoutID(long workout_id)
    {
        String where = DatabaseHelper.COLUMN_ID + "=" + workout_id +";";
        Cursor cursor = database.query(DatabaseHelper.TABLE_WORKOUTS, allColumnsWorkout, where, null, null, null, null);
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