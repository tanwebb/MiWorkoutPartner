package twfq5.mail.umkc.edu.miworkoutpartner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.database.DatabaseUtils.queryNumEntries;

/**
 * Created by Tanner on 11/29/2015.
 */
public class DataSourceExerciseVideo
{
    private static final String LOGTAG = "miworkoutpartner";
    private static DataSourceExerciseVideo instance = null;

    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    private DataSourceExerciseVideo(Context context)
    {
        dbhelper = DatabaseHelper.getInstance(context.getApplicationContext());
    }

    public synchronized static DataSourceExerciseVideo instance(Context context)
    {
        if( instance == null )
        {
            instance = new DataSourceExerciseVideo(context);
        }
        return instance;
    }

    public static final String[] allColumnsExerciseVideo=
            {
                    DatabaseHelper.COLUMN_EXERCISE_VIDEOS_ID,
                    DatabaseHelper.COLUMN_EXERCISE_VIDEOS_NAME,
                    DatabaseHelper.COLUMN_EXERCISE_VIDEOS_URL
            };

    public void open() {
        Log.i(LOGTAG, "ExerciseVideo Database Opened");
        database = dbhelper.getWritableDatabase();
    }

    public void close() {
        Log.i(LOGTAG, "ExerciseVideo Database Closed");
        dbhelper.close();
    }

    //*********ExerciseVideo Table Manipulation***********

    //Function loads string arrays from arrays.xml into database
    public void createAllExerciseVideos(String[] names, String[] urls)
    {
        //If rows have already been loaded, no need to add anything to the db
        if(countExerciseVideoRows() < names.length && names.length == urls.length)
        {
            ExerciseVideo exerciseVideo = new ExerciseVideo();
            for(int i = 0; i < names.length; i++)
            {
                exerciseVideo = new ExerciseVideo(names[i], urls[i]);
                insertExerciseVideo(exerciseVideo);
            }
            Log.i(LOGTAG, "Exercise Videos loaded");
        }
        else
        {
            Log.i(LOGTAG, "Exercise Videos were NOT loaded");
        }

    }

    //Function inserts an ExerciseVideo into the EXERCISE_VIDEOS table
    //Returns the exerciseVideo with it's id
    public ExerciseVideo insertExerciseVideo(ExerciseVideo exerciseVideo)
    {
        if(exerciseVideoExists(exerciseVideo) == false)
        {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_EXERCISE_VIDEOS_NAME, exerciseVideo.get_videoName());
            values.put(DatabaseHelper.COLUMN_EXERCISE_VIDEOS_URL, exerciseVideo.get_videoURL());
            long insertid = database.insert(DatabaseHelper.TABLE_EXERCISE_VIDEOS, null, values);
            exerciseVideo.set_id(insertid);
            return exerciseVideo;
        }
        else
        {
            return null;
        }
    }

    //Function removes a row (an ExerciseVideo) from the EXERCISE_VIDEO table
    public boolean removeExerciseVideo(long exercisevideo_id)
    {
        int result = 0;

        if(validExerciseVideoID(exercisevideo_id) == true)
        {
            String where = DatabaseHelper.COLUMN_EXERCISE_VIDEOS_ID + "=" + exercisevideo_id;
            result = database.delete(DatabaseHelper.TABLE_EXERCISE_VIDEOS, where, null);
        }
        return (result == 1);
    }

    public long countExerciseVideoRows()
    {
        long result = queryNumEntries(database, DatabaseHelper.TABLE_EXERCISE_VIDEOS);
        return result;
    }

    //Function finds all rows in the EXERCISE_VIDEO table and returns them in an ArrayList of ExerciseVideos
    public List<ExerciseVideo> findAllExerciseVideos() {
        List<ExerciseVideo> exerciseVideos = new ArrayList<ExerciseVideo>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_EXERCISE_VIDEOS, allColumnsExerciseVideo, null, null, null, null, null);

        Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ExerciseVideo exerciseVideo = new ExerciseVideo();
                exerciseVideo.set_id(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXERCISE_VIDEOS_ID)));
                exerciseVideo.set_videoName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXERCISE_VIDEOS_NAME)));
                exerciseVideo.set_videoURL(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXERCISE_VIDEOS_URL)));

                exerciseVideos.add(exerciseVideo);
            }
        }
        cursor.close();
        return exerciseVideos;
    }

    //Function finds all rows in the EXERCISE_VIDEO table whose name contains the search value
    //and returns them in an ArrayList of ExerciseVideos
    public List<ExerciseVideo> findAllExerciseVideosSearch(String searchVal) {
        List<ExerciseVideo> exerciseVideos = new ArrayList<ExerciseVideo>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_EXERCISE_VIDEOS, allColumnsExerciseVideo, null, null, null, null, null);

        Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ExerciseVideo exerciseVideo = new ExerciseVideo();
                exerciseVideo.set_id(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXERCISE_VIDEOS_ID)));
                exerciseVideo.set_videoName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXERCISE_VIDEOS_NAME)));
                exerciseVideo.set_videoURL(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXERCISE_VIDEOS_URL)));

                if(exerciseVideo.get_videoName().contains(searchVal))
                {
                    exerciseVideos.add(exerciseVideo);
                }
            }
        }
        cursor.close();
        return exerciseVideos;
    }

    //Checks if exerciseVideo with the same name already exists in the EXERCISE_VIDEO table
    public boolean exerciseVideoExists(ExerciseVideo exerciseVideo)
    {
        String where = DatabaseHelper.COLUMN_EXERCISE_VIDEOS_NAME + "=" + "\"" + exerciseVideo.get_videoName() + "\"";
        Cursor cursor = database.query(DatabaseHelper.TABLE_EXERCISE_VIDEOS, allColumnsExerciseVideo, where, null, null, null, null);
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

    //Makes sure that the id from an exerciseVideo is actually in the EXERCISE_VIDEO table
    public boolean validExerciseVideoID(long exercisevideo_id)
    {
        String where = DatabaseHelper.COLUMN_EXERCISE_VIDEOS_ID + "=" + exercisevideo_id +";";
        Cursor cursor = database.query(DatabaseHelper.TABLE_EXERCISE_VIDEOS, allColumnsExerciseVideo, where, null, null, null, null);
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
