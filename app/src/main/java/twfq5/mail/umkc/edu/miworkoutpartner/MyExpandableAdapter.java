package twfq5.mail.umkc.edu.miworkoutpartner;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

public class MyExpandableAdapter extends BaseExpandableListAdapter {

    private static final String LOGTAG = "miworkoutpartner";
    private Context context;
    private ArrayList<ArrayList<SetCarrier>> childItems;
    private ArrayList<String> parentItems;
    private ArrayList<SetCarrier> child;

    public MyExpandableAdapter(Context context, ArrayList<String> parents, ArrayList<ArrayList<SetCarrier>> children) {
        this.parentItems = parents;
        this.childItems = children;
        this.context = context;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        child = childItems.get(groupPosition);
        TextView textView = null;

        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.group_elv, null);
        }

        textView = (TextView) convertView.findViewById(R.id.group_textView);
        textView.setText(child.get(childPosition).getSetString());
        textView.setBackgroundColor(Color.WHITE);

        //When finding sets if there weren't any for a
        //certain exercise the SetCarrier's set was said to be null.
        //This is why this needs to be checked
        if (child.get(childPosition).getSet() != null)
        {
            if (child.get(childPosition).getSet().get_completed() == 1)
            {
                textView.setBackgroundColor(Color.GREEN);
            }
        }
        return convertView;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_elv, null);
        }

        ((CheckedTextView) convertView).setText(parentItems.get(groupPosition));
        ((CheckedTextView) convertView).setChecked(isExpanded);

        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return childItems.get(groupPosition).get(childPosition).getSet();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (childItems.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public int getGroupCount() {
        return parentItems.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }

}