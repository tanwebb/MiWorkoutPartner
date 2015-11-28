package twfq5.mail.umkc.edu.miworkoutpartner;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class ViewWorkouts extends ListActivity implements View.OnClickListener {

    Model model;
    private static final String LOGTAG = "miworkoutpartner";
    String curWorkoutName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_workouts);
        Intent intent = getIntent();
        model = Model.instance(this.getApplicationContext());

        ListView workoutList = (ListView)findViewById(android.R.id.list);
        workoutList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id)
            {
                Workout w = (Workout)parent.getItemAtPosition(position);
                Intent intent = new Intent(ViewWorkouts.this, MainActivity_elv.class);
                long workoutID = w.get_id();
                String workoutName = w.get_workoutname();
                Bundle extras = new Bundle();
                extras.putLong(getString(R.string.EXTRA_WORKOUTID_ELV), workoutID);
                extras.putString(getString(R.string.EXTRA_WORKOUTNAME_ELV), workoutName);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        showList();
    }

    public void showList()
    {
        //model.openWorkout();

        List<Workout> workouts = model.findAllWorkouts();

        ArrayAdapter<Workout> adapter = new ArrayAdapter<Workout>(this, android.R.layout.simple_list_item_1, workouts);
        setListAdapter(adapter);

        //Log.i(LOGTAG, "The current number of sets in the sets table is: " + model.countSetRows());
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        model.openWorkout();
    }

    @Override
    protected void onPause() {
        super.onPause();
        model.closeWorkout();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_workouts, menu);
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

    @Override
    public void onClick(View v) {

    }
}
