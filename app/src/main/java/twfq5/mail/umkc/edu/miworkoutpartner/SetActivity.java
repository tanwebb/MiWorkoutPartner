package twfq5.mail.umkc.edu.miworkoutpartner;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.TextView;

import java.util.List;

public class SetActivity extends ListActivity implements View.OnClickListener {

    private Model model;
    private long passedInExerciseID;
    private String passedInExerciseName;
    private static final String LOGTAG = "miworkoutpartner";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras == null)
        {
            passedInExerciseID = 0;
            passedInExerciseName = "Exercise";
            TextView exerciseNameText = (TextView)findViewById(R.id.set_exercise_name);
            exerciseNameText.setText(passedInExerciseName);
        }
        else
        {
            passedInExerciseID = extras.getLong(getString(R.string.EXTRA_EXERCISEID));
            passedInExerciseName = extras.getString(getString(R.string.EXTRA_EXERCISENAME));
            Log.i(LOGTAG, "The ExerciseID passed in is: " + passedInExerciseID + passedInExerciseName);
            TextView exerciseNameText = (TextView)findViewById(R.id.set_exercise_name);
            exerciseNameText.setText(passedInExerciseName);
        }
        model = Model.instance(this.getApplicationContext());

        View addSetButton = findViewById(R.id.add_set_button);
        addSetButton.setOnClickListener(this);

        this.registerForContextMenu(findViewById(android.R.id.list));

        showList();
    }

    public void showList()
    {
        List<Set> sets = model.findCertainSets(passedInExerciseID);
        ArrayAdapter<Set> adapter = new ArrayAdapter<Set>(this, android.R.layout.simple_list_item_1, sets);
        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set, menu);
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
        else if (id == R.id.action_help_set)
        {
            Intent intent = new Intent(SetActivity.this, HelpScreenActivity.class);
            Bundle extras = new Bundle();
            String s = "manage_set";
            extras.putString(getString(R.string.EXTRA_HELP), s);
            intent.putExtras(extras);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_set_button)
        {
            addSet();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.set_context_menu, menu);
        menu.setHeaderTitle("Manage Set");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.delete_set_button_yes)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Set s = (Set) getListAdapter().getItem(info.position);
            deleteSet(s);
        }
        else if(id == R.id.edit_set_button)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Set s = (Set) getListAdapter().getItem(info.position);
            editSet(s);
        }


        return super.onContextItemSelected(item);
    }

    public void addSet()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View addSetNameView = inflater.inflate(R.layout.dialog_set, null);
        final int NOT_COMPLETED = 0;
        final int COMPLETED = 1;

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(addSetNameView);

        View addRepsDialogButton = addSetNameView.findViewById(R.id.add_reps_button);
        addRepsDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView numberReps = (TextView) addSetNameView.findViewById(R.id.changing_reps);
                String curRepsText = numberReps.getText().toString();
                long curReps = Long.parseLong(curRepsText);
                curReps++;
                numberReps.setText(Long.toString(curReps));
            }
        });

        View subtractRepsDialogButton = addSetNameView.findViewById(R.id.subtract_reps_button);
        subtractRepsDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView numberReps = (TextView) addSetNameView.findViewById(R.id.changing_reps);
                String curRepsText = numberReps.getText().toString();
                long curReps = Long.parseLong(curRepsText);
                if(curReps > 0)
                {
                    curReps--;
                }
                numberReps.setText(Long.toString(curReps));
            }
        });

        View addWeightDialogButton = addSetNameView.findViewById(R.id.add_weight_button);
        addWeightDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView numberWeight = (TextView) addSetNameView.findViewById(R.id.changing_weight);
                String curWeightText = numberWeight.getText().toString();
                long curWeight = Long.parseLong(curWeightText);
                curWeight = curWeight + 5;
                numberWeight.setText(Long.toString(curWeight));
            }
        });

        View subtractWeightDialogButton = addSetNameView.findViewById(R.id.subtract_weight_button);
        subtractWeightDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView numberWeight = (TextView) addSetNameView.findViewById(R.id.changing_weight);
                String curWeightText = numberWeight.getText().toString();
                long curWeight = Long.parseLong(curWeightText);
                if(curWeight > 0)
                {
                    curWeight = curWeight - 5;
                }
                numberWeight.setText(Long.toString(curWeight));
            }
        });

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                TextView numberReps = (TextView) addSetNameView.findViewById(R.id.changing_reps);
                String curRepsText = numberReps.getText().toString();
                long curReps = Long.parseLong(curRepsText);

                TextView numberWeight = (TextView) addSetNameView.findViewById(R.id.changing_weight);
                String curWeightText = numberWeight.getText().toString();
                long curWeight = Long.parseLong(curWeightText);

                Set set = new Set();
                set.set_reps(curReps);
                set.set_weight(curWeight);
                set.set_exerciseid(passedInExerciseID);
                set.set_completed(NOT_COMPLETED);
                set = model.createSetEntry(set);
                Log.i(LOGTAG, "Set created with id:" + set.get_id() + " and reps:" + set.get_reps()
                        + " and weight:" + set.get_weight() + " and eid:" + set.get_exerciseid());

                //Shuts keyboard
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null
                        : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                //Display list
                showList();
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

    public void editSet(final Set set)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View changeSetView = inflater.inflate(R.layout.dialog_set, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(changeSetView);
        TextView changingReps = (TextView) changeSetView.findViewById(R.id.changing_reps);
        changingReps.setText(String.valueOf(set.get_reps()));
        TextView changingWeight = (TextView) changeSetView.findViewById(R.id.changing_weight);
        changingWeight.setText(String.valueOf(set.get_weight()));

        View addRepsDialogButton = changeSetView.findViewById(R.id.add_reps_button);
        addRepsDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView numberReps = (TextView) changeSetView.findViewById(R.id.changing_reps);
                String curRepsText = numberReps.getText().toString();
                long curReps = Long.parseLong(curRepsText);
                curReps++;
                numberReps.setText(Long.toString(curReps));
            }
        });

        View subtractRepsDialogButton = changeSetView.findViewById(R.id.subtract_reps_button);
        subtractRepsDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView numberReps = (TextView) changeSetView.findViewById(R.id.changing_reps);
                String curRepsText = numberReps.getText().toString();
                long curReps = Long.parseLong(curRepsText);
                if(curReps > 0)
                {
                    curReps--;
                }
                numberReps.setText(Long.toString(curReps));
            }
        });

        View addWeightDialogButton = changeSetView.findViewById(R.id.add_weight_button);
        addWeightDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView numberWeight = (TextView) changeSetView.findViewById(R.id.changing_weight);
                String curWeightText = numberWeight.getText().toString();
                long curWeight = Long.parseLong(curWeightText);
                curWeight = curWeight + 5;
                numberWeight.setText(Long.toString(curWeight));
            }
        });

        View subtractWeightDialogButton = changeSetView.findViewById(R.id.subtract_weight_button);
        subtractWeightDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView numberWeight = (TextView) changeSetView.findViewById(R.id.changing_weight);
                String curWeightText = numberWeight.getText().toString();
                long curWeight = Long.parseLong(curWeightText);
                if(curWeight > 0)
                {
                    curWeight = curWeight - 5;
                }
                numberWeight.setText(Long.toString(curWeight));
            }
        });

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                TextView numberReps = (TextView) changeSetView.findViewById(R.id.changing_reps);
                String curRepsText = numberReps.getText().toString();
                long curReps = Long.parseLong(curRepsText);

                TextView numberWeight = (TextView) changeSetView.findViewById(R.id.changing_weight);
                String curWeightText = numberWeight.getText().toString();
                long curWeight = Long.parseLong(curWeightText);

                set.set_reps(curReps);
                set.set_weight(curWeight);
                model.updateSet(set);

                //Shuts keyboard
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null
                        : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                //Display list
                showList();
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

    public void deleteSet(final Set set)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View deleteSetView = inflater.inflate(R.layout.dialog_delete, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(deleteSetView);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try
                {
                    model.removeSet(set.get_id());
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
