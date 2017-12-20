package jessej.helsinkievents;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Jesse on 3.12.2017.
 */

public class GalleryAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> urls;

    public GalleryAdapter(Activity context, ArrayList<String> urls) {
        super(context, R.layout.list_image, urls);

        this.context = context;
        this.urls = urls;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_image, null,true);

        ImageView ivGalleryImage = (ImageView) rowView.findViewById(R.id.ivGalleryImage);

        String singleUrl = urls.get(position);

        Picasso.with(getContext())
                .load(singleUrl)
                .into(ivGalleryImage);

        return rowView;
    }


}
