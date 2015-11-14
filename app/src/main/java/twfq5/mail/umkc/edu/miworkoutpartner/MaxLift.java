package twfq5.mail.umkc.edu.miworkoutpartner;

/**
 * Created by Tanner on 10/5/2015.
 */
public class MaxLift
{

    private String _exerciseName;
    private long _id;
    private long _weight;
    private String _date;

    public String get_exerciseName() {
        return _exerciseName;
    }

    public void set_exerciseName(String _exerciseName) {
        this._exerciseName = _exerciseName;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long get_weight() {
        return _weight;
    }

    public void set_weight(long _weight) {
        this._weight = _weight;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public String toString() {
        //return get_exerciseName() + ": " + get_weight() + " pounds" + ", " + get_date() + " mid:" + get_id();
        return get_exerciseName() + ": " + get_weight() + " pounds" + " \n" + "-Date: " + get_date();
    }
}
