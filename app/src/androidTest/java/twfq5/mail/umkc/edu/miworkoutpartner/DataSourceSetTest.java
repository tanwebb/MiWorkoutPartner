package twfq5.mail.umkc.edu.miworkoutpartner;

import android.content.Context;
import android.test.AndroidTestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanner on 11/18/2015.
 */
public class DataSourceSetTest extends AndroidTestCase
{
    private Model model;

    public void setUp(  )
    {
        // The dependency here is being satisfied with dependency
        //   injection.
        Context c = getContext();
        model = model.instance(c);
    }

    public void testResetCompletedSetsRelated() throws Exception
    {
        Workout w = new Workout();
        w.set_workoutname("Test Workout");
        w = model.createWorkoutEntry(w);

        Exercise e = new Exercise();
        e.set_workoutid(w.get_id());
        e.set_exercisename("Test Exercise");
        e = model.createExerciseEntry(e);

        Set s = new Set();
        s.set_exerciseid(e.get_id());
        s.set_completed(1);
        s.set_reps(8);
        s.set_weight(225);
        s = model.createSetEntry(s);

        model.resetCompletedSetsRelated(w.get_id());

        List<Set> setList = new ArrayList<Set>();
        setList = model.findCertainSets(e.get_id());
        boolean noCompleted = true;
        for(Set curSet: setList)
        {
            if(curSet.get_completed() == 1)
            {
                noCompleted = false;
                break;
            }
        }

        //Delete all entries so they can be used in another test case
        model.removeAssociatedSetExercise(e.get_id());
        model.removeAssociatedExercises(w.get_id());
        model.removeWorkout(w.get_id());

        assertTrue("All sets did not get reset to not completed", noCompleted);
    }

    public void testCountAllRelated() throws Exception
    {
        Workout w = new Workout();
        w.set_workoutname("Test Workout");
        w = model.createWorkoutEntry(w);

        Exercise e = new Exercise();
        e.set_workoutid(w.get_id());
        e.set_exercisename("Test Exercise");
        e = model.createExerciseEntry(e);

        Set s = new Set();
        s.set_exerciseid(e.get_id());
        s.set_completed(1);
        s.set_reps(8);
        s.set_weight(225);
        s = model.createSetEntry(s);

        Set s2 = new Set();
        s2.set_exerciseid(e.get_id());
        s2.set_completed(0);
        s2.set_reps(8);
        s2.set_weight(225);
        s2 = model.createSetEntry(s2);

        double numberCounted = model.countAllRelated(w.get_id());
        boolean allCounted = false;
        if(numberCounted == 2)
        {
            allCounted = true;
        }

        //Delete all entries so they can be used in another test case
        model.removeAssociatedSetExercise(e.get_id());
        model.removeAssociatedExercises(w.get_id());
        model.removeWorkout(w.get_id());

        assertTrue("All sets did not get counted", allCounted);
    }

    public void testAllRelatedCompleted() throws Exception
    {
        Workout w = new Workout();
        w.set_workoutname("Test Workout");
        w = model.createWorkoutEntry(w);

        Exercise e = new Exercise();
        e.set_workoutid(w.get_id());
        e.set_exercisename("Test Exercise");
        e = model.createExerciseEntry(e);

        Set s = new Set();
        s.set_exerciseid(e.get_id());
        s.set_completed(1);
        s.set_reps(8);
        s.set_weight(225);
        s = model.createSetEntry(s);

        Set s2 = new Set();
        s2.set_exerciseid(e.get_id());
        s2.set_completed(0);
        s2.set_reps(8);
        s2.set_weight(225);
        s2 = model.createSetEntry(s2);

        double numberCounted = model.countAllRelatedCompleted(w.get_id());
        boolean allCounted = false;
        if(numberCounted == 1)
        {
            allCounted = true;
        }

        //Delete all entries so they can be used in another test case
        model.removeAssociatedSetExercise(e.get_id());
        model.removeAssociatedExercises(w.get_id());
        model.removeWorkout(w.get_id());

        assertTrue("Sets that were completed were not counted properly", allCounted);
    }

    public void testRemoveSet() throws Exception
    {
        Workout w = new Workout();
        w.set_workoutname("Test Workout");
        w = model.createWorkoutEntry(w);

        Exercise e = new Exercise();
        e.set_workoutid(w.get_id());
        e.set_exercisename("Test Exercise");
        e = model.createExerciseEntry(e);

        Set s = new Set();
        s.set_exerciseid(e.get_id());
        s.set_completed(1);
        s.set_reps(8);
        s.set_weight(225);
        s = model.createSetEntry(s);

        model.removeSet(s.get_id());

        List<Set> setList = new ArrayList<Set>();
        setList = model.findCertainSets(e.get_id());
        int count = 0;
        boolean result = false;
        for(Set curSet: setList)
        {
            count++;
        }

        if(count == 0)
        {
            result = true;
        }

        //Delete all entries so they can be used in another test case
        model.removeAssociatedSetExercise(e.get_id());
        model.removeAssociatedExercises(w.get_id());
        model.removeWorkout(w.get_id());

        assertTrue("All sets did not get reset to not completed", result);
    }

}
