package jessej.helsinkievents;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;




public class DetailActivity extends AppCompatActivity {

    public static Event event;

    private ProgressBar pbDetail;
    private ConstraintLayout clDetailChild;
    private ImageView ivEventFirstImage;
    private TextView tvEventTime;
    private TextView tvEventTitle;
    private MapView mvEventPosition;
    private ScrollView svEventDetailScroll;
    private TextView tvLongDetails;
    private double latitude;
    private double longitude;

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
        setContentView(R.layout.activity_detail);
        initDetail(savedInstanceState);
    }

    private void initDetail(Bundle savedInstanceState) {

        //Asetetaan eri niminen title
        String strTitle = getString(R.string.activity_title_details);
        SpannableString spanstrTitle = new SpannableString(strTitle);
        spanstrTitle.setSpan(new ForegroundColorSpan(getColor(R.color.colorWhite)), 0, strTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(spanstrTitle);

        // Asetetaan komponentit/viewit normaalisti
        pbDetail = (ProgressBar) findViewById(R.id.pbDetail);
        clDetailChild = (ConstraintLayout) findViewById(R.id.clDetailChild);
        ivEventFirstImage = (ImageView) findViewById(R.id.ivEventFirstImage);
        tvEventTime = (TextView) findViewById(R.id.tvEventTime);
        tvEventTitle = (TextView) findViewById(R.id.tvEventTitle);
        svEventDetailScroll = (ScrollView) findViewById(R.id.svEventDetailScroll);
        tvLongDetails = (TextView) findViewById(R.id.tvLongDetails);

        // Karttajutut...
        mvEventPosition = (MapView) findViewById(R.id.mvEventPosition);
        mvEventPosition.onCreate(savedInstanceState);

        clDetailChild.setVisibility(View.INVISIBLE);
        pbDetail.setVisibility(View.VISIBLE);

        // Haetaan extrana tuota eventin id -> jolla saamme muut detailit
        String eventExtraId = "";
        Intent thisIntent = getIntent();
        Bundle bundle = thisIntent.getExtras();
        // Jos extra löytyy (aktiviteettiin päästy oikein tarkoituksin)
        if (bundle != null) {
            eventExtraId = (String) bundle.get("extra_event_id");

            // Haetaan event objekti jokseekin mielenkiintoisella tavalla
            List<Event> tempList = ResultActivity.eventList;
            for (Event e : tempList) {
                if (Objects.equals(e.getId(), eventExtraId)) {
                    // e.getId().contains(eventExtraId)
                    event = e;
                }
            }

            pbDetail.setVisibility(View.INVISIBLE);
            clDetailChild.setVisibility(View.VISIBLE);

            tvEventTime.setText(event.getTime_range());
            tvEventTitle.setText(event.getName());
            if (event.getDescription() != "" && event.getShort_description() != "") {

                // Description lähteenä on höttömöllitekstiä, joten Html.fromHtml metodia tarvitaan me
                String shortDesc = "<b>" + event.getShort_description() + "</b>";
                String longDesc = event.getDescription();
                String prettyDesc = shortDesc + "<br><br>" + longDesc;
                tvLongDetails.setText(Html.fromHtml(prettyDesc, Html.FROM_HTML_MODE_COMPACT));
            } else if (event.getDescription() != "") {
                tvLongDetails.setText(Html.fromHtml(event.getDescription(), Html.FROM_HTML_MODE_COMPACT));
            } else if (event.getShort_description() != "") {
                tvLongDetails.setText(Html.fromHtml(event.getShort_description(),  Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvLongDetails.setText("Ei lisätietoja.");
            }

            loadMap(event.getLatitude(), event.getLongitude());
            loadImage();

            ivEventFirstImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentGallery = new Intent(getApplicationContext(), GalleryActivity.class);
                    startActivity(intentGallery);
                }
            });

        } else {
            // Muuten aktiviteetti suljetaan
            finish();
        }
    }

    private void loadMap(final double latitude, final double longitude) {
        mvEventPosition.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng coordinates = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions().position(coordinates));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15));
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setMapToolbarEnabled(true);
                mvEventPosition.onResume();
            }
        });
    }

    private void loadImage() {
        if (event.getFirstImgUrl() != null) {
            String imageUrl = event.getFirstImgUrl();
            Picasso.with(getApplicationContext())
                    .load(imageUrl)
                    .fit()
                    .centerCrop()
                    .placeholder(R.color.mainBlueBackground)
                    .error(R.color.mainBlueForeground)
                    .into(ivEventFirstImage);

        } else {
            Log.d("img", "null url");
            Guideline glH1 = findViewById(R.id.glDetailH1);
            ConstraintLayout.LayoutParams glParams= (ConstraintLayout.LayoutParams) glH1.getLayoutParams();
            glParams.guidePercent = 0.13f;
            glH1.setLayoutParams(glParams);
        }
    }
}
