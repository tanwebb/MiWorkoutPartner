package twfq5.mail.umkc.edu.miworkoutpartner;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity implements View.OnClickListener{

    private static final String LOGTAG = "miworkoutpartner";
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        model = Model.instance(this.getApplicationContext());

        View addWorkoutButton = findViewById(R.id.addWorkoutButton);
        addWorkoutButton.setOnClickListener(this);

        this.registerForContextMenu(findViewById(android.R.id.list));

        ListView workoutList = (ListView)findViewById(android.R.id.list);
        workoutList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id)
            {
                //Intent to go to the Exercise Activity
                //Passes the workout's id and name
                Workout w = (Workout)parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, ExerciseActivity.class);
                long workoutID = w.get_id();
                String workoutName = w.get_workoutname();
                Bundle extras = new Bundle();
                extras.putLong(getString(R.string.EXTRA_WORKOUTID), workoutID);
                extras.putString(getString(R.string.EXTRA_WORKOUTNAME), workoutName);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        showList();
    }

    public void showList()
    {
        List<Workout> workouts = model.findAllWorkouts();
        ArrayAdapter<Workout> adapter = new ArrayAdapter<Workout>(this, android.R.layout.simple_list_item_1, workouts);
        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        else if (id == R.id.action_help_main)
        {
            Intent intent = new Intent(MainActivity.this, HelpScreenActivity.class);
            Bundle extras = new Bundle();
            String s = "manage_workout";
            extras.putString(getString(R.string.EXTRA_HELP), s);
            intent.putExtras(extras);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.addWorkoutButton)
        {
            addWorkout();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.workout_context_menu, menu);
        menu.setHeaderTitle("Manage Workout");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.delete_workout_button_yes)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Workout w = (Workout) getListAdapter().getItem(info.position);
            deleteWorkout(w);
        }
        else if(id == R.id.edit_workout_button)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Workout w = (Workout) getListAdapter().getItem(info.position);
            editWorkout(w);

        }

        return super.onContextItemSelected(item);
    }

    public void addWorkout()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View addWorkoutNameView = inflater.inflate(R.layout.dialog_workout, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(addWorkoutNameView);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                EditText workoutName = (EditText) addWorkoutNameView.findViewById(R.id.dialog_workoutname_edittext);
                String curWorkoutName = workoutName.getText().toString();
                try
                {
                    Workout workout = new Workout();
                    workout.set_workoutname(curWorkoutName);
                    workout = model.createWorkoutEntry(workout);
                    Log.i(LOGTAG, "Workout created with id:" + workout.get_id() + " and name:" + workout.get_workoutname());
                }
                catch(Exception e)
                {
                    System.out.println("Invalid Input");
                    e.printStackTrace();

                    String errorText = "Workout Already Exists";
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

    public void editWorkout(final Workout workout)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View addWorkoutNameView = inflater.inflate(R.layout.dialog_workout, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(addWorkoutNameView);
        EditText workoutName = (EditText) addWorkoutNameView.findViewById(R.id.dialog_workoutname_edittext);
        workoutName.setText(workout.get_workoutname());

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText workoutName = (EditText) addWorkoutNameView.findViewById(R.id.dialog_workoutname_edittext);
                String curWorkoutName = workoutName.getText().toString();

                try
                {
                    workout.set_workoutname(curWorkoutName);
                    model.updateWorkout(workout);
                }
                catch(Exception e)
                {
                    System.out.println("Invalid Input");
                    e.printStackTrace();

                    String errorText = "Workout Name Already Exists";
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

    public void deleteWorkout(final Workout workout)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View deleteWorkoutView = inflater.inflate(R.layout.dialog_delete, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(deleteWorkoutView);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try
                {
                    model.removeEntireWorkout(workout.get_id());
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
