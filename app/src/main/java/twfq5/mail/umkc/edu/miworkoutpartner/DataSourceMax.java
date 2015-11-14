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
public class DataSourceMax
{
    private static final String LOGTAG = "miworkoutpartner";
    private static DataSourceMax instance = null;

    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    private DataSourceMax(Context context)
    {
        dbhelper = DatabaseHelper.getInstance(context.getApplicationContext());
    }

    public synchronized static DataSourceMax instance(Context context)
    {
        if( instance == null )
        {
            instance = new DataSourceMax(context);
        }
        return instance;
    }

    public static final String[] allColumnsMaxes =
            {
                    DatabaseHelper.COLUMN_MAX_ID,
                    DatabaseHelper.COLUMN_MAX_NAME,
                    DatabaseHelper.COLUMN_MAX_WEIGHT,
                    DatabaseHelper.COLUMN_MAX_DATE
            };

    public void open()
    {
        Log.i(LOGTAG, "Max Database Opened");
        database = dbhelper.getWritableDatabase();
    }

    public void close()
    {
        Log.i(LOGTAG, "Database Closed");
        dbhelper.close();
    }

    //*********Max Table Manipulation***********

    //Throws Exception if the Workout is already in the database
    public MaxLift createMaxEntry(MaxLift max) throws Exception
    {
        if(maxExists(max))
        {
            Exception e = new Exception("Invalid Input");
            throw e;
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_MAX_NAME, max.get_exerciseName());
        values.put(DatabaseHelper.COLUMN_MAX_WEIGHT, max.get_weight());
        values.put(DatabaseHelper.COLUMN_MAX_DATE, max.get_date());
        long insertid = database.insert(DatabaseHelper.TABLE_MAXES, null, values);
        max.set_id(insertid);

        return max;
    }

    //Throws Exception if the max ID is not in the database
    public boolean removeMax(long max_id) throws Exception
    {
        if(validMaxID(max_id) == false)
        {
            Exception e = new Exception("Invalid Input");
            throw e;
        }

        String where = DatabaseHelper.COLUMN_MAX_ID + "=" + max_id;
        int result = database.delete(DatabaseHelper.TABLE_MAXES, where, null);
        return (result == 1);
    }

    //Throws Exception if the Workout is already in the database
    public boolean updateMax(MaxLift max) throws Exception
    {
        if(maxExists(max))
        {
            Exception e = new Exception("Invalid Input");
            throw e;
        }

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

    public boolean maxExists(MaxLift maxLift)
    {
        String where = DatabaseHelper.COLUMN_MAX_NAME + "=" + "\"" + maxLift.get_exerciseName() + "\"";
        Cursor cursor = database.query(DatabaseHelper.TABLE_MAXES, allColumnsMaxes, where, null, null, null, null);
        if(cursor.getCount() > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean validMaxID(long max_id)
    {
        String where = DatabaseHelper.COLUMN_MAX_ID + "=" + max_id +";";
        Cursor cursor = database.query(DatabaseHelper.TABLE_MAXES, allColumnsMaxes, where, null, null, null, null);
        if(cursor.getCount() > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}