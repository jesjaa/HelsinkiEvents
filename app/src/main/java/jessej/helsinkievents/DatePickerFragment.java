package jessej.helsinkievents;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Jesse on 29.11.2017.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Asetetaan nykyinen päivä ns. placeholderiksi valitsijaan
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Ei lupaa perua back napilla
        setCancelable(false);

        // Luodaan uusi instanssi päivävalintadialogista ja palautetaan se
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Tee jotain päivämäärällä kun painetaan set

        // Koska parametri heldevin apissa pitää olla muotoa yyyy-mm-dd
        // muokkaamme kuukausi- ja päivämuuttujaa niin että ne ovat aina kaksinumeroisia
        String strYear = Integer.toString(year);
        String strMonth = String.format("%02d", (month + 1)); // Kuukaudet on nollasta alkaen, miksi? siis 0 - 11
        String strDay = String.format("%02d", day);

        // Kasataan päivämäärämuuttuja
        String strDate = strYear + "-" + strMonth + "-" + strDay;

        // Käytetään globaalia muuttujaa dialokien käsittelyssä
        if (SearchActivity.DIALOG_ID == 101){
            SearchActivity.strDateStart = strDate;
            TextView tv = (TextView) getActivity().findViewById(R.id.tvDateStart);
            tv.setText(strDate);
        } else if (SearchActivity.DIALOG_ID == 102) {
            SearchActivity.strDateEnd = strDate;
            TextView tv = (TextView) getActivity().findViewById(R.id.tvDateEnd);
            tv.setText(strDate);
        } else {
            // error
        }
    }

    @Override
    public void onCancel(DialogInterface view){
        // Painettaessa "cancel" -nappia asetetaan kyseinen filtteri pois päältä
        if (SearchActivity.DIALOG_ID == 101){
            Switch sw = (Switch) getActivity().findViewById(R.id.swDateStart);
            sw.setChecked(false);
        } else if (SearchActivity.DIALOG_ID == 102) {
            Switch sw = (Switch) getActivity().findViewById(R.id.swDateEnd);
            sw.setChecked(false);
        }
    }

}

