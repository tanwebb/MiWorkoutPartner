package twfq5.mail.umkc.edu.miworkoutpartner;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ViewExerciseVideos extends ListActivity implements View.OnClickListener {

    private Model model;
    private static final String LOGTAG = "miworkoutpartner";
    private long passedInWorkoutID; //Need this for when trying to add an exerciseVideo to the workout
    private String passedInWorkoutName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercise_videos);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras == null)
        {
            passedInWorkoutID = 0;
            passedInWorkoutName = "";
        }
        else
        {
            passedInWorkoutID = extras.getLong(getString(R.string.EXTRA_EXERCISE_ACTIVITY_WID));
            passedInWorkoutName = extras.getString(getString(R.string.EXTRA_EXERCISE_ACTIVITY_WNAME));
            Log.i(LOGTAG, "The workoutID passed in is: " + passedInWorkoutID);
        }
        model = Model.instance(this.getApplicationContext());

        //Only enable adding when videos are accessed through exercise activity, not homescreen
        if(passedInWorkoutID > 0)
        {
            this.registerForContextMenu(findViewById(android.R.id.list));
        }

        loadExerciseVideos(); //Will only load videos the first time or when an array is updated

        showAlert();

        View searchVideoButton = findViewById(R.id.search_button_view_videos);
        searchVideoButton.setOnClickListener(this);

        ListView ExerciseVideoList = (ListView)findViewById(android.R.id.list);
        ExerciseVideoList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id)
            {
                //Intent to go to the ViewVideo Activity
                //Passes the exerciseVideo's name, id, and url
                ExerciseVideo ev = (ExerciseVideo)parent.getItemAtPosition(position);
                Intent intent = new Intent(ViewExerciseVideos.this, ViewVideo.class);
                long videoID = ev.get_id();
                String videoName = ev.get_videoName();
                String videoURL = ev.get_videoURL();
                Bundle extras = new Bundle();
                extras.putLong(getString(R.string.EXTRA_EXERCISE_VIEW_ID), videoID);
                extras.putString(getString(R.string.EXTRA_EXERCISE_VIEW_NAME), videoName);
                extras.putString(getString(R.string.EXTRA_EXERCISE_VIEW_URL), videoURL);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        showList();
    }

    public void showList()
    {
        List<ExerciseVideo> exerciseVideos = model.findAllExerciseVideos();

        ArrayAdapter<ExerciseVideo> adapter = new ArrayAdapter<ExerciseVideo>(this, android.R.layout.simple_list_item_1, exerciseVideos);
        setListAdapter(adapter);
    }

    public void showListSearch(String searchValue)
    {
        List<ExerciseVideo> exerciseVideos = model.findAllExerciseVideosSearch(searchValue);

        ArrayAdapter<ExerciseVideo> adapter = new ArrayAdapter<ExerciseVideo>(this, android.R.layout.simple_list_item_1, exerciseVideos);
        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_exercise_videos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }
        else if (id == R.id.action_help_exercise_videos)
        {
            Intent intent = new Intent(ViewExerciseVideos.this, HelpScreenActivity.class);
            Bundle extras = new Bundle();
            String s = "view_videos";
            extras.putString(getString(R.string.EXTRA_HELP), s);
            intent.putExtras(extras);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.search_button_view_videos)
        {
            EditText searchValue = (EditText) findViewById(R.id.search_text_view_videos);
            String curSearchValue = searchValue.getText().toString();

            //Shuts keyboard
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null
                    : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            showListSearch(curSearchValue);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exercise_video_context_menu, menu);
        menu.setHeaderTitle("Video Options");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.add_exercise_video_button)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            ExerciseVideo exerciseVideo = (ExerciseVideo) getListAdapter().getItem(info.position);
            addExerciseVideo(exerciseVideo);
        }

        return super.onContextItemSelected(item);
    }

    public void showAlert()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(ViewExerciseVideos.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Must have internet access to view video demonstrations.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void addExerciseVideo(final ExerciseVideo exerciseVideo)
    {
        try
        {
            Exercise exercise = new Exercise();
            exercise.set_exercisename(exerciseVideo.get_videoName());
            exercise.set_workoutid(passedInWorkoutID);
            exercise = model.createExerciseEntry(exercise);
            Log.i(LOGTAG, "Exercise created with id:" + exercise.get_id() + " and name:" + exercise.get_exercisename()
                    + " and workoutid:" + exercise.get_workoutid());
        }
        catch(Exception e)
        {
            System.out.println("Invalid Input");
            e.printStackTrace();

            String errorText = "Exercise Already Exists for this Workout";
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, errorText, duration);
            toast.show();
        }
        finally
        {
            EditText searchValue = (EditText) findViewById(R.id.search_text_view_videos);
            String curSearchValue = searchValue.getText().toString();
            //Return the user to the appropriate prior screen
            if(curSearchValue.equals(""))
            {
                showList();
            }
            else
            {
                showListSearch(curSearchValue);
            }
        }
    }

    public void loadExerciseVideos()
    {
        //Assign Arrays
        //Database will get updated if the size of the array is larger than the number of rows
        String[] exerciseVideoNames = this.getApplicationContext().getResources()
                .getStringArray(R.array.exercise_video_names);
        String[] exerciseVideoUrls = this.getApplicationContext().getResources()
                .getStringArray(R.array.exercise_video_urls);
        model.createAllExerciseVideos(exerciseVideoNames, exerciseVideoUrls);
    }
}
