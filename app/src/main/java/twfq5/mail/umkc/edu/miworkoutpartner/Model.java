package twfq5.mail.umkc.edu.miworkoutpartner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanner on 10/7/2015.
 */
public class Model
{
    private static final String LOGTAG = "miworkoutpartner";
    private static Model instance = null;

    private DataSourceWorkout dataSourceWorkout;
    private DataSourceExercise dataSourceExercise;
    private DataSourceSet dataSourceSet;
    private DataSourceMax dataSourceMax;

    //Singleton Design Pattern
    private Model(Context context) {
        dataSourceWorkout = DataSourceWorkout.instance(context.getApplicationContext());
        dataSourceExercise = DataSourceExercise.instance(context.getApplicationContext());
        dataSourceSet = DataSourceSet.instance(context.getApplicationContext());
        dataSourceMax = DataSourceMax.instance(context.getApplicationContext());
    }

    public synchronized static Model instance(Context context)
    {
        if( instance == null )
        {
            instance = new Model(context);
        }
        return instance;
    }

    //*********Open/Close Function Calls***********
    public void openWorkout()
    {
        dataSourceWorkout.open();
    }

    public void openExercise()
    {
        dataSourceExercise.open();
    }

    public void openSet()
    {
        dataSourceSet.open();
    }

    public void openMax()
    {
        dataSourceMax.open();
    }

    public void closeWorkout()
    {
        dataSourceWorkout.close();
    }

    public void closeExercise()
    {
        dataSourceExercise.close();
    }

    public void closeSet()
    {
        dataSourceSet.close();
    }

    public void closeMax()
    {
        dataSourceMax.close();
    }


    //*********Workout Function Calls***********

    //Throws Exception if the Workout is already in the database
    public Workout createWorkoutEntry(Workout workout) throws Exception
    {
        openWorkout();
        Workout result = dataSourceWorkout.createWorkoutEntry(workout);
        closeWorkout();
        return result;
    }

    //Throws Exception if the Workout ID is not in the database
    public boolean removeWorkout(long workout_id) throws Exception
    {
        openWorkout();
        boolean result = dataSourceWorkout.removeWorkout(workout_id);
        closeWorkout();
        return result;
    }

    //Throws Exception if the Workout is already in the database
    public boolean updateWorkout(Workout workout) throws Exception
    {
        openWorkout();
        boolean result = dataSourceWorkout.updateWorkout(workout);
        closeWorkout();
        return result;
    }

    public List<Workout> findAllWorkouts()
    {
        openWorkout();
        List<Workout> result= dataSourceWorkout.findAllWorkouts();
        closeWorkout();
        return result;
    }

    //*********Exercise Function Calls ***********

    //Throws Exception if Exercise with same Name and workoutID already exists in the database
    public Exercise createExerciseEntry(Exercise exercise) throws Exception
    {
        openExercise();
        Exercise result = dataSourceExercise.createExerciseEntry(exercise);
        closeExercise();
        return result;
    }

    //Throws Exception if the Exercise ID is not in the database
    public boolean removeExercise(long exercise_id) throws Exception
    {
        openExercise();
        boolean result = dataSourceExercise.removeExercise(exercise_id);
        closeExercise();
        return result;
    }

    public boolean removeAssociatedExercises(long workout_id)
    {
        openExercise();
        boolean result = dataSourceExercise.removeAssociatedExercises(workout_id);
        closeExercise();
        return result;
    }

    //Throws Exception if Exercise with same Name and workoutID already exist in the database
    public boolean updateExercise(Exercise exercise) throws Exception
    {
        openExercise();
        boolean result = dataSourceExercise.updateExercise(exercise);
        closeExercise();
        return result;
    }

    public long countExerciseRows()
    {
        openExercise();
        long result = dataSourceExercise.countExerciseRows();
        closeExercise();
        return result;
    }

    public List<Exercise> findCertainExercises(long workoutID)
    {
        openExercise();
        List<Exercise> result = dataSourceExercise.findCertainExercises(workoutID);
        closeExercise();
        return result;
    }

    public ArrayList<String> findCertainExercisesToString(long workout_id)
    {
        openExercise();
        ArrayList<String> result = dataSourceExercise.findCertainExercisesToString(workout_id);
        closeExercise();
        return result;
    }

    //*********Set Function Calls***********
    public Set createSetEntry(Set set)
    {
        openSet();
        Set result = dataSourceSet.createSetEntry(set);
        closeSet();
        return result;
    }

    //Throws Exception if the Set ID is not in the database
    public boolean removeSet(long set_id) throws Exception
    {
        openSet();
        boolean result = dataSourceSet.removeSet(set_id);
        closeSet();
        return result;
    }

    public boolean removeAssociatedSetExercise(long exercise_id)
    {
        openSet();
        boolean result = dataSourceSet.removeAssociatedSetExercise(exercise_id);
        closeSet();
        return result;
    }

    public void removeAssociatedSetWorkout(long workout_id)
    {
        openSet();
        dataSourceSet.removeAssociatedSetWorkout(workout_id);
        closeSet();

    }

    public long countSetRows()
    {
        openSet();
        long result = dataSourceSet.countSetRows();
        closeSet();
        return result;
    }

    public boolean updateSet(Set set)
    {
        openSet();
        boolean result = dataSourceSet.updateSet(set);
        closeSet();
        return result;
    }

    public List<Set> findCertainSets(long exerciseID)
    {
        openSet();
        List<Set> result = dataSourceSet.findCertainSets(exerciseID);
        closeSet();
        return result;
    }

    public ArrayList<ArrayList<SetCarrier>> findCertainSetsToString(long workout_id)
    {
        openSet();
        ArrayList<ArrayList<SetCarrier>> result = dataSourceSet.findCertainSetsToString(workout_id);
        closeSet();
        return result;
    }

    public double countAllRelatedCompleted(long workout_id)
    {
        openSet();
        double result = dataSourceSet.countAllRelatedCompleted(workout_id);
        closeSet();
        return result;
    }

    public double countAllRelated(long workout_id)
    {
        openSet();
        double result = dataSourceSet.countAllRelated(workout_id);
        closeSet();
        return result;
    }

    //*********Max Function Calls***********

    //Throws Exception if the Max is already in the database
    public MaxLift createMaxEntry(MaxLift max) throws Exception
    {
        openMax();
        MaxLift result = dataSourceMax.createMaxEntry(max);
        closeMax();
        return result;
    }

    //Throws Exception if the max ID is not in the database
    public boolean removeMax(long max_id)   throws Exception
    {
        openMax();
        boolean result = dataSourceMax.removeMax(max_id);
        closeMax();
        return result;
    }

    //Throws Exception if the Max is already in the database
    public boolean updateMax(MaxLift max) throws Exception
    {
        openMax();
        boolean result = dataSourceMax.updateMax(max);
        closeMax();
        return result;
    }

    public List<MaxLift> findAllMaxes()
    {
        openMax();
        List<MaxLift> result = dataSourceMax.findAllMaxes();
        closeMax();
        return result;
    }

}
