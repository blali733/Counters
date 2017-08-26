package pl.blali733.counters;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

/**
 * Activity responsible for defining starting (splash) screen.
 * @author blali733
 * @version 1.0
 * @since 0.2
 */
public class SplashActivity extends AppCompatActivity {

    /**
     * Method creating splash screen.
     * @param savedInstanceState State of application.
     * @since 1.0
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Splash","Splash screen generated");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
