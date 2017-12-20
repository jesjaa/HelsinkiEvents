package jessej.helsinkievents;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

public class FeatureActivity extends AppCompatActivity {

    private TextView tvFeaturesImplemented;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature);

        String strTitle = getString(R.string.activity_title_feature);
        SpannableString spanstrTitle = new SpannableString(strTitle);
        spanstrTitle.setSpan(new ForegroundColorSpan(getColor(R.color.colorWhite)), 0, strTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(spanstrTitle);

        String strText = "<p>------------<em>ID 1</em>------------ <strong>-tehty</strong><br />User must be able to search events based on event time, place and keyword. At least one keyword is mandatory.<br />Show the data ListView or in RecyclerView. Show at least 4 fields of event in list.<br /><br />&nbsp;- SearchActivityss&auml; asetetaan hakusanat, ajat, avainsanat ja paikat ja haetaan niiden perusteella resultaktiviteetiss&auml;</p>\n" +
                "<p><br />------------<em>ID 2</em>------------ <strong>-tehty</strong><br />User must be able to search for keywords and places for filtering events. User probably doesn&rsquo;t<br />know the places/keywords, that&rsquo;s why this feature is needed. User must be able to select<br />somehow place/keyword he/she wants to use when searching for events.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>&nbsp;- Keyword- ja Place -aktiviteeteiss&auml; voidaan hakea filtterit ja lis&auml;t&auml; ne hakuun \"parametreiksi\"</p>\n" +
                "<p><br />------------<em>ID 3</em>------------ <strong>-tehty</strong><br />All adapters used must be custom adapters</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>&nbsp;- Kaikki applikaatiossa k&auml;ytetyt adapterit on tehty k&auml;sin</p>\n" +
                "<p><br />------------<em>ID 4</em>------------ <strong>-tehty</strong><br />When user selects the event, he/she will be shown detailed information about the event.<br />Show only textual fields (that seems to contain sensible information)</p>\n" +
                "<p><br />&nbsp;- K&auml;ytt&auml;j&auml; p&auml;&auml;see k&auml;siksi tapahtuman tarkempiin tietoihin napilla</p>\n" +
                "<p><br />------------<em>ID 5</em>------------ <strong>-tehty</strong><br />Show somehow location information of the event in the map</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>&nbsp;- Tapahtuman paikkatiedot n&auml;kyy Google Maps kartassa DetailActivityn alareunassa</p>\n" +
                "<p><br />------------<em>ID 6</em>------------ <strong>-tehty</strong><br />Show somehow all images of the event.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>&nbsp;- Kuvia voi selata jos tapahtuman luoja on asettanut sille kuvia<br /> Painamalla DetailActivityss&auml; yl&auml;reunassa esiintyv&auml;&auml; kuvaa p&auml;&auml;see selaamaan sen tapahtuman galleriaa (GalleryActivityss&auml;)</p>\n" +
                "<p><br />------------<em>ID 7</em>------------ <strong>-osittain tehty </strong><br />User can browse ALL events. Note that there will be more than 30000 events so it is not possible to show them all. You must use RecyclerView.</p>\n" +
                "<p><br />&nbsp;- ei tehty tai oikeastaan osittain tehty koska k&auml;ytin ratkaisussa pelkk&auml;&auml; constrainlayoutia ja listviewi&auml;</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>&nbsp;- K&auml;ytt&auml;j&auml; voi selata kaikkia tapahtumia hakemalla tapahtumia ilman hakusanoja ja hakuehtoja<br /> detailactivityss&auml; selaamalla viimeiseen ruudulla n&auml;kyv&auml;&auml;n tapahtumaan asti, sen j&auml;lkeen se alkaa lataamaan lis&auml;&auml; tapahtumia ja niin se tekee kunnes tapahtumat loppuu tietol&auml;hteest&auml;</p>\n" +
                "<p><br />------------<em>ID 8</em>------------ <strong>-tehty</strong><br />Application must show splash screen when the application starts. In that screen show your name for few seconds.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>&nbsp;- Applikaatio alkaa 2,5 sekunnin pituisella avausn&auml;yt&ouml;ll&auml; jossa n&auml;kyy applikaation nimi ja tekij&auml;</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>------------<em>ID 9</em>------------ <strong>-tehty</strong><br />Application must contain a view which describes the implemented features</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>&nbsp;- T&auml;t&auml; samaa teksti&auml; p&auml;&auml;see selaamaan tiedostoista ja applikaation app barista alkun&auml;yt&ouml;ss&auml;<br /> (oikealla kolme pistett&auml; ja valitse \"Lista toiminnoista\")</p>";
        
        tvFeaturesImplemented = (TextView) findViewById(R.id.tvFeatureImplemented);

        tvFeaturesImplemented.setText(Html.fromHtml(strText, Html.FROM_HTML_MODE_COMPACT));

    }

}
