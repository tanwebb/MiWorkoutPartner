package twfq5.mail.umkc.edu.miworkoutpartner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Tanner on 10/17/2015.
 */

public class CustomAdapterMaxes extends ArrayAdapter<MaxLift> {

    public CustomAdapterMaxes(Context context, int textViewResourceId)
    {
        super(context, textViewResourceId);
    }

    public CustomAdapterMaxes(Context context, int resource, List<MaxLift> items)
    {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.max_lift_element, null);
        }

        MaxLift ml = getItem(position);

        if (ml != null) {
            TextView max_element_name = (TextView) v.findViewById(R.id.max_element_name);
            TextView max_element_weight = (TextView) v.findViewById(R.id.max_element_weight);
            TextView max_element_date = (TextView) v.findViewById(R.id.max_element_date);

            if (max_element_name != null) {
                max_element_name.setText(ml.get_exerciseName());
            }

            if (ml != null) {
                max_element_weight.setText(String.valueOf(ml.get_weight()) + "lbs");
            }

            if (ml != null) {
                max_element_date.setText(ml.get_date());
            }
        }

        return v;
    }

}
