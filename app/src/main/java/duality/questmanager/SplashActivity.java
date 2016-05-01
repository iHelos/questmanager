package duality.questmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by olegermakov on 17.04.16.
 */
public class SplashActivity extends AppCompatActivity {

    public static final String PREFLINK = "QuestManagerSharedPreferences";
    public static final String ISLOGGEDIN = "isLogged";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean isLogged = sharedPreferences.getBoolean(ISLOGGEDIN, false);

//        GCMRegistrar.checkDevice(this);
//        GCMRegistrar.checkManifest(this);
//        final String regId = GCMRegistrar.getRegistrationId(this);
//        if (regId.equals("")) {
//            GCMRegistrar.register(this, "483910217912");
//            Log.d(tag, "Registered");
//        }
//        else {
//            Log.v(tag, "Already registered");
//        }

        if (isLogged) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}