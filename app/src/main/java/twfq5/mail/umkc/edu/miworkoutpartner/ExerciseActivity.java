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
import android.view.LayoutInflater;
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

import java.util.List;

public class ExerciseActivity extends ListActivity implements View.OnClickListener
{
    private Model model;
    private long passedInWorkoutID;
    private String passedInWorkoutName;
    private static final String LOGTAG = "miworkoutpartner";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras == null)
        {
            passedInWorkoutID = 0;
            passedInWorkoutName = "Workout";
            TextView workoutNameText = (TextView)findViewById(R.id.exercise_workout_name);
            workoutNameText.setText(passedInWorkoutName);
        }
        else
        {
            passedInWorkoutID = extras.getLong(getString(R.string.EXTRA_WORKOUTID));
            passedInWorkoutName = extras.getString(getString(R.string.EXTRA_WORKOUTNAME));
            Log.i(LOGTAG, "The workoutID passed in is: " + passedInWorkoutID);
            TextView workoutNameText = (TextView)findViewById(R.id.exercise_workout_name);
            workoutNameText.setText(passedInWorkoutName);
        }
        model = Model.instance(this.getApplicationContext());

        View addExerciseButton = findViewById(R.id.add_exercise_button);
        addExerciseButton.setOnClickListener(this);

        this.registerForContextMenu(findViewById(android.R.id.list));

        ListView exerciseList = (ListView)findViewById(android.R.id.list);
        exerciseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //Intent to go to the Set Activity
                //Passes the exercise's id and name
                Exercise e = (Exercise) parent.getItemAtPosition(position);
                Intent intent = new Intent(ExerciseActivity.this, SetActivity.class);
                long exerciseID = e.get_id();
                String exerciseName = e.get_exercisename();
                Bundle extras = new Bundle();
                extras.putLong(getString(R.string.EXTRA_EXERCISEID), exerciseID);
                extras.putString(getString(R.string.EXTRA_EXERCISENAME), exerciseName);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        showList();
    }

    public void showList()
    {
        List<Exercise> exercises = model.findCertainExercises(passedInWorkoutID);

        ArrayAdapter<Exercise> adapter = new ArrayAdapter<Exercise>(this, android.R.layout.simple_list_item_1, exercises);
        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercise, menu);
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
        else if (id == R.id.action_help_exercise)
        {
            //Intent to go to the Help Activity
            //Passes the a string
            Intent intent = new Intent(ExerciseActivity.this, HelpScreenActivity.class);
            Bundle extras = new Bundle();
            String s = "manage_exercise";
            extras.putString(getString(R.string.EXTRA_HELP), s);
            intent.putExtras(extras);
            startActivity(intent);
        }
        else if(id == R.id.action_watch_exercise_in_activity)
        {
            //Intent to go to the ViewExerciseVideos Activity
            //Passes the passed in workout's name and id
            Intent intent = new Intent(ExerciseActivity.this, ViewExerciseVideos.class);
            Bundle extras = new Bundle();
            String s = passedInWorkoutName;
            extras.putString(getString(R.string.EXTRA_EXERCISE_ACTIVITY_WNAME), s);
            long l = passedInWorkoutID;
            extras.putLong(getString(R.string.EXTRA_EXERCISE_ACTIVITY_WID), l);
            intent.putExtras(extras);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        showList();
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.add_exercise_button)
        {
            addExercise();
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exercise_context_menu, menu);
        menu.setHeaderTitle("Manage Exercise");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.delete_exercise_button_yes)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Exercise e = (Exercise) getListAdapter().getItem(info.position);
            deleteExercise(e);
        }
        else if(id == R.id.edit_exercise_button)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Exercise e = (Exercise) getListAdapter().getItem(info.position);
            editExercise(e);
        }

        return super.onContextItemSelected(item);
    }

    public void addExercise()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View addExerciseNameView = inflater.inflate(R.layout.dialog_exercise, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(addExerciseNameView);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                EditText exerciseName = (EditText) addExerciseNameView.findViewById(R.id.dialog_exercisename_edittext);
                String curExerciseName = exerciseName.getText().toString();
                try
                {
                    Exercise exercise = new Exercise();
                    exercise.set_exercisename(curExerciseName);
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
                    //Shuts keyboard
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null
                            : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    //Display list
                    showList();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void editExercise(final Exercise exercise)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View addExerciseNameView = inflater.inflate(R.layout.dialog_exercise, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(addExerciseNameView);

        EditText recentMaxName = (EditText) addExerciseNameView.findViewById(R.id.dialog_exercisename_edittext);
        recentMaxName.setText(exercise.get_exercisename());

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText exerciseName = (EditText) addExerciseNameView.findViewById(R.id.dialog_exercisename_edittext);
                String curExerciseName = exerciseName.getText().toString();
                try
                {
                    exercise.set_exercisename(curExerciseName);
                    model.updateExercise(exercise);
                }
                catch (Exception e)
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
                    //Shuts keyboard
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null
                            : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    //Display list
                    showList();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void deleteExercise(final Exercise exercise)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View deleteExerciseView = inflater.inflate(R.layout.dialog_delete, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(deleteExerciseView);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try
                {
                    model.removeEntireExercise(exercise.get_id());
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    showList();
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}
