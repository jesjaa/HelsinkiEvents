package jessej.helsinkievents;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.drawable.splash_screen);

        init();
    }
    private void init() {

        //Käytetään säiettä tai SystemClock -luokkaa hidastaaksemme avausnäytön sulkeutumista pari sekuntia
        SystemClock.sleep(2500);

        //Avataan "koti"- Home -aktiviteetti
        Intent intentHome = new Intent(this, HomeActivity.class);
        startActivity(intentHome);

        //Suljetaan SplashActivity
        finish();
    }
}
