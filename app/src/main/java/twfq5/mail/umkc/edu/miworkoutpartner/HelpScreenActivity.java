package twfq5.mail.umkc.edu.miworkoutpartner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class HelpScreenActivity extends AppCompatActivity {

    private static final String LOGTAG = "miworkoutpartner";
    private String passedInValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_screen);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras == null)
        {
            passedInValue = "";
            TextView helpManage = (TextView)findViewById(R.id.help_manage);
            helpManage.setText(passedInValue);
            TextView helpManageText = (TextView)findViewById(R.id.help_manage_text);
            helpManageText.setText(passedInValue);
        }
        else
        {
            passedInValue = extras.getString(getString(R.string.EXTRA_HELP));
            Log.i(LOGTAG, "The value passed in is: " + passedInValue);
        }

        TextView helpManage = (TextView)findViewById(R.id.help_manage);
        TextView helpManageText = (TextView)findViewById(R.id.help_manage_text);

        //Depending on what was passed through the intent,
        // different messages will be shown on the screen
        if(passedInValue.equals("manage_workout"))
        {
            helpManage.setText(R.string.help_manage_workout);
            helpManageText.setText(R.string.help_manage_workout_text);
        }
        else if(passedInValue.equals("manage_exercise"))
        {
            helpManage.setText(R.string.help_manage_exercise);
            helpManageText.setText(R.string.help_manage_exercise_text);
        }
        else if(passedInValue.equals("manage_set"))
        {
            helpManage.setText(R.string.help_manage_set);
            helpManageText.setText(R.string.help_manage_set_text);
        }
        else if(passedInValue.equals("manage_max"))
        {
            helpManage.setText(R.string.help_manage_max);
            helpManageText.setText(R.string.help_manage_max_text);
        }
        else if(passedInValue.equals("view_workout"))
        {
            helpManage.setText(R.string.help_view_workout);
            helpManageText.setText(R.string.help_view_workout_text);
        }
        else if(passedInValue.equals("view_videos"))
        {
            helpManage.setText(R.string.help_view_video);
            helpManageText.setText(R.string.help_view_video_text);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help_screen, menu);
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
