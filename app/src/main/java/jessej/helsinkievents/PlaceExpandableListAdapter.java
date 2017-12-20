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

import java.util.HashMap;
import java.util.List;

/**
 * Created by Jesse on 1.12.2017.
 */

public class PlaceExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listTitles;
    private HashMap<String, List<Place>> hashmapPlaces;

    public PlaceExpandableListAdapter(Context context, List<String> listTitles, HashMap<String, List<Place>> hashmapKeywords) {
        this.context = context;
        this.listTitles = listTitles;
        this.hashmapPlaces = hashmapKeywords;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.hashmapPlaces.get(this.listTitles.get(listPosition)).get(expandedListPosition).getName();
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(final int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        final String expandedListDetail = (String) this.hashmapPlaces.get(this.listTitles.get(listPosition)).get(expandedListPosition).getLongaddress();
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_2, null);
        }

        // Childina meillä on checkbox ja tarvitsemme tiedon onko se kytketty vai ei
        final CheckBox expandedListCheckBox = (CheckBox) convertView.findViewById(R.id.cbExpandable2);
        expandedListCheckBox.setText(expandedListText);

        final TextView expandedListTextView = (TextView) convertView.findViewById(R.id.tvExpandable2);
        expandedListTextView.setText(expandedListDetail);


        expandedListCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    hashmapPlaces.get(listTitles.get(listPosition)).get(expandedListPosition).setSelected(true);
                } else {
                    hashmapPlaces.get(listTitles.get(listPosition)).get(expandedListPosition).setSelected(false);
                }
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.hashmapPlaces.get(this.listTitles.get(listPosition)).size();
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
