package jessej.helsinkievents;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private ProgressBar pbResults;
    private ListView lvResults;
    private ProgressBar pbResultsLoadMore;

    public static List<Event> eventList;
    private RequestQueue eventQueue;
    private int pageNumber;
    private String nextPage;
    private ResultAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        resultInit();
    }

    private void resultInit() {

        //Asetetaan eri niminen title
        String strTitle = getString(R.string.activity_title_search);
        SpannableString spanstrTitle = new SpannableString(strTitle);
        spanstrTitle.setSpan(new ForegroundColorSpan(getColor(R.color.colorWhite)), 0, strTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(spanstrTitle);

        // Määritellään muuttujat näytettäville komponenteille
        pbResults = (ProgressBar) findViewById(R.id.pbResults);
        pbResults.setVisibility(View.VISIBLE);
        lvResults = (ListView) findViewById(R.id.lvResults);
        lvResults.setVisibility(View.INVISIBLE);
        pbResultsLoadMore = (ProgressBar) findViewById(R.id.pbResultsLoadMore);
        pbResultsLoadMore.setVisibility(View.INVISIBLE);

        // Määritetään tarvittavat muuttujat
        eventList = new ArrayList<Event>();
        eventQueue = Volley.newRequestQueue(this);
        pageNumber = 2;

        // Haetaan extrana tuotu url
        String url = "";
        Intent thisIntent = getIntent();
        Bundle bundle = thisIntent.getExtras();
        // Jos extra löytyy (aktiviteettiin päästy oikein tarkoituksin)
        if (bundle != null) {
            url = (String) bundle.get("request_url");

            // Metodi hakee ja parsee datan
            getData(url);

        } else {
            // Muuten aktiviteetti suljetaan
            //finish();
        }


    }

    public void getData(String url) {

        List<Event> tempList = new ArrayList<Event>();

        Log.d("request", url);

        final JsonObjectRequest dataRequest = new JsonObjectRequest(JsonObjectRequest.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject responseObject) {

                // Täällä teemme json datan parsemisen, sitä samaa kuin aiemminkin..
                try {
                    JSONArray jsonArrayData = responseObject.getJSONArray("data");
                    JSONObject jsonObjectMeta = responseObject.getJSONObject("meta");
                    if (!jsonObjectMeta.isNull("next")) {
                        // Seuraava sivu jos se halutaan hakea..
                        if (!jsonObjectMeta.isNull("next")) {
                            nextPage = jsonObjectMeta.getString("next");
                        } else {
                            nextPage = null;
                        }

                    }

                    for (int i = 0; i < jsonArrayData.length(); i++) {
                        JSONObject jsonObjectEvent = jsonArrayData.getJSONObject(i);

                        String id = "";
                        String location_id = "";
                        String name = "";
                        String info_url = "";
                        String short_description = "";
                        String description = "";
                        String start_time = "";
                        String end_time = "";

                        double longitude = 0;
                        double latitude = 0;

                        // Suoritamme erittäin kivuliaan tavan tarkistaa onko jsonobjektin (eventin)
                        // jokin olennainen tieto null
                        if (!jsonObjectEvent.isNull("id")) {
                            id = jsonObjectEvent.getString("id");
                        }

                        if (!jsonObjectEvent.isNull("location")) {
                            location_id = jsonObjectEvent.getJSONObject("location").getString("@id");
                        }

                        if (!jsonObjectEvent.isNull("name")) {
                            if (!jsonObjectEvent.getJSONObject("name").isNull("fi")) {
                                name = jsonObjectEvent.getJSONObject("name").getString("fi");
                            }
                        } else {
                            name = "Ei nimeä";
                        }

                        JSONArray jsonArrayImages = jsonObjectEvent.getJSONArray("images");
                        ArrayList<String> image_urls = new ArrayList<String>();
                        if (jsonArrayImages != null) {
                            String tempUrl = "";
                            for (int iImg = 0; iImg < jsonArrayImages.length(); iImg++) {
                                tempUrl = jsonArrayImages.getJSONObject(iImg).getString("url");
                                image_urls.add(tempUrl);
                                //Log.d("image url", tempUrl);
                            }
                        }

                        if (!jsonObjectEvent.isNull("info_url")) {
                            if (!jsonObjectEvent.getJSONObject("info_url").isNull("fi")) {
                                info_url = jsonObjectEvent.getJSONObject("info_url").getString("fi");
                            }
                        }

                        if (!jsonObjectEvent.isNull("short_description")) {
                            if (!jsonObjectEvent.getJSONObject("short_description").isNull("fi")) {
                                short_description = jsonObjectEvent.getJSONObject("short_description").getString("fi");
                            }
                        }

                        if (!jsonObjectEvent.isNull("description")) {
                            if (!jsonObjectEvent.getJSONObject("description").isNull("fi")) {
                                description = jsonObjectEvent.getJSONObject("description").getString("fi");
                            }
                        }

                        if (!jsonObjectEvent.isNull("start_time")) {
                            start_time = jsonObjectEvent.getString("start_time");
                        }

                        if (!jsonObjectEvent.isNull("end_time")) {
                            end_time = jsonObjectEvent.getString("end_time");
                        }

                        if (!jsonObjectEvent.isNull("location")) {
                            if (!jsonObjectEvent.getJSONObject("location").isNull("position")) {
                                if (!jsonObjectEvent.getJSONObject("location").getJSONObject("position").isNull("coordinates")) {
                                    longitude = jsonObjectEvent.getJSONObject("location").getJSONObject("position").getJSONArray("coordinates").getDouble(0);
                                    latitude = jsonObjectEvent.getJSONObject("location").getJSONObject("position").getJSONArray("coordinates").getDouble(1);
                                }
                            }
                        }


                        Event event = new Event(id, location_id, name, image_urls, info_url, short_description, description, start_time, end_time, latitude, longitude);
                        eventList.add(event);


                    }

                    // Täytetään lista
                    // populateListView(eventList);
                    adapter = new ResultAdapter(eventList, getApplicationContext());
                    lvResults.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("jsonError", e.getMessage());
                }

                // ProgressBar pois näkyvistä ja data näytölle
                pbResults.setVisibility(View.INVISIBLE);
                lvResults.setVisibility(View.VISIBLE);


                // Jos vastaus tuli niin tehdään mahdolliseksi hakea lisää dataa listaan
                lvResults.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int i) {

                    }

                    @Override
                    public void onScroll(AbsListView absListView, int firstItemOnSight, int itemCountOnSight, int totalItemCount) {
                        final int lastItem = firstItemOnSight + itemCountOnSight;
                        if (lastItem == totalItemCount) {
                            if (nextPage != null) {
                                // Suorita metodi
                                addData(nextPage);
                            }
                        }
                    }
                });

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // ...
            }
        });

        eventQueue.add(dataRequest);
    }


    private void populateListView(List<Event> list) {
        ResultAdapter adapter = new ResultAdapter(list, getApplicationContext());
        lvResults.setAdapter(adapter);
    }

    private void addData(String url) {

        pbResultsLoadMore.setVisibility(View.VISIBLE);

        List<Event> tempListAdd = new ArrayList<Event>();

        final JsonObjectRequest dataRequestAdd = new JsonObjectRequest(JsonObjectRequest.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject responseObject) {

                // Täällä teemme json datan parsemisen
                try {
                    JSONArray jsonArrayData = responseObject.getJSONArray("data");
                    JSONObject jsonObjectMeta = responseObject.getJSONObject("meta");
                    if (!jsonObjectMeta.isNull("next")) {
                        // Seuraava sivu jos se halutaan hakea..
                        if (!jsonObjectMeta.isNull("next")) {
                            nextPage = jsonObjectMeta.getString("next");
                        } else {
                            nextPage = null;
                        }

                    }

                    for (int i = 0; i < jsonArrayData.length(); i++) {
                        JSONObject jsonObjectEvent = jsonArrayData.getJSONObject(i);

                        String id = "";
                        String location_id = "";
                        String name = "";
                        String info_url = "";
                        String short_description = "";
                        String description = "";
                        String start_time = "";
                        String end_time = "";

                        double longitude = 0;
                        double latitude = 0;

                        // Suoritamme erittäin kivuliaan tavan tarkistaa onko jsonobjektin (eventin)
                        // jokin olennainen tieto null
                        if (!jsonObjectEvent.isNull("id")) {
                            id = jsonObjectEvent.getString("id");
                        }

                        if (!jsonObjectEvent.isNull("location")) {
                            location_id = jsonObjectEvent.getJSONObject("location").getString("@id");
                        }

                        if (!jsonObjectEvent.isNull("name")) {
                            if (!jsonObjectEvent.getJSONObject("name").isNull("fi")) {
                                name = jsonObjectEvent.getJSONObject("name").getString("fi");
                            }
                        } else {
                            name = "Ei nimeä";
                        }

                        JSONArray jsonArrayImages = jsonObjectEvent.getJSONArray("images");
                        ArrayList<String> image_urls = new ArrayList<String>();
                        if (jsonArrayImages != null) {
                            String tempUrl = "";
                            for (int iImg = 0; iImg < jsonArrayImages.length(); iImg++) {
                                tempUrl = jsonArrayImages.getJSONObject(iImg).getString("url");
                                image_urls.add(tempUrl);
                                //Log.d("image url", tempUrl);
                            }
                        }

                        if (!jsonObjectEvent.isNull("info_url")) {
                            if (!jsonObjectEvent.getJSONObject("info_url").isNull("fi")) {
                                info_url = jsonObjectEvent.getJSONObject("info_url").getString("fi");
                            }
                        }

                        if (!jsonObjectEvent.isNull("short_description")) {
                            if (!jsonObjectEvent.getJSONObject("short_description").isNull("fi")) {
                                short_description = jsonObjectEvent.getJSONObject("short_description").getString("fi");
                            }
                        }

                        if (!jsonObjectEvent.isNull("description")) {
                            if (!jsonObjectEvent.getJSONObject("description").isNull("fi")) {
                                description = jsonObjectEvent.getJSONObject("description").getString("fi");
                            }
                        }

                        if (!jsonObjectEvent.isNull("start_time")) {
                            start_time = jsonObjectEvent.getString("start_time");
                        }

                        if (!jsonObjectEvent.isNull("end_time")) {
                            end_time = jsonObjectEvent.getString("end_time");
                        }

                        if (!jsonObjectEvent.isNull("location")) {
                            if (!jsonObjectEvent.getJSONObject("location").isNull("position")) {
                                if (!jsonObjectEvent.getJSONObject("location").getJSONObject("position").isNull("coordinates")) {
                                    longitude = jsonObjectEvent.getJSONObject("location").getJSONObject("position").getJSONArray("coordinates").getDouble(0);
                                    latitude = jsonObjectEvent.getJSONObject("location").getJSONObject("position").getJSONArray("coordinates").getDouble(1);
                                }
                            }
                        }


                        Event event = new Event(id, location_id, name, image_urls, info_url, short_description, description, start_time, end_time, latitude, longitude);
                        eventList.add(event);

                    }


                    // Täydennetään listview
                    adapter.notifyDataSetChanged();

                    // Ja "hävitetään" progressbar
                    pbResultsLoadMore.setVisibility(View.INVISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("jsonError", e.getMessage());
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // ...
            }
        });

        eventQueue.add(dataRequestAdd);
    }

}
