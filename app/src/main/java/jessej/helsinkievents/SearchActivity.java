package jessej.helsinkievents;

import android.app.DialogFragment;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText etSearchword;
    private Switch swFilter;
    private ConstraintLayout clFilter;
    private Switch swDateStart;
    private Switch swDateEnd;
    private TextView tvDateStart;
    private TextView tvDateEnd;
    public static String strDateStart;
    public static String strDateEnd;
    public static int DIALOG_ID;
    private Switch swKeywordsSet;
    private Button btnKeywordsSet;
    public static List<Keyword> listFilterByKeywords;
    public static List<Place> listFilterByPlaces;
    private Switch swPlacesSet;
    private Button btnPlacesSet;
    private Button btnShowResults;
    private Button btnCancel;

    private boolean boolUseFilters;
    private boolean boolUseDateStart;
    private boolean boolUseDateEnd;
    private boolean boolUseKeywords;
    private boolean boolUsePlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchInit();
    }

    private void searchInit() {
        //Asetetaan eri niminen title
        String strTitle = getString(R.string.activity_title_search);
        SpannableString spanstrTitle = new SpannableString(strTitle);
        spanstrTitle.setSpan(new ForegroundColorSpan(getColor(R.color.colorWhite)), 0, strTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(spanstrTitle);

        // Määritetään muuttujille aktiviteetin komponentit
        etSearchword = (EditText) findViewById(R.id.etSearchword);
        swFilter = (Switch) findViewById(R.id.swFilter);
        clFilter = (ConstraintLayout) findViewById(R.id.clFilter);
        btnShowResults = (Button) findViewById(R.id.btnShowResults);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        // Rajauslaatikon sisällön komponentit
        swDateStart = (Switch) findViewById(R.id.swDateStart);
        swDateEnd = (Switch) findViewById(R.id.swDateEnd);
        tvDateStart = (TextView) findViewById(R.id.tvDateStart);
        tvDateEnd = (TextView) findViewById(R.id.tvDateEnd);
        swKeywordsSet = (Switch) findViewById(R.id.swKeywordsSet);
        btnKeywordsSet = (Button) findViewById(R.id.btnKeywordsSet);
        swPlacesSet = (Switch) findViewById(R.id.swPlacesSet);
        btnPlacesSet = (Button) findViewById(R.id.btnPlacesSet);

        // Määritetään muut muuttujat...
        boolUseFilters = false;
        boolUseDateStart = false;
        boolUseDateEnd = false;
        boolUseKeywords = false;
        boolUsePlaces = false;

        // Asetetaan rajausvalinnat näkymättömiksi
        clFilter.setVisibility(View.INVISIBLE);

        // Määritetään painikkeet hakemiseksi tai peruuttamiseksi
        btnShowResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO uusi intent, uudet kujeet
                // Kasataan haku lopullinen haku url täällä ja viedään se merkkijonona (extrana) tulos -aktiviteettiin

                String url = "https://api.hel.fi/linkedevents/v1/event/?include=location";

                if (etSearchword.getText().length() > 0) {
                    String text = "&text=" + etSearchword.getText().toString();

                    // Korvataan kaikki välilyönnit apin haluamalla tavalla
                    //text.replaceAll("\\s","%20");
                    // ei tarvikaan API hoitaa sen

                    // Lisätään urliin parametri arvoineen
                    url = url + text;
                }

                // Jos filttereitä käytetään
                if (boolUseFilters) {

                    // Jos käyttäjä määrittänyt alkupäivämäärän
                    if (boolUseDateStart) {
                        String start = "&start=" + strDateStart;
                        url = url + start;
                    }

                    // Jos käyttäjä määrittänyt loppupäivämäärän
                    if (boolUseDateEnd) {
                        String end = "&end=" + strDateEnd;
                        url = url + end;
                    }

                    // Jos käyttäjä määrittänyt avainsanat
                    if (boolUseKeywords) {

                        // Asetetaan merkkijono ensin alkamaan parametrillä
                        String keyword = "&keyword=";

                        // Asetetaan ns. erotin jota käytetään Hel Devin apissa erottamaan arvot (jos monta)
                        String separator = "";

                        // Koska erotinta ei tungeta vielä parametrin ja arvon väliin
                        // -> on sen oltava tyhjä merkkijono

                        // Käydään läpi kaikki asetetut avainsanat ja lisätään merkkijonoon -> haku-urliin
                        for (Keyword kw : listFilterByKeywords) {
                            keyword = keyword + separator + kw.getId();
                            // Jatkossa käytämme pilkkua erottimena
                            separator = ",";
                        }

                        url = url + keyword;

                    }

                    // Jos käyttäjä määrittänyt paikatrajaukset
                    if (boolUsePlaces) {
                        // To restrict the retrieved events to a known location(s), use the query
                        // parameter location, separating values by commas if you wish to query for several locations.

                        // Käytämme siis Place -objektien id muuttujaa jonka haimme aikaisemmin
                        String location = "&location=";

                        String separator = "";

                        for (Place pl : listFilterByPlaces) {
                            location = location + separator + pl.getId();
                            separator = ",";
                        }

                        url = url + location;
                    }
                } // filter jos -lause loppuu

                Log.d("URL", url);

                Intent intentResult = new Intent(getApplicationContext(), ResultActivity.class);
                intentResult.putExtra("request_url", url);
                startActivity(intentResult);

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Näytetään rajausvalinnat käyttäjän tahdon mukaan
        swFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    clFilter.setVisibility(View.VISIBLE);
                    boolUseFilters = true;
                } else {
                    clFilter.setVisibility(View.INVISIBLE);
                    boolUseFilters = false;
                }
            }
        });

        // Asetetaan aloituspäivä ja avataan dialogi
        swDateStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    // Avaa dialogi ja käytä haussa alkupäivämäärä -ehtoa
                    DIALOG_ID = 101;
                    showDatePickerDialog();
                    boolUseDateStart = true;
                } else {
                    strDateStart = "";
                    tvDateStart.setText("");
                    boolUseDateStart = false;
                }
            }
        });

        // Asetetaan päättymispäivä -rajaus ja avataan dialogi
        swDateEnd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    // Avaa dialogi ja käytä haussa loppupäivämäärä -ehtoa
                    DIALOG_ID = 102;
                    showDatePickerDialog();
                    boolUseDateEnd = true;
                } else {
                    strDateEnd = "";
                    tvDateEnd.setText("");
                    boolUseDateEnd = false;
                }
            }
        });

        // Asetetaan halutaanko käyttää avainsanoja haussa
        swKeywordsSet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    // Käytä avainsanalistaa hakuehtoina
                    boolUseKeywords = true;
                } else {
                    // Tyhjennä avainsanalista äläkä käytä hakuehtona
                    listFilterByKeywords = new ArrayList<Keyword>();
                    boolUseKeywords = false;
                }
            }
        });

        // Täällä button vie aktiviteettiin missä määrittelemme haluamamme avainsanat hakua varten
        btnKeywordsSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Jos käyttäjä on ilmoittanut haluavansa käyttää avainlistaa rajauksessa -> avaa Keyword aktiviteetti
                if (swKeywordsSet.isChecked()) {
                    Intent intentKeyword = new Intent(view.getContext(), KeywordActivity.class);
                    startActivity(intentKeyword);
                } else {
                    // Jos käyttäjä kuitenkin painaa "valitse avainsana" -näppäintä vaikka kytkin olisi "pois" -asennossa
                    // asetetaan se silloin päälle ja viedään käyttäjä Keyword -aktiviteettiin
                    swKeywordsSet.setChecked(true);
                    Intent intentKeyword = new Intent(view.getContext(), KeywordActivity.class);
                    startActivity(intentKeyword);
                }
            }
        });

        // Asetetaan haluammeko käyttää haussa paikkamääritelmiä
        swPlacesSet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    // Käytä aluerajausta
                    boolUsePlaces = true;
                } else {
                    // Älä käytä aluerajausta
                    listFilterByPlaces = new ArrayList<Place>();
                    boolUsePlaces = false;
                }
            }
        });

        // Täällä käyttäjä viedään paikkamääritelmien valintaan
        btnPlacesSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Jos käyttäjä on ilmoittanut haluavansa käyttää aluelistaa rajauksena avaa Place aktiviteetti
                if (swPlacesSet.isChecked()) {
                    // Avaa Place -aktiviteetti
                    Intent intentPlace = new Intent(view.getContext(), PlaceActivity.class);
                    startActivity(intentPlace);
                } else {
                    // Avaa Place -aktiviteetti silti ja aseta sen kytkin päälle
                    swPlacesSet.setChecked(true);
                    Intent intentPlace = new Intent(view.getContext(), PlaceActivity.class);
                    startActivity(intentPlace);
                }
            }
        });
    }

    private void showDatePickerDialog() {
        DialogFragment datepickerFragment = new DatePickerFragment();
        datepickerFragment.setCancelable(false);
        datepickerFragment.show(getFragmentManager(), "DateSet");
    }

}
