package twfq5.mail.umkc.edu.miworkoutpartner;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class ViewVideo extends AppCompatActivity {
    private Model model;
    private long passedInExerciseVideoID;
    private String passedInExerciseVideoName;
    private String passedInExerciseVideoURL;
    private static final String LOGTAG = "miworkoutpartner";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras == null)
        {
            passedInExerciseVideoID = 0;
            passedInExerciseVideoName = "";
            passedInExerciseVideoURL = "";
            setTitle(passedInExerciseVideoName);
        }
        else
        {
            passedInExerciseVideoID = extras.getLong(getString(R.string.EXTRA_EXERCISE_VIEW_ID));
            passedInExerciseVideoName = extras.getString(getString(R.string.EXTRA_EXERCISE_VIEW_NAME));
            passedInExerciseVideoURL = extras.getString(getString(R.string.EXTRA_EXERCISE_VIEW_URL));
            setTitle(passedInExerciseVideoName);
            Log.i(LOGTAG, "The exercisevideoID passed in is: " + passedInExerciseVideoID);
        }
        model = Model.instance(this.getApplicationContext());
        VideoView vid = (VideoView) findViewById(R.id.videoView);
        Uri video = Uri.parse(passedInExerciseVideoURL);
        vid.setVideoURI(video);
        vid.setMediaController(new MediaController(this));
        vid.requestFocus();
        vid.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_video, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
