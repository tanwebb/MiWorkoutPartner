package twfq5.mail.umkc.edu.miworkoutpartner;

import java.util.ArrayList;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity_elv extends ExpandableListActivity implements View.OnClickListener{

    private Model model;
    private long passedInWorkoutID;
    private String passedInWorkoutName;
    private static final String LOGTAG = "miworkoutpartner";
    private ArrayList<String> parentItems = new ArrayList<String>();
    private ArrayList<ArrayList<SetCarrier>> childItems = new ArrayList<ArrayList<SetCarrier>>();
    private ExpandableListView expandableList;
    private int progress;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // this is not really  necessary as ExpandableListActivity contains an ExpandableList
        setContentView(R.layout.activity_main_activity_elv);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras == null)
        {
            passedInWorkoutID = 0;
            passedInWorkoutName = "Workout";
            TextView workoutNameText = (TextView)findViewById(R.id.textview_workouts_name);
            workoutNameText.setText(passedInWorkoutName);
        }
        else
        {
            passedInWorkoutID = extras.getLong(getString(R.string.EXTRA_WORKOUTID_ELV));
            passedInWorkoutName = extras.getString(getString(R.string.EXTRA_WORKOUTNAME_ELV));
            Log.i(LOGTAG, "The workoutID passed in is: " + passedInWorkoutID + passedInWorkoutName);
            TextView workoutName = (TextView)findViewById(R.id.textview_workouts_name);
            workoutName.setText(passedInWorkoutName);
        }
        model = Model.instance(this.getApplicationContext());

        View resetWorkoutButton = findViewById(R.id.workout_reset_button);
        resetWorkoutButton.setOnClickListener(this);

        expandableList = (ExpandableListView)findViewById(android.R.id.list); // you can use (ExpandableListView) findViewById(R.id.list)
        expandableList.setDividerHeight(2);

        progressBar = (ProgressBar)findViewById(R.id.progressBar1);
        progressBar.setMax(100);

        this.registerForContextMenu(expandableList);
        showList();
        /*
        //Expands Groups from the beginning
        for(int i=0; i < adapter.getGroupCount(); i++)
        {
            expandableList.expandGroup(i);
        }

        //Makes it so the groups aren't collapsible
        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return true; // This way the expander cannot be collapsed
            }
        });*/
    }

    public void showList()
    {
        ArrayList<Integer> currentlyExpandedGroups = new ArrayList<Integer>();
        currentlyExpandedGroups = getExpandedGroups();

        setGroupParents();
        setChildData();

        MyExpandableAdapter adapter = new MyExpandableAdapter(this, parentItems, childItems);
        setListAdapter(adapter);

        //Make sure groups that were opened before refreshing the screen, remain open
        for(int i = 0; i < adapter.getGroupCount(); i++)
        {
            if(currentlyExpandedGroups.contains(i))
            {
                expandableList.expandGroup(i);
            }
        }

        //Calculate the number of workouts completed vs uncompleted
        //Update the progress bar accordingly
        double progressCalculation = (model.countAllRelatedCompleted(passedInWorkoutID) /
                                        model.countAllRelated(passedInWorkoutID)) * 100;
        progress = (int) progressCalculation;
        progressBar.setProgress(progress);

    }

    public void setGroupParents() {
        parentItems = model.findCertainExercisesToString(passedInWorkoutID);
    }

    public void setChildData() {
        childItems = model.findCertainSetsToSetCarrier(passedInWorkoutID);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        Log.i("", "Click");
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;

        int type = ExpandableListView.getPackedPositionType(info.packedPosition);

        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD)
        {
            // Inflate the menu; this adds items to the action bar if it is present.
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_elv_set, menu);
            menu.setHeaderTitle("Set Completed?");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) item
                .getMenuInfo();

        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        int groupPosition = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        int childPosition = ExpandableListView.getPackedPositionChild(info.packedPosition);
        int id = item.getItemId();
        final int NOT_COMPLETED = 0;
        final int COMPLETED = 1;

        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD)
        {
            if(id == R.id.complete_set_button_yes)
            {
                Log.i(LOGTAG, "in completed click Set clicked:" + groupPosition + " " + childPosition);
                Set s = (Set) getExpandableListAdapter().getChild(groupPosition, childPosition);
                if(s != null)
                {
                    s.set_completed(COMPLETED);
                    model.updateSet(s);
                    Log.i(LOGTAG, "Set clicked:" + s.get_id());
                    showList();
                }
            }
            else if(id == R.id.complete_set_button_no)
            {
                Log.i(LOGTAG, "in not completed click Set clicked:" + groupPosition + " " + childPosition);
                Set s = (Set) getExpandableListAdapter().getChild(groupPosition, childPosition);
                if(s != null)
                {
                    s.set_completed(NOT_COMPLETED);
                    model.updateSet(s);
                    Log.i(LOGTAG, "Set clicked:" + s.get_id());
                    showList();
                }
            }
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity_elv, menu);
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
        else if (id == R.id.action_help_main_elv)
        {
            Intent intent = new Intent(MainActivity_elv.this, HelpScreenActivity.class);
            Bundle extras = new Bundle();
            String s = "view_workout";
            extras.putString(getString(R.string.EXTRA_HELP), s);
            intent.putExtras(extras);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.workout_reset_button)
        {
            model.resetCompletedSetsRelated(passedInWorkoutID);
        }

        showList();
    }

    public ArrayList<Integer> getExpandedGroups()
    {
        ArrayList<Integer> curExpanded = new ArrayList<Integer>();
        for(int i = 0; i < parentItems.size(); i++)
        {
            if(expandableList.isGroupExpanded(i))
            {
                curExpanded.add(i);
            }
        }
        return curExpanded;
    }


}
