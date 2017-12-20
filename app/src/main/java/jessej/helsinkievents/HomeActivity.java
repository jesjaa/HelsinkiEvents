package jessej.helsinkievents;

import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {

    private static boolean RUN_ONCE = true;
    private static int eventCount = 0;
    private RequestQueue homeQueue;
    private TextView tvHomeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        homeInit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.search_item:
                //Avataan search aktiviteetti
                Intent intentSearch = new Intent(this, SearchActivity.class);
                startActivity(intentSearch);
                return true;

            case R.id.features_item:
                Intent intentFeature = new Intent(this, FeatureActivity.class);
                startActivity(intentFeature);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void homeInit(){

        android.support.v7.widget.Toolbar toolbarHome = findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbarHome);

        //Asetetaan icon erikseen
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_logo_round);

        //Asetetaan title erikseen jotta saadaan teemaan sopiva väri
        String strTitle = getString(R.string.app_name);
        SpannableString spanstrTitle = new SpannableString(strTitle);
        spanstrTitle.setSpan(new ForegroundColorSpan(getColor(R.color.colorWhite)), 0, strTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(spanstrTitle);

        homeQueue = Volley.newRequestQueue(this);

        tvHomeCount = (TextView) findViewById(R.id.tvHomeCount);
        if (eventCount > 0) {
            tvHomeCount.setText(Integer.toString(eventCount));
        }

        getCount();

    }

    private void getCount() {

        if (RUN_ONCE) {

            RUN_ONCE = false;

            String url = "https://api.hel.fi/linkedevents/v1/event/";

            final JsonObjectRequest dataRequest = new JsonObjectRequest(JsonObjectRequest.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject responseObject) {

                    try {
                        JSONObject jsonObject = responseObject.getJSONObject("meta");
                        eventCount = jsonObject.getInt("count");


                        Thread threadCounter = new Thread() {
                            public void run() {
                                for (int i = 0; i < eventCount; i = i + 111) {
                                    try {
                                        final int a = i;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                tvHomeCount.setText("" + a);
                                            }
                                        });
                                        sleep(10);
                                    } catch (InterruptedException e) {
                                        Log.d("InterruptedException", e.getMessage());
                                    }
                                }
                            }
                        };

                        threadCounter.start();

                        tvHomeCount.setText(Integer.toString(eventCount));

                    } catch (JSONException e) {
                        Log.e("jsonError", e.getMessage());
                    }


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError volleyError) {

                    Log.e("volleyError", volleyError.toString());

                }
            });

            homeQueue.add(dataRequest);

        }


    }


}
