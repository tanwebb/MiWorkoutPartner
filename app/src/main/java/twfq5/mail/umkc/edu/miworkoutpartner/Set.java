package twfq5.mail.umkc.edu.miworkoutpartner;

/**
 * Created by Tanner on 9/21/2015.
 */
public class Set
{
    private long _id;
    private long _exerciseid;
    private long _reps;
    private long _weight;
    private int _completed;

    void set_id(long _id)
    {
        this._id = _id;
    }

    public void set_reps(long _reps)
    {
        this._reps = _reps;
    }

    public void set_weight(long _weight)
    {
        this._weight = _weight;
    }

    public void set_exerciseid(long _exerciseid)
    {
        this._exerciseid = _exerciseid;
    }

    public void set_completed(int _completed)
    {
        this._completed = _completed;
    }

    public long get_id()
    {
        return _id;
    }

    public long get_reps()
    {
        return _reps;
    }

    public long get_weight()
    {
        return _weight;
    }

    public long get_exerciseid()
    {
        return _exerciseid;
    }

    public int get_completed()
    {
        return _completed;
    }

    public String toString() {
        //return get_reps() + " reps at " + get_weight() + " pounds" + " eid:" + get_exerciseid() + " sid;" + get_id();
        return get_reps() + " reps x " + get_weight() + "lbs";
    }
}
