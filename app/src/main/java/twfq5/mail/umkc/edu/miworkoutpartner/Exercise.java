package twfq5.mail.umkc.edu.miworkoutpartner;

/**
 * Created by Tanner on 9/19/2015.
 */
public class Exercise {

    private long _id;
    private String _exercisename;
    private long _workoutid;

    public void set_id(long _id)
    {
        this._id = _id;
    }

    public void set_exercisename(String _exercisename)
    {
        this._exercisename = _exercisename;
    }

    public void set_workoutid(long _workoutid)
    {
        this._workoutid = _workoutid;
    }

    public long get_id()
    {
        return _id;
    }

    public String get_exercisename()
    {
        return _exercisename;
    }

    public long get_workoutid()
    {
        return _workoutid;
    }


    @Override
    public String toString() {
        //return get_exercisename() + " exerciseid:" + get_id() + " workoutid:" + get_workoutid();
        return get_exercisename();
    }
}
