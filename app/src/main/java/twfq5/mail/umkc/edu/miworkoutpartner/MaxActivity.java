package twfq5.mail.umkc.edu.miworkoutpartner;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MaxActivity extends ListActivity implements View.OnClickListener
{

    Model model;
    private static final String LOGTAG = "miworkoutpartner";
    //private String curMaxName;
    //private long curMaxWeight;
    //private String curMaxDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_max);
        Intent intent = getIntent();
        model = Model.instance(this.getApplicationContext());

        View plusText = findViewById(R.id.plus_text_max);
        plusText.setOnClickListener(this);

        this.registerForContextMenu(findViewById(android.R.id.list));
        showList();
    }

    public void showList()
    {
        //model.openMax();

        List<MaxLift> maxes = model.findAllMaxes();
        CustomAdapterMaxes adapter = new CustomAdapterMaxes(this, R.layout.max_lift_element, maxes);
        setListAdapter(adapter);
    }

    /*
    private void createEnterMaxData(String enteredName, Long enteredWeight, String enteredDate)
    {
        MaxLift max = new MaxLift();
        max.set_exerciseName(enteredName);
        max.set_weight(enteredWeight);
        max.set_date(enteredDate);
        max = model.createMaxEntry(max);
        Log.i(LOGTAG, "Max created with id:" + max.get_id() + " and name:" + max.get_exerciseName()
                + " and workoutid:" + max.get_weight() + " " + max.get_date());
    }*/

    /*@Override
    protected void onResume() {
        super.onResume();
        model.openMax();
    }

    @Override
    protected void onPause() {
        super.onPause();
        model.closeMax();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_max, menu);
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
    public void onClick(View v)
    {
        if(v.getId() == R.id.plus_text_max)
        {
            addMax();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.max_context_menu, menu);
        menu.setHeaderTitle("Manage Max");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.delete_max_button_yes)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            MaxLift m = (MaxLift) getListAdapter().getItem(info.position);
            deleteMax(m);
        }
        else if(id == R.id.edit_max_button)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            MaxLift m = (MaxLift) getListAdapter().getItem(info.position);
            updateMax(m);
        }


        return super.onContextItemSelected(item);
    }

    public void addMax()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View editMaxView = inflater.inflate(R.layout.dialog_max, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(editMaxView);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                EditText maxName = (EditText) editMaxView.findViewById(R.id.dialog_max_name);
                String curMaxName = maxName.getText().toString();
                if (curMaxName.matches(""))
                {
                    curMaxName = "N/A";
                }

                EditText maxWeight = (EditText) editMaxView.findViewById(R.id.dialog_max_weight);
                String stringMaxWeight = maxWeight.getText().toString();
                long curMaxWeight;
                if (stringMaxWeight.matches("")) //Check for empty text
                {
                    curMaxWeight = 0;
                }
                else
                {
                    curMaxWeight = Long.parseLong(maxWeight.getText().toString());
                }

                EditText maxDate = (EditText) editMaxView.findViewById(R.id.dialog_max_date);
                String curMaxDate = maxDate.getText().toString();
                if (curMaxDate.matches(""))
                {
                    curMaxDate = "N/A";
                }

                try
                {
                    MaxLift max = new MaxLift();
                    max.set_exerciseName(curMaxName);
                    max.set_weight(curMaxWeight);
                    max.set_date(curMaxDate);
                    max = model.createMaxEntry(max);
                    Log.i(LOGTAG, "Max created with id:" + max.get_id() + " and name:" + max.get_exerciseName()
                            + " and workoutid:" + max.get_weight() + " " + max.get_date());
                }
                catch(Exception e)
                {
                    System.out.println("Invalid Input");
                    e.printStackTrace();

                    String errorText = "Max Already Exists";
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

    public void updateMax(final MaxLift m)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View editMaxView = inflater.inflate(R.layout.dialog_max, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(editMaxView);

        EditText recentMaxName = (EditText) editMaxView.findViewById(R.id.dialog_max_name);
        recentMaxName.setText(m.get_exerciseName());

        EditText recentMaxWeight = (EditText) editMaxView.findViewById(R.id.dialog_max_weight);
        recentMaxWeight.setText(Long.toString(m.get_weight()));

        EditText recentMaxDate = (EditText) editMaxView.findViewById(R.id.dialog_max_date);
        recentMaxDate.setText(m.get_date());

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText maxName = (EditText) editMaxView.findViewById(R.id.dialog_max_name);
                String curMaxName = maxName.getText().toString();
                if (curMaxName.matches(""))
                {
                    curMaxName = "N/A";
                }

                EditText maxWeight = (EditText) editMaxView.findViewById(R.id.dialog_max_weight);
                String stringMaxWeight = maxWeight.getText().toString();
                long curMaxWeight;
                if (stringMaxWeight.matches("")) //Check for empty text
                {
                    curMaxWeight = 0;
                }
                else
                {
                    curMaxWeight = Long.parseLong(maxWeight.getText().toString());
                }

                EditText maxDate = (EditText) editMaxView.findViewById(R.id.dialog_max_date);
                String curMaxDate = maxDate.getText().toString();
                if (curMaxDate.matches(""))
                {
                    curMaxDate = "N/A";
                }

                try
                {
                    m.set_exerciseName(curMaxName);
                    m.set_weight(curMaxWeight);
                    m.set_date(curMaxDate);
                    model.updateMax(m);
                }
                catch (Exception e)
                {
                    System.out.println("Invalid Input");
                    e.printStackTrace();

                    String errorText = "Max Already Exists";
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

    public void deleteMax(final MaxLift m)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View deleteMaxView = inflater.inflate(R.layout.dialog_delete, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(deleteMaxView);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try
                {
                    model.removeMax(m.get_id());
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
