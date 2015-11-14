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
 * Created by Tanner on 9/16/2015.
 */
public class DataSource
{
    private static final String LOGTAG = "miworkoutpartner";

    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    public static final String[] allColumnsWorkout =
            {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_NAME
            };

    public static final String[] allColumnsMaxes =
            {
                    DatabaseHelper.COLUMN_MAX_ID,
                    DatabaseHelper.COLUMN_MAX_NAME,
                    DatabaseHelper.COLUMN_MAX_WEIGHT,
                    DatabaseHelper.COLUMN_MAX_DATE
            };

    public DataSource(Context context)
    {
        dbhelper = DatabaseHelper.getInstance(context);
    }

    public void open()
    {
        Log.i(LOGTAG, "Database Opened");
        database = dbhelper.getWritableDatabase();
    }

    public void close()
    {
        Log.i(LOGTAG, "Database Closed");
        dbhelper.close();
    }

    //*********Workout Table Manipulation***********
    public Workout createWorkoutEntry(Workout workout)
    {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, workout.get_workoutname());
        long insertid = database.insert(DatabaseHelper.TABLE_WORKOUTS, null, values);
        workout.set_id(insertid);

        return workout;
    }

    public boolean removeWorkout(long workout_id)
    {
        String where = DatabaseHelper.COLUMN_ID + "=" + workout_id;
        int result = database.delete(DatabaseHelper.TABLE_WORKOUTS, where, null);
        return (result == 1);
    }

    public boolean updateWorkout(Workout workout)
    {
        ContentValues values = new ContentValues();
        String where = DatabaseHelper.COLUMN_ID + "=" + workout.get_id();
        values.put(DatabaseHelper.COLUMN_NAME, workout.get_workoutname());
        int result = database.update(DatabaseHelper.TABLE_WORKOUTS, values, where, null);
        return (result == 1);
    }

    public List<Workout> findAllWorkouts()
    {
        List<Workout> workouts = new ArrayList<Workout>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_WORKOUTS, allColumnsWorkout, null, null, null, null, null);

        Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
        if(cursor.getCount() > 0)
        {
            while(cursor.moveToNext())
            {
                Workout workout = new Workout();
                workout.set_id(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
                workout.set_workoutname(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)));

                workouts.add(workout);
            }
        }
        cursor.close();
        return workouts;
    }


    //*********Exercise Table Manipulation***********
    public Exercise createExerciseEntry(Exercise exercise)
    {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_EXERCISE_NAME, exercise.get_exercisename());
        values.put(DatabaseHelper.COLUMN_EXERCISE_WORKOUTID, exercise.get_workoutid());
        long insertid = database.insert(DatabaseHelper.TABLE_EXERCISES, null, values);
        exercise.set_id(insertid);

        return exercise;
    }

    public boolean removeExercise(long exercise_id)
    {
        String where = DatabaseHelper.COLUMN_EXERCISE_ID + "=" + exercise_id;
        int result = database.delete(DatabaseHelper.TABLE_EXERCISES, where, null);
        return (result == 1);
    }

    public boolean removeAssociatedExercises(long workout_id)
    {
        String where = DatabaseHelper.COLUMN_EXERCISE_WORKOUTID + "=" + workout_id;
        int result = database.delete(DatabaseHelper.TABLE_EXERCISES, where, null);
        return (result == 1);
    }

    public boolean updateExercise(Exercise exercise)
    {
        ContentValues values = new ContentValues();
        String where = DatabaseHelper.COLUMN_EXERCISE_ID + "=" + exercise.get_id();
        values.put(DatabaseHelper.COLUMN_EXERCISE_NAME, exercise.get_exercisename());
        int result = database.update(DatabaseHelper.TABLE_EXERCISES, values, where, null);
        return (result == 1);
    }

    public long countExerciseRows()
    {
        long result = queryNumEntries(database, DatabaseHelper.TABLE_EXERCISES);
        Log.i(LOGTAG, "There are " + result + " rows in exercise database");
        return result;
    }

    public List<Exercise> findCertainExercises(long workoutID)
    {
        List<Exercise> exercises = new ArrayList<Exercise>();
        String rawQuery = "SELECT * FROM " + DatabaseHelper.TABLE_WORKOUTS + " INNER JOIN " +
                DatabaseHelper.TABLE_EXERCISES + " ON " + DatabaseHelper.TABLE_WORKOUTS +
                "." + DatabaseHelper.COLUMN_ID + " = " + DatabaseHelper.TABLE_EXERCISES +
                "." + DatabaseHelper.COLUMN_EXERCISE_WORKOUTID + " WHERE " +
                DatabaseHelper.COLUMN_EXERCISE_WORKOUTID + " = " + workoutID + ";";

        Cursor cursor = database.rawQuery(rawQuery, null);
        Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
        if(cursor.getCount() > 0)
        {
            while(cursor.moveToNext())
            {
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

    public ArrayList<String> findCertainExercisesToString(long workout_id)
    {
        ArrayList<String> exerciseNames = new ArrayList<String>();
        String rawQuery = "SELECT * FROM " + DatabaseHelper.TABLE_WORKOUTS + " INNER JOIN " +
                DatabaseHelper.TABLE_EXERCISES + " ON " + DatabaseHelper.TABLE_WORKOUTS +
                "." + DatabaseHelper.COLUMN_ID + " = " + DatabaseHelper.TABLE_EXERCISES +
                "." + DatabaseHelper.COLUMN_EXERCISE_WORKOUTID + " WHERE " +
                DatabaseHelper.COLUMN_EXERCISE_WORKOUTID + " = " + workout_id + ";";

        Cursor cursor = database.rawQuery(rawQuery, null);
        Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
        if(cursor.getCount() > 0)
        {
            while(cursor.moveToNext())
            {
                exerciseNames.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXERCISE_NAME)));
            }
        }
        cursor.close();
        return exerciseNames;
    }

    //*********Set Table Manipulation***********
    public Set createSetEntry(Set set)
    {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SET_REPS, set.get_reps());
        values.put(DatabaseHelper.COLUMN_SET_WEIGHT, set.get_weight());
        values.put(DatabaseHelper.COLUMN_SET_EXERCISEID, set.get_exerciseid());
        long insertid = database.insert(DatabaseHelper.TABLE_SETS, null, values);
        set.set_id(insertid);

        return set;
    }

    public boolean removeSet(long set_id)
    {
        String where = DatabaseHelper.COLUMN_SET_ID + "=" + set_id;
        int result = database.delete(DatabaseHelper.TABLE_SETS, where, null);
        return (result == 1);
    }

    public boolean removeAssociatedSetExercise(long exercise_id)
    {
        String where = DatabaseHelper.COLUMN_SET_EXERCISEID + "=" + exercise_id;
        int result = database.delete(DatabaseHelper.TABLE_SETS, where, null);
        return (result == 1);
    }

    public void removeAssociatedSetWorkout(long workout_id)
    {
        String rawQuery = "SELECT * FROM " + DatabaseHelper.TABLE_WORKOUTS + " INNER JOIN " +
                DatabaseHelper.TABLE_EXERCISES + " ON " + DatabaseHelper.TABLE_WORKOUTS +
                "." + DatabaseHelper.COLUMN_ID + " = " + DatabaseHelper.TABLE_EXERCISES +
                "." + DatabaseHelper.COLUMN_EXERCISE_WORKOUTID + " WHERE " +
                DatabaseHelper.COLUMN_EXERCISE_WORKOUTID + " = " + workout_id + ";";
        Cursor cursor = database.rawQuery(rawQuery, null);
        if(cursor.getCount() > 0)
        {
            while(cursor.moveToNext())
            {
                Log.i(LOGTAG, "exerciseid to be deleted is " + cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXERCISE_ID)));
                long exerciseID = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXERCISE_ID));
                String execQuery = "DELETE FROM " + DatabaseHelper.TABLE_SETS + " WHERE " +
                                    DatabaseHelper.COLUMN_SET_EXERCISEID + "=" + exerciseID;
                database.execSQL(execQuery);
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

    public boolean updateSet(Set set)
    {
        ContentValues values = new ContentValues();
        String where = DatabaseHelper.COLUMN_SET_ID + "=" + set.get_id();
        values.put(DatabaseHelper.COLUMN_SET_REPS, set.get_reps());
        values.put(DatabaseHelper.COLUMN_SET_WEIGHT, set.get_weight());
        int result = database.update(DatabaseHelper.TABLE_SETS, values, where, null);
        return (result == 1);
    }

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

                sets.add(set);
            }
        }
        cursor.close();
        return sets;
    }

    public ArrayList<Object> findCertainSetsToString(long workout_id)
    {
        ArrayList<Object> setStrings = new ArrayList<Object>();

        String rawQuery = "SELECT * FROM " + DatabaseHelper.TABLE_WORKOUTS + " INNER JOIN " +
                DatabaseHelper.TABLE_EXERCISES + " ON " + DatabaseHelper.TABLE_WORKOUTS +
                "." + DatabaseHelper.COLUMN_ID + " = " + DatabaseHelper.TABLE_EXERCISES +
                "." + DatabaseHelper.COLUMN_EXERCISE_WORKOUTID + " WHERE " +
                DatabaseHelper.COLUMN_EXERCISE_WORKOUTID + " = " + workout_id + ";";

        Cursor cursor = database.rawQuery(rawQuery, null);
        if(cursor.getCount() > 0)
        {
            while(cursor.moveToNext())
            {
                long exerciseID = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXERCISE_ID));
                ArrayList<String> child = new ArrayList<String>();
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
                        child.add("• " + set.toString());
                    }
                }
                else
                {
                        child.add("• " + "No Sets Available");
                }
                secondCursor.close();
                setStrings.add(child);
            }
        }
        cursor.close();
        return setStrings;
    }


    //*********Workout Table Manipulation***********
    public MaxLift createMaxEntry(MaxLift max)
    {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_MAX_NAME, max.get_exerciseName());
        values.put(DatabaseHelper.COLUMN_MAX_WEIGHT, max.get_weight());
        values.put(DatabaseHelper.COLUMN_MAX_DATE, max.get_date());
        long insertid = database.insert(DatabaseHelper.TABLE_MAXES, null, values);
        max.set_id(insertid);

        return max;
    }

    public boolean removeMax(long max_id)
    {
        String where = DatabaseHelper.COLUMN_MAX_ID + "=" + max_id;
        int result = database.delete(DatabaseHelper.TABLE_MAXES, where, null);
        return (result == 1);
    }

    public boolean updateMax(MaxLift max)
    {
        ContentValues values = new ContentValues();
        String where = DatabaseHelper.COLUMN_ID + "=" + max.get_id();
        values.put(DatabaseHelper.COLUMN_MAX_NAME, max.get_exerciseName());
        values.put(DatabaseHelper.COLUMN_MAX_WEIGHT, max.get_weight());
        values.put(DatabaseHelper.COLUMN_MAX_DATE, max.get_date());
        int result = database.update(DatabaseHelper.TABLE_MAXES, values, where, null);
        return (result == 1);
    }

    public List<MaxLift> findAllMaxes()
    {
        List<MaxLift> maxes = new ArrayList<MaxLift>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_MAXES, allColumnsMaxes, null, null, null, null, null);

        Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
        if(cursor.getCount() > 0)
        {
            while(cursor.moveToNext())
            {
                MaxLift max = new MaxLift();
                max.set_id(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_MAX_ID)));
                max.set_exerciseName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_MAX_NAME)));
                max.set_weight(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_MAX_WEIGHT)));
                max.set_date(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_MAX_DATE)));

                maxes.add(max);
            }
        }
        cursor.close();
        return maxes;
    }

}
