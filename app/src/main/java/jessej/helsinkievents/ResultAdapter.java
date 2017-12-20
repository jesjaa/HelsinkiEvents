package jessej.helsinkievents;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jesse on 2.12.2017.
 */

public class ResultAdapter extends ArrayAdapter<Event> {

    private List<Event> listEvents;
    Context mContext;

    private static class ViewHolder {
        ConstraintLayout clResultItem;
        TextView tvResultTitle;
        TextView tvResultDateStartEnd;
        TextView tvResultDescription;
        Button btnResultDetails;
    }

    public ResultAdapter (List<Event> list, Context context) {
        super(context, R.layout.list_result_item, list);
        this.listEvents = list;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Event event = getItem(position);

        final ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_result_item, parent, false);

            viewHolder.clResultItem = (ConstraintLayout) convertView.findViewById(R.id.clResultItem);
            viewHolder.tvResultTitle = (TextView) convertView.findViewById(R.id.tvResultTitle);
            viewHolder.tvResultDateStartEnd = (TextView) convertView.findViewById(R.id.tvResultDateStartEnd);
            viewHolder.tvResultDescription = (TextView) convertView.findViewById(R.id.tvResultDescription);
            viewHolder.btnResultDetails = (Button) convertView.findViewById(R.id.btnResultDetails);

            viewHolder.tvResultDescription.setVisibility(View.GONE);


            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.clResultItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.tvResultDescription.getVisibility() == View.GONE) {
                    viewHolder.tvResultDescription.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.tvResultDescription.setVisibility(View.GONE);
                }
            }
        });


        viewHolder.tvResultTitle.setText(event.getName());
        viewHolder.tvResultDescription.setText(event.getShort_description());
        viewHolder.tvResultDateStartEnd.setText(event.getTime_range());
        viewHolder.btnResultDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO avaa uus aktiviteetti
                String extraEventId = event.getId();
                Intent intentDetail = new Intent(getContext(), DetailActivity.class);
                intentDetail.putExtra("extra_event_id", extraEventId);
                getContext().startActivity(intentDetail);
            }
        });

        return convertView;
    }

    public void onItemClick(AdapterView parent, View v, int position, long id) {

    }

    
}
