package twfq5.mail.umkc.edu.miworkoutpartner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Homescreen extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        View workout_button = findViewById(R.id.manage_workout_button);
        View maxes_button = findViewById(R.id.view_maxes_button);
        View show_workout_button = findViewById(R.id.view_workout_button);

        workout_button.setOnClickListener(this);
        maxes_button.setOnClickListener(this);
        show_workout_button.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_homescreen, menu);
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
        if(v.getId() == R.id.manage_workout_button)
        {
            Intent intent = new Intent(Homescreen.this, MainActivity.class);
            startActivity(intent);
            //startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        else if(v.getId() == R.id.view_workout_button)
        {
            Intent intent = new Intent(Homescreen.this, ViewWorkouts.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.view_maxes_button)
        {
            Intent intent = new Intent(Homescreen.this, MaxActivity.class);
            startActivity(intent);
        }



    }
}
