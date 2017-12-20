package jessej.helsinkievents;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    private ArrayList<String> imageUrls;
    private ConstraintLayout clGalleryImages;
    private ListView lvGalleryImages;
    private ConstraintLayout clGalleryFullScreen;
    private ImageView ivGalleryFimage;




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        //Asetetaan eri niminen title
        String strTitle = getString(R.string.activity_title_gallery);
        SpannableString spanstrTitle = new SpannableString(strTitle);
        spanstrTitle.setSpan(new ForegroundColorSpan(getColor(R.color.colorWhite)), 0, strTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(spanstrTitle);

        imageUrls = new ArrayList<String>();
        imageUrls = DetailActivity.event.getImage_urls();


        Log.d("image count", Integer.toString(imageUrls.size()));

        clGalleryImages = (ConstraintLayout) findViewById(R.id.clGalleryImages);
        lvGalleryImages = (ListView) findViewById(R.id.lvGalleryImages);

        clGalleryFullScreen = (ConstraintLayout) findViewById(R.id.clGalleryFullScreen);
        ivGalleryFimage = (ImageView) findViewById(R.id.ivGalleryFimage);

        clGalleryFullScreen.setVisibility(View.INVISIBLE);

        GalleryAdapter adapter = new GalleryAdapter(this, imageUrls);

        lvGalleryImages.setAdapter(adapter);

        lvGalleryImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String singleUrl = imageUrls.get(position);
                Picasso.with(getApplicationContext())
                        .load(singleUrl)
                        .into(ivGalleryFimage);

                clGalleryImages.setVisibility(View.INVISIBLE);
                clGalleryFullScreen.setVisibility(View.VISIBLE);


            }
        });

        ivGalleryFimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clGalleryImages.setVisibility(View.VISIBLE);
                clGalleryFullScreen.setVisibility(View.INVISIBLE);
            }
        });


    }
}
