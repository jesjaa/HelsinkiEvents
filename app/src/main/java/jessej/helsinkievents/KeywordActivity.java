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

public class KeywordActivity extends AppCompatActivity {

    private ProgressBar pbKeywords;
    private ConstraintLayout clKeywords;
    private SearchView searchKeyword;
    private Button btnKeywordsSave;
    private Button btnKeywordsCancel;
    private ExpandableListView elvKeywords;
    private ExpandableListAdapter elvKeywordsAdapter;
    private List<Keyword> keywordList;
    private HashMap<String, List<Keyword>> hashmapKeywordList;
    private List<String> keywordTitles;
    private RequestQueue requestQueue;
    private String strUrl;
    private boolean boolResponse;
    private int intPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword);

        keywordInit();
    }

    private void keywordInit() {

        // Progressbar näkyviin
        pbKeywords = (ProgressBar) findViewById(R.id.pbKeywords);
        pbKeywords.setVisibility(View.VISIBLE);

        // Piilotetaan sisältö (vaikka sisältöä ei vielä olekaan) latauksen ajaksi
        clKeywords = (ConstraintLayout) findViewById(R.id.clKeywords);
        clKeywords.setVisibility(View.INVISIBLE);

        elvKeywords = (ExpandableListView) findViewById(R.id.elvKeywords);
        searchKeyword = (SearchView) findViewById(R.id.searchKeyword);

        // Muut muuttujat
        btnKeywordsSave = (Button) findViewById(R.id.btnKeywordsSave);
        btnKeywordsCancel = (Button) findViewById(R.id.btnKeywordsCancel);

        keywordList = new ArrayList<Keyword>();
        hashmapKeywordList = new HashMap<>();
        keywordTitles = new ArrayList<String>();

        requestQueue = Volley.newRequestQueue(this);

        // Ensimäinen url, tätä muunnellan jatkossa jotta saadaan kaikilta sivuilta tulos
        strUrl = "https://api.hel.fi/linkedevents/v1/keyword/?page_size=100";

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
                pbKeywords.setVisibility(View.INVISIBLE);
                clKeywords.setVisibility(View.VISIBLE);

                // Suoritetaan vielä ExpandableListViewin täyttäminen
                continueKeywordInit();

            }
        });

        requestQueue.add(dataRequest);
    }

    public void parseData(JSONObject responseObject) {
        if (responseObject != null) {
            // Parsetaan json data keyword luokkaan ja siitä listaan
            try {
                // Data on json objekti muodossa, tästä objektista saamme hakemalla json array dataa
                JSONArray jsonArrayData = responseObject.getJSONArray("data");
                for (int i = 0; i < jsonArrayData.length(); i++) {
                    JSONObject jsonObject = jsonArrayData.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    // Käyttöliittymämme on suomeksi, joten nimet haetaan suomeksi
                    String name = jsonObject.getJSONObject("name").getString("fi");
                    String category = name.substring(0, 1);
                    category = category.toUpperCase();

                    Keyword keyword = new Keyword(id, name, category);
                    keywordList.add(keyword);
                }

                // Seuraava url, ei merkitystä tässä ratkaisussa
                JSONObject jsonObjectMeta = responseObject.getJSONObject("meta");
                String nextUrl = jsonObjectMeta.getString("next");

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("jsonError", e.getMessage());
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

    private void continueKeywordInit() {
        // Avainsanan lisääminen HashMappiin kategorioittain
        // Käydään läpi koko keywordList (avainlista johon on lisätty esiintyvät avaimet)
        for (Keyword keyword : keywordList) {
            // Luodaan uusi avainlista kategorian perusteella
            List<Keyword> keywords = hashmapKeywordList.get(keyword.getCategory());

            // Jos avainlistaa ei löydy uudesta avainlistasta -> luodaan uusi lista
            if (keywords == null) {
                keywords = new ArrayList<>();

                // Lisätään HashMappiin nyt kategorian perusteella
                // Ensimmäinen parametri on uuden avainlistan otsikko/avain
                hashmapKeywordList.put(keyword.getCategory(), keywords);
            }

            // Avainlista jo luotu (aiemmin silmukassa) -> lisätään avain uuteen avainlistaan
            keywords.add(keyword);
        }

        // Tuodaan hashmapista "otsikot" listaan
        keywordTitles = new ArrayList<>(hashmapKeywordList.keySet());

        // Luodaan uusi lista joka on tarkoitettu otsikot -listan järjestelyyn
        keywordTitles = keywordTitles.subList(0, keywordTitles.size());

        // Järjestellään otsikot aakkosjärjestykseen
        Collections.sort(keywordTitles);


        Log.d("METODISSA", "täytetään kentät");

        // Täytetään aiemmin luotu ExpandableListView aakkosilla joiden lapsina ovat avaimet
        elvKeywordsAdapter = new KeywordExpandableListAdapter(this, keywordTitles, hashmapKeywordList);
        elvKeywords.setAdapter(elvKeywordsAdapter);

        searchKeyword.setQueryHint("Etsi avainsanoja");

        searchKeyword.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        btnKeywordsSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Luodaan palautettava lista
                List<Keyword> keywordList = new ArrayList<Keyword>();

                int selectedSize = 0;

                // Käydään hashmapin listat läpi yksitellen
                for (List<Keyword> kwkw : hashmapKeywordList.values()) {
                    // Käydään valitun listan kaikki objektit läpi
                    for (int i = 0; i < kwkw.size(); i++) {
                        // Jos listan valittu objekti on merkitty valituksi hakuehtoihin
                        if (kwkw.get(i).isSelected()){
                            // Lisätään avain palautettavaan listaan
                            keywordList.add(kwkw.get(i));
                            selectedSize++;
                        }
                    }
                }

                String text = "Valittuja avainsanoja: " + Integer.toString(selectedSize);
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

                // Palautetaan valitut avaimet omassa avaimet listassaan List<Keyword> lista
                // Tai oikeastaan tallennetaan staattiseen muuttujaan SearchActivityssä
                SearchActivity.listFilterByKeywords = keywordList;
                finish();
            }
        });

        // Peruuttaminen vain lopettaa aktiviteetin (mitään ei viedä takaisin tai tallenneta) ja
        // lista avaimista on tyhjä
        btnKeywordsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Metodi hakee alkuperäisestä listasta samanlaisuuksia käyttäjän syöttämän merkkijonon perusteella
    private void filterSearch(String s) {
        if (s.length() > 0) {
            List<Keyword> filteredKeywords = new ArrayList<Keyword>();

            String hakuParam = s.toLowerCase();

            for (int i = 0; i < keywordList.size(); i++) {
                // Ehtolause: jos objektin nimi -muuttuja pitää sisällään syötetyn merkkijonon
                // -> lisätään se uuteen listaan
                if (keywordList.get(i).getName().toLowerCase().contains(hakuParam)) {
                    filteredKeywords.add(keywordList.get(i));
                }
            }

            // Tässä tapahtuu taas muokkaaminen sopivaan muotoon expandable listviewiä varten
            HashMap<String, List<Keyword>> filteredHashmap = new HashMap<>();
            for (Keyword keyword : filteredKeywords) {
                List<Keyword> keywords = filteredHashmap.get(keyword.getCategory());
                if (keywords == null) {
                    keywords = new ArrayList<>();
                    filteredHashmap.put(keyword.getCategory(), keywords);
                }
                keywords.add(keyword);
            }

            List<String> filteredTitles = new ArrayList<>(filteredHashmap.keySet());
            filteredTitles = filteredTitles.subList(0, filteredTitles.size());
            Collections.sort(filteredTitles);

            ExpandableListAdapter filteredAdapter = new KeywordExpandableListAdapter(getApplicationContext(), filteredTitles, filteredHashmap);

            elvKeywords.setAdapter(filteredAdapter);
        } else {

            // Jos merkkijono on tyhjä tai perutaan haku laitetaan adapteriksi alkuperäinen adapteri
            // jossa on kaikki tieto tallessa
            elvKeywords.setAdapter(elvKeywordsAdapter);
        }

    }
}

