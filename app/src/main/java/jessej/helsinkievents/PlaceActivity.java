package jessej.helsinkievents;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PlaceActivity extends AppCompatActivity {

    private ProgressBar pbPlaces;
    private ConstraintLayout clPlaces;
    private SearchView searchPlace;
    private Button btnPlacesSave;
    private Button btnPlacesCancel;
    private ExpandableListView elvPlaces;
    private ExpandableListAdapter elvPlacesAdapter;
    private List<Place> placeList;
    private HashMap<String, List<Place>> hashmapPlaceList;
    private List<String> placeTitles;
    private RequestQueue placeRequestQueue;
    private String strUrl;
    private boolean boolResponse;
    private int intPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        placeInit();
    }

    private void placeInit() {

        // Progressbar näkyviin
        pbPlaces = (ProgressBar) findViewById(R.id.pbPlaces);
        pbPlaces.setVisibility(View.VISIBLE);

        // Piilotetaan sisältö (vaikka sisältöä ei vielä olekaan) latauksen ajaksi
        clPlaces = (ConstraintLayout) findViewById(R.id.clPlaces);
        clPlaces.setVisibility(View.INVISIBLE);

        elvPlaces = (ExpandableListView) findViewById(R.id.elvPlaces);
        searchPlace = (SearchView) findViewById(R.id.searchPlace);

        // Muut muuttujat
        btnPlacesSave = (Button) findViewById(R.id.btnPlacesSave);
        btnPlacesCancel = (Button) findViewById(R.id.btnPlacesCancel);

        placeList = new ArrayList<Place>();
        hashmapPlaceList = new HashMap<>();
        placeTitles = new ArrayList<String>();

        placeRequestQueue = Volley.newRequestQueue(this);

        // Ensimäinen url, tätä muunnellan jatkossa jotta saadaan kaikilta sivuilta tulos
        strUrl = "https://api.hel.fi/linkedevents/v1/place/?page_size=100";

        // Muuttujia joita tarvitaan myöhemmin
        boolResponse = false;
        intPage = 1;

        // getData -metodi suorittaa ensimäisen json -latauksen, jonka jälkeen se suorittaa toisen latauksen jos sivu löytyy
        getData(intPage);
    }

    public void getData(int pageNumber) {

        String url = strUrl + "&page=" + Integer.toString(pageNumber);

        Log.d("request", url);

        final JsonObjectRequest dataRequest = new JsonObjectRequest(JsonObjectRequest.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject responseObject) {

                // Niin kauan kuin dataa on suoritetaan onResponse ja metodi parseData joka
                // muokkaa datan sopivaan muotoon
                parseData(responseObject);


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Log.e("volleyError", volleyError.toString());

                // Volleyn RequestQueue -kirjastossa ei ole mitään interfacea tai callbackeja
                // joiden avulla voidaan selvittää onko kaikki requestit suoritettu loppuun
                // Koska tarvii ajaa vielä viimeinen metodi joka täyttää expansedListViewin
                // on se metodi ajettava täällä "onErrorResponsessa"
                pbPlaces.setVisibility(View.INVISIBLE);
                clPlaces.setVisibility(View.VISIBLE);

                // Suoritetaan vielä ExpandableListViewin täyttäminen
                continuePlaceInit();

            }
        });

        placeRequestQueue.add(dataRequest);
    }

    public void parseData(JSONObject responseObject) {
        if (responseObject != null) {
            // Parsetaan json data place luokkaan ja siitä listaan

            try {
                // Data on json objekti muodossa, tästä objektista saamme hakemalla json array dataa

                JSONArray jsonArrayData = responseObject.getJSONArray("data");
                for (int i = 0; i < jsonArrayData.length(); i++) {
                    JSONObject jsonObject = jsonArrayData.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    String name = jsonObject.getJSONObject("name").getString("fi");
                    String postal_code = jsonObject.getString("postal_code");
                    String street_address = jsonObject.getJSONObject("street_address").getString("fi");
                    String address_locality = jsonObject.getJSONObject("address_locality").getString("fi");

                    // Tässä kohtaa haetaan location objekti ja siitä latitude, longitude
                    JSONObject jsonObjectPosition = jsonObject.getJSONObject("position");
                    JSONArray jsonArrayCoordinates = jsonObjectPosition.getJSONArray("coordinates");
                    String longitude = jsonArrayCoordinates.get(0).toString();
                    String latitude = jsonArrayCoordinates.get(1).toString();

                    String category = name.substring(0, 1);
                    category = category.toUpperCase();

                    Place place = new Place(id, name, postal_code, street_address, address_locality, latitude, longitude, category);
                    placeList.add(place);
                }

                // Seuraava url, ei merkitystä tässä ratkaisussa
                JSONObject jsonObjectMeta = responseObject.getJSONObject("meta");
                String nextUrl = jsonObjectMeta.getString("next");

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("jsonError", e.getMessage());
            }

            // Jos löytyy response -> tehdään uusi haku
            if (!boolResponse) {
                Log.d("response", "making new request");
                boolResponse = false;
                intPage = intPage + 1;
                getData(intPage);
            } else {
                boolResponse = true;
            }
        }
    }

    private void continuePlaceInit() {
        // Avainsanan lisääminen HashMappiin kategorioittain
        // Käydään läpi koko placeList (avainlista johon on lisätty esiintyvät avaimet)

        for (Place place : placeList) {
            // Luodaan uusi avainlista kategorian perusteella
            List<Place> places = hashmapPlaceList.get(place.getCategory());

            // Jos avainlistaa ei löydy uudesta avainlistasta -> luodaan uusi lista
            if (places == null) {
                places = new ArrayList<>();

                // Lisätään HashMappiin nyt kategorian perusteella
                // Ensimmäinen parametri on uuden avainlistan otsikko/avain
                hashmapPlaceList.put(place.getCategory(), places);
            }

            // Avainlista jo luotu (aiemmin silmukassa) -> lisätään avain uuteen avainlistaan
            places.add(place);
        }

        // Tuodaan hashmapista "otsikot" listaan
        placeTitles = new ArrayList<>(hashmapPlaceList.keySet());

        // Luodaan uusi lista joka on tarkoitettu otsikot -listan järjestelyyn
        placeTitles = placeTitles.subList(0, placeTitles.size());

        // Järjestellään otsikot aakkosjärjestykseen
        Collections.sort(placeTitles);


        Log.d("methdo", "populating fields");

        // Täytetään aiemmin luotu ExpandableListView aakkosilla joiden lapsina ovat avaimet
        elvPlacesAdapter = new PlaceExpandableListAdapter(this, placeTitles, hashmapPlaceList);
        elvPlaces.setAdapter(elvPlacesAdapter);

        searchPlace.setQueryHint("Etsi paikkoja tai osoitteita");

        searchPlace.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // Turha filteröinti mutta filteröidään nyt silti
                filterSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterSearch(s);
                return false;
            }
        });

        // Tallenna avainsanat -napilla käydään läpi hashmap ja etsitään valitut objektit
        btnPlacesSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Luodaan palautettava lista
                List<Place> placeList = new ArrayList<Place>();

                int selectedSize = 0;

                // Käydään hashmapin listat läpi yksitellen
                for (List<Place> plpl : hashmapPlaceList.values()) {
                    // Käydään valitun listan kaikki objektit läpi
                    for (int i = 0; i < plpl.size(); i++) {
                        // Jos listan valittu objekti on merkitty valituksi hakuehtoihin
                        if (plpl.get(i).isSelected()){
                            // Lisätään avain palautettavaan listaan
                            placeList.add(plpl.get(i));
                            selectedSize++;
                        }
                    }
                }

                String text = "Valittuja paikkoja: " + Integer.toString(selectedSize);
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

                // Palautetaan valitut avaimet omassa avaimet listassaan List<Place> lista
                // Tai oikeastaan tallennetaan staattiseen muuttujaan SearchActivityssä
                SearchActivity.listFilterByPlaces = placeList;
                finish();
            }
        });

        // Peruuttaminen vain lopettaa aktiviteetin (mitään ei viedä takaisin tai tallenneta) ja
        // lista avaimista on tyhjä
        btnPlacesCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Metodi hakee alkuperäisestä listasta samanlaisuuksia käyttäjän syöttämän merkkijonon perusteella
    private void filterSearch(String s) {
        if (s.length() > 0) {
            List<Place> filteredPlaces = new ArrayList<Place>();

            String hakuParam = s.toLowerCase();

            for (int i = 0; i < placeList.size(); i++) {
                // Ehtolause: jos objektin nimi -muuttuja pitää sisällään syötetyn merkkijonon
                // -> lisätään se uuteen listaan
                if (placeList.get(i).getLongname().toLowerCase().contains(hakuParam)) {
                    filteredPlaces.add(placeList.get(i));
                }
            }

            // Tässä tapahtuu taas muokkaaminen sopivaan muotoon expandable listviewiä varten
            HashMap<String, List<Place>> filteredHashmap = new HashMap<>();
            for (Place place : filteredPlaces) {
                List<Place> places = filteredHashmap.get(place.getCategory());
                if (places == null) {
                    places = new ArrayList<>();
                    filteredHashmap.put(place.getCategory(), places);
                }
                places.add(place);
            }

            List<String> filteredTitles = new ArrayList<>(filteredHashmap.keySet());
            filteredTitles = filteredTitles.subList(0, filteredTitles.size());
            Collections.sort(filteredTitles);

            ExpandableListAdapter filteredAdapter = new PlaceExpandableListAdapter(getApplicationContext(), filteredTitles, filteredHashmap);

            elvPlaces.setAdapter(filteredAdapter);
        } else {

            // Jos merkkijono on tyhjä tai perutaan haku laitetaan adapteriksi alkuperäinen adapteri
            // jossa on kaikki tieto tallessa
            elvPlaces.setAdapter(elvPlacesAdapter);
        }

    }
}
