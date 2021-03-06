package duality.questmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import duality.questmanager.gcm.MessageGCMListener;
import duality.questmanager.intent.GetTasksServiceHelper;
import duality.questmanager.intent.GetTokenServiceHelper;
import duality.questmanager.rest.ResultListener;

/**
 * Created by anna on 17.04.16.
 */
public class SplashActivity extends AppCompatActivity implements ResultListener {

    public static final String PREFLINK = "QuestManagerSharedPreferences";
    public static final String ISLOGGEDIN = "isLogged";
    public static final String GOTTOKEN = "gotToken";

    private static final String TAG = "SplashActivity";

    private int mRequestId;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private ResultListener backgroundListener = new ResultListener() {
        @Override
        public void onSuccess(String result) {
            Intent intent = new Intent(getApplicationContext(), FragmentsActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        public void onFail(String result) {
            Intent intent = new Intent(getApplicationContext(), FragmentsActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      //  Preferences.updateTheme(this);
        Preferences.updateLocaleIfNeeded(this);
        checkPlayServices();
        //updateLocaleIfNeeded();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean isLogged = sharedPreferences.getBoolean(ISLOGGEDIN, false);

        if (isLogged) {
            boolean gotToken = sharedPreferences.getBoolean(GOTTOKEN, false);
            if (gotToken) {
                GetTasksServiceHelper.start(getApplicationContext(), backgroundListener, false);
                GetTasksServiceHelper.start(getApplicationContext(), backgroundListener, true);

//                Intent intent = new Intent(this, FragmentsActivity.class);
//                startActivity(intent);
//                finish();
            }
            else
            {
                mRequestId = GetTokenServiceHelper.start(this, this);
            }
        }
        else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onSuccess(String result) {
        GetTasksServiceHelper.start(getApplicationContext(), backgroundListener, false);
        GetTasksServiceHelper.start(getApplicationContext(), backgroundListener, true);
    }

    @Override
    public void onFail(String result) {
        Intent intent = new Intent(this, WaitConfirmActivity.class);
        startActivity(intent);
        finish();
    }
}