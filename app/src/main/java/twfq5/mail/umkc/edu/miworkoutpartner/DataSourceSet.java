package twfq5.mail.umkc.edu.miworkoutpartner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static android.database.DatabaseUtils.queryNumEntries;

/**
 * Created by Tanner on 10/7/2015.
 */
public class DataSourceSet
{
    private static final String LOGTAG = "miworkoutpartner";
    private static DataSourceSet instance = null;

    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    private DataSourceSet(Context context)
    {
        dbhelper = DatabaseHelper.getInstance(context.getApplicationContext());
    }

    public synchronized static DataSourceSet instance(Context context)
    {
        if( instance == null )
        {
            instance = new DataSourceSet(context);
        }
        return instance;
    }

    public static final String[] allColumnsSet =
            {
                    DatabaseHelper.COLUMN_SET_ID,
                    DatabaseHelper.COLUMN_SET_EXERCISEID,
                    DatabaseHelper.COLUMN_SET_REPS,
                    DatabaseHelper.COLUMN_SET_WEIGHT,
                    DatabaseHelper.COLUMN_SET_COMPLETED
            };

    public void open()
    {
        Log.i(LOGTAG, "Set Database Opened");
        database = dbhelper.getWritableDatabase();
    }

    public void close()
    {
        Log.i(LOGTAG, "Set Database Closed");
        dbhelper.close();
    }

    //*********Set Table Manipulation***********

    //Function inserts a set into the SET table
    //Set passed in has all of it's information set besides it's id
    //Returns a set with everything set including it's id
    public Set createSetEntry(Set set)
    {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SET_REPS, set.get_reps());
        values.put(DatabaseHelper.COLUMN_SET_WEIGHT, set.get_weight());
        values.put(DatabaseHelper.COLUMN_SET_EXERCISEID, set.get_exerciseid());
        values.put(DatabaseHelper.COLUMN_SET_COMPLETED, set.get_completed());
        long insertid = database.insert(DatabaseHelper.TABLE_SETS, null, values);
        set.set_id(insertid);

        return set;
    }

    //Throws Exception if the Set ID is not in the SET table
    //Function removes a row from the SET table based on the id column
    //Returns true if row was removed else returns false
    public boolean removeSet(long set_id) throws Exception
    {
        if(validSetID(set_id) == false)
        {
            Exception e = new Exception("Invalid Input");
            throw e;
        }

        String where = DatabaseHelper.COLUMN_SET_ID + "=" + set_id;
        int result = database.delete(DatabaseHelper.TABLE_SETS, where, null);
        return (result == 1);
    }

    //Function removes rows (set) from the SET table based on it's exerciseID column
    //Returns true if row was removed else returns false
    public boolean removeAssociatedSetExercise(long exercise_id)
    {
        String where = DatabaseHelper.COLUMN_SET_EXERCISEID + "=" + exercise_id;
        int result = database.delete(DatabaseHelper.TABLE_SETS, where, null);
        return (result == 1);
    }

    //Function removes rows (set) from the SET table based on it's exerciseID column
    //Returns true if row was removed else returns false
    public void removeAssociatedSetWorkout(long workout_id)
    {
        String rawQuery = "SELECT * FROM " + DatabaseHelper.TABLE_WORKOUTS + " INNER JOIN " +
                DatabaseHelper.TABLE_EXERCISES + " ON " + DatabaseHelper.TABLE_WORKOUTS +
                "." + DatabaseHelper.COLUMN_ID + " = " + DatabaseHelper.TABLE_EXERCISES +
                "." + DatabaseHelper.COLUMN_EXERCISE_WORKOUTID + " WHERE " +
                DatabaseHelper.COLUMN_EXERCISE_WORKOUTID + " = " + workout_id + ";";
        Cursor cursor = database.rawQuery(rawQuery, null); //Find all exercises that have the same workout_id
        if(cursor.getCount() > 0)
        {
            while(cursor.moveToNext())
            {
                Log.i(LOGTAG, "exerciseid to be deleted is " + cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXERCISE_ID)));
                long exerciseID = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXERCISE_ID));
                String execQuery = "DELETE FROM " + DatabaseHelper.TABLE_SETS + " WHERE " +
                        DatabaseHelper.COLUMN_SET_EXERCISEID + "=" + exerciseID;
                database.execSQL(execQuery); //Delete all set's that have the same exercise_id as the found exercise
            }
        }
        cursor.close();
    }

    public long countSetRows()
    {
        long result = queryNumEntries(database, DatabaseHelper.TABLE_SETS);
        Log.i(LOGTAG, "There are " + result + " rows in set database");
        return result;
    }

    //Function updates a row with a specific set id information
    //Returns true if row was updated else returns false
    public boolean updateSet(Set set)
    {
        ContentValues values = new ContentValues();
        String where = DatabaseHelper.COLUMN_SET_ID + "=" + set.get_id();
        values.put(DatabaseHelper.COLUMN_SET_REPS, set.get_reps());
        values.put(DatabaseHelper.COLUMN_SET_WEIGHT, set.get_weight());
        values.put(DatabaseHelper.COLUMN_SET_COMPLETED, set.get_completed());
        int result = database.update(DatabaseHelper.TABLE_SETS, values, where, null);
        return (result == 1);
    }

    //Function finds all rows in the SET table that have a matching exerciseid
    // and returns them in an ArrayList of Exercises
    public List<Set> findCertainSets(long exerciseID)
    {
        List<Set> sets = new ArrayList<Set>();
        String rawQuery = "SELECT * FROM " + DatabaseHelper.TABLE_EXERCISES + " INNER JOIN " +
                DatabaseHelper.TABLE_SETS + " ON " + DatabaseHelper.TABLE_EXERCISES +
                "." + DatabaseHelper.COLUMN_EXERCISE_ID + " = " + DatabaseHelper.TABLE_SETS +
                "." + DatabaseHelper.COLUMN_SET_EXERCISEID + " WHERE " +
                DatabaseHelper.COLUMN_SET_EXERCISEID + " = " + exerciseID + ";";

        Cursor cursor = database.rawQuery(rawQuery, null);
        Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
        if(cursor.getCount() > 0)
        {
            while(cursor.moveToNext())
            {
                Set set = new Set();
                set.set_id(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_SET_ID)));
                set.set_reps(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_SET_REPS)));
                set.set_weight(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_SET_WEIGHT)));
                set.set_exerciseid(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_SET_EXERCISEID)));
                set.set_completed(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_SET_COMPLETED)));

                sets.add(set);
            }
        }
        cursor.close();
        return sets;
    }

    //Function finds all rows in the SET table that have a matching exerciseid
    // and returns them in an ArrayList of an ArrayList of setCarriers
    public ArrayList<ArrayList<SetCarrier>> findCertainSetsToSetCarrier(long workout_id)
    {
        ArrayList<ArrayList<SetCarrier>> setStrings = new ArrayList<ArrayList<SetCarrier>>();

        //Get all the Exercises that correspond with the workout
        String rawQuery = "SELECT * FROM " + DatabaseHelper.TABLE_WORKOUTS + " INNER JOIN " +
                DatabaseHelper.TABLE_EXERCISES + " ON " + DatabaseHelper.TABLE_WORKOUTS +
                "." + DatabaseHelper.COLUMN_ID + " = " + DatabaseHelper.TABLE_EXERCISES +
                "." + DatabaseHelper.COLUMN_EXERCISE_WORKOUTID + " WHERE " +
                DatabaseHelper.COLUMN_EXERCISE_WORKOUTID + " = " + workout_id + ";";

        Cursor cursor = database.rawQuery(rawQuery, null);
        if(cursor.getCount() > 0)
        {
            //Get all the sets from the current exercise
            while(cursor.moveToNext())
            {
                long exerciseID = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXERCISE_ID));
                ArrayList<SetCarrier> child = new ArrayList<SetCarrier>();
                String secondRawQuery = "Select * FROM " + DatabaseHelper.TABLE_SETS + " WHERE " +
                        DatabaseHelper.COLUMN_SET_EXERCISEID + "=" + exerciseID;
                Cursor secondCursor = database.rawQuery(secondRawQuery, null);
                if(secondCursor.getCount() > 0)
                {
                    while(secondCursor.moveToNext())
                    {
                        Set set = new Set();
                        set.set_id(secondCursor.getLong(secondCursor.getColumnIndex(DatabaseHelper.COLUMN_SET_ID)));
                        set.set_reps(secondCursor.getLong(secondCursor.getColumnIndex(DatabaseHelper.COLUMN_SET_REPS)));
                        set.set_weight(secondCursor.getLong(secondCursor.getColumnIndex(DatabaseHelper.COLUMN_SET_WEIGHT)));
                        set.set_exerciseid(secondCursor.getLong(secondCursor.getColumnIndex(DatabaseHelper.COLUMN_SET_EXERCISEID)));
                        set.set_completed(secondCursor.getInt(secondCursor.getColumnIndex(DatabaseHelper.COLUMN_SET_COMPLETED)));

                        SetCarrier sc = new SetCarrier();
                        sc.add("• " + set.get_reps() + " reps x " + set.get_weight() + "lbs", set);
                        child.add(sc);
                    }
                }
                else
                {
                    SetCarrier sc = new SetCarrier();
                    sc.add("• " + "No Sets Available", null);
                    child.add(sc);
                }
                secondCursor.close();
                setStrings.add(child);
            }
        }
        cursor.close();
        return setStrings;
    }

    public boolean validSetID(long set_id)
    {
        String where = DatabaseHelper.COLUMN_SET_ID + "=" + set_id +";";
        Cursor cursor = database.query(DatabaseHelper.TABLE_SETS, allColumnsSet, where, null, null, null, null);
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

    //Function counts all corresponding rows from the SET table whose completed column equals 1
    // and returns them in a double
    public double countAllRelatedCompleted(long workout_id)
    {
        final int NOT_COMPLETED = 0;
        final int COMPLETED = 1;
        double count = 0;

        //Get all the Exercises that correspond with the workout
        String rawQuery = "SELECT * FROM " + DatabaseHelper.TABLE_WORKOUTS + " INNER JOIN " +
                DatabaseHelper.TABLE_EXERCISES + " ON " + DatabaseHelper.TABLE_WORKOUTS +
                "." + DatabaseHelper.COLUMN_ID + " = " + DatabaseHelper.TABLE_EXERCISES +
                "." + DatabaseHelper.COLUMN_EXERCISE_WORKOUTID + " WHERE " +
                DatabaseHelper.COLUMN_EXERCISE_WORKOUTID + " = " + workout_id + ";";

        Cursor cursor = database.rawQuery(rawQuery, null);
        if(cursor.getCount() > 0)
        {
            //Get all the sets from the current exercise
            while(cursor.moveToNext())
            {
                long exerciseID = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXERCISE_ID));
                String secondRawQuery = "Select * FROM " + DatabaseHelper.TABLE_SETS + " WHERE " +
                        DatabaseHelper.COLUMN_SET_EXERCISEID + "=" + exerciseID;
                Cursor secondCursor = database.rawQuery(secondRawQuery, null);
                if(secondCursor.getCount() > 0)
                {
                    while(secondCursor.moveToNext())
                    {
                        Set set = new Set();
                        set.set_id(secondCursor.getLong(secondCursor.getColumnIndex(DatabaseHelper.COLUMN_SET_ID)));
                        set.set_reps(secondCursor.getLong(secondCursor.getColumnIndex(DatabaseHelper.COLUMN_SET_REPS)));
                        set.set_weight(secondCursor.getLong(secondCursor.getColumnIndex(DatabaseHelper.COLUMN_SET_WEIGHT)));
                        set.set_exerciseid(secondCursor.getLong(secondCursor.getColumnIndex(DatabaseHelper.COLUMN_SET_EXERCISEID)));
                        set.set_completed(secondCursor.getInt(secondCursor.getColumnIndex(DatabaseHelper.COLUMN_SET_COMPLETED)));
                        if(set.get_completed() == COMPLETED)
                        {
                            count++;
                        }
                    }
                }
                secondCursor.close();
            }
        }
        cursor.close();
        return count;
    }

    //Function counts all corresponding rows from the SET table and returns the count as a double
    public double countAllRelated(long workout_id)
    {
        double count = 0;

        //Get all the Exercises that correspond with the workout
        String rawQuery = "SELECT * FROM " + DatabaseHelper.TABLE_WORKOUTS + " INNER JOIN " +
                DatabaseHelper.TABLE_EXERCISES + " ON " + DatabaseHelper.TABLE_WORKOUTS +
                "." + DatabaseHelper.COLUMN_ID + " = " + DatabaseHelper.TABLE_EXERCISES +
                "." + DatabaseHelper.COLUMN_EXERCISE_WORKOUTID + " WHERE " +
                DatabaseHelper.COLUMN_EXERCISE_WORKOUTID + " = " + workout_id + ";";

        Cursor cursor = database.rawQuery(rawQuery, null);
        if(cursor.getCount() > 0)
        {
            //Get all the sets from the current exercise
            while(cursor.moveToNext())
            {
                long exerciseID = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXERCISE_ID));
                String secondRawQuery = "Select * FROM " + DatabaseHelper.TABLE_SETS + " WHERE " +
                        DatabaseHelper.COLUMN_SET_EXERCISEID + "=" + exerciseID;
                Cursor secondCursor = database.rawQuery(secondRawQuery, null);
                if(secondCursor.getCount() > 0)
                {
                    while(secondCursor.moveToNext())
                    {
                        Set set = new Set();
                        set.set_id(secondCursor.getLong(secondCursor.getColumnIndex(DatabaseHelper.COLUMN_SET_ID)));
                        set.set_reps(secondCursor.getLong(secondCursor.getColumnIndex(DatabaseHelper.COLUMN_SET_REPS)));
                        set.set_weight(secondCursor.getLong(secondCursor.getColumnIndex(DatabaseHelper.COLUMN_SET_WEIGHT)));
                        set.set_exerciseid(secondCursor.getLong(secondCursor.getColumnIndex(DatabaseHelper.COLUMN_SET_EXERCISEID)));
                        set.set_completed(secondCursor.getInt(secondCursor.getColumnIndex(DatabaseHelper.COLUMN_SET_COMPLETED)));
                        count++;
                    }
                }
                secondCursor.close();
            }
        }
        cursor.close();
        return count;
    }

    //Function changes all corresponding rows in the SET table to not completed
    public void resetCompletedSetsRelated(long workout_id)
    {
        final int NOT_COMPLETED = 0;
        final int COMPLETED = 1;

        //Get all the Exercises that correspond with the workout
        String rawQuery = "SELECT * FROM " + DatabaseHelper.TABLE_WORKOUTS + " INNER JOIN " +
                DatabaseHelper.TABLE_EXERCISES + " ON " + DatabaseHelper.TABLE_WORKOUTS +
                "." + DatabaseHelper.COLUMN_ID + " = " + DatabaseHelper.TABLE_EXERCISES +
                "." + DatabaseHelper.COLUMN_EXERCISE_WORKOUTID + " WHERE " +
                DatabaseHelper.COLUMN_EXERCISE_WORKOUTID + " = " + workout_id + ";";

        Cursor cursor = database.rawQuery(rawQuery, null);
        if(cursor.getCount() > 0)
        {
            //Get all the sets from the current exercise
            while(cursor.moveToNext())
            {
                long exerciseID = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXERCISE_ID));
                String secondRawQuery = "Select * FROM " + DatabaseHelper.TABLE_SETS + " WHERE " +
                        DatabaseHelper.COLUMN_SET_EXERCISEID + "=" + exerciseID;
                Cursor secondCursor = database.rawQuery(secondRawQuery, null);
                if(secondCursor.getCount() > 0)
                {
                    while(secondCursor.moveToNext())
                    {
                        Set set = new Set();
                        set.set_id(secondCursor.getLong(secondCursor.getColumnIndex(DatabaseHelper.COLUMN_SET_ID)));
                        set.set_reps(secondCursor.getLong(secondCursor.getColumnIndex(DatabaseHelper.COLUMN_SET_REPS)));
                        set.set_weight(secondCursor.getLong(secondCursor.getColumnIndex(DatabaseHelper.COLUMN_SET_WEIGHT)));
                        set.set_exerciseid(secondCursor.getLong(secondCursor.getColumnIndex(DatabaseHelper.COLUMN_SET_EXERCISEID)));
                        set.set_completed(secondCursor.getInt(secondCursor.getColumnIndex(DatabaseHelper.COLUMN_SET_COMPLETED)));
                        set.set_completed(NOT_COMPLETED);
                        updateSet(set);
                    }
                }
                secondCursor.close();
            }
        }
        cursor.close();
    }
}
