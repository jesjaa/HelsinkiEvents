package jessej.helsinkievents;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Jesse on 29.11.2017.
 */

public class KeywordExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listTitles;
    private HashMap<String, List<Keyword>> hashmapKeywords;

    public KeywordExpandableListAdapter(Context context, List<String> listTitles, HashMap<String, List<Keyword>> hashmapKeywords) {
        this.context = context;
        this.listTitles = listTitles;
        this.hashmapKeywords = hashmapKeywords;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.hashmapKeywords.get(this.listTitles.get(listPosition)).get(expandedListPosition).getName();
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(final int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }

        // Childina meillä on checkbox ja tarvitsemme tiedon onko se kytketty vai ei
        final CheckBox expandedListCheckBox = (CheckBox) convertView.findViewById(R.id.cbExpandableItem);
        expandedListCheckBox.setText(expandedListText);



        expandedListCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    hashmapKeywords.get(listTitles.get(listPosition)).get(expandedListPosition).setSelected(true);
                } else {
                    hashmapKeywords.get(listTitles.get(listPosition)).get(expandedListPosition).setSelected(false);
                }
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.hashmapKeywords.get(this.listTitles.get(listPosition)).size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.listTitles.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listTitles.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }

        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.tvExpandableTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
