package twfq5.mail.umkc.edu.miworkoutpartner;

/**
 * Created by Tanner on 9/16/2015.
 */
public class Workout
{
    private long _id;
    private String _workoutname;

    public void set_id(long _id)
    {
        this._id = _id;
    }

    public void set_workoutname(String _workoutname)
    {
        this._workoutname = _workoutname;
    }

    public long get_id()
    {
        return _id;
    }

    public String get_workoutname()
    {
        return _workoutname;
    }


    @Override
    public String toString() {
        return get_workoutname();
    }
}
