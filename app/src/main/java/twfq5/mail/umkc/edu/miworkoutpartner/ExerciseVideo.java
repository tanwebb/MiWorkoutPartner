package twfq5.mail.umkc.edu.miworkoutpartner;

/**
 * Created by Tanner on 11/29/2015.
 */
public class ExerciseVideo
{
    private long _id;
    private String _videoURL;
    private String _videoName;

    public ExerciseVideo()
    {

    }

    public ExerciseVideo(String videoName, String videoURL)
    {
        this._videoName = videoName;
        this._videoURL = videoURL;
    }

    public String get_videoName() {
        return _videoName;
    }

    public void set_videoName(String _videoName) {
        this._videoName = _videoName;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String get_videoURL() {
        return _videoURL;
    }

    public void set_videoURL(String _videoURL) {
        this._videoURL = _videoURL;
    }

    public String toString() {
        //return get_exerciseName() + ": " + get_weight() + " pounds" + ", " + get_date() + " mid:" + get_id();
        return get_videoName();
    }
}
