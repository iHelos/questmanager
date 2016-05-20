package duality.questmanager.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import duality.questmanager.R;
import duality.questmanager.SplashActivity;
import duality.questmanager.processor.RegisterProcessor;
import duality.questmanager.rest.RESTAnswer;

/**
 * Created by olegermakov on 18.04.16.
 */

public class RegistrationIntentService extends IntentService {

    public static final String REGISTER_EMAIL = "tokenToServerEmail";
    public static final String REGISTER_DEV_ID = "tokenToServerDevID" ;

    public static final  String REGISTRATION_COMPLETE = "registrationCompleted";
    public static final String SENT_TOKEN_TO_SERVER = "tokenToServer";

    private static final String TAG = "RegIntService";
    public static final String REGISTRATION_RESULT = "regResult";
    public static final String REGISTRATION_SUCCESS = "regSuccess";
    public static final String REGISTRATION_ERROR = "regError";


    public RegistrationIntentService() {
        super("RegistrationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            final String email = intent.getStringExtra(REGISTER_EMAIL);
            final String dev_id = intent.getStringExtra(REGISTER_DEV_ID);

            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, "GCM Registration Token: " + token);



            RESTAnswer result = RegisterProcessor.processSignUp(token, dev_id, email);
            String message = result.getMessage();
            if (message.equalsIgnoreCase("0"))
            {
                result.setMessage(getResources().getString(R.string.loginActivity_error_blank));
            }
            else if(message.equalsIgnoreCase("1"))
            {
                result.setMessage(getResources().getString(R.string.loginActivity_error_invalid));
            }
            else if(message.equalsIgnoreCase("2"))
            {
                result.setMessage(getResources().getString(R.string.loginActivity_error_required));
            }
            else
            {
                result.setMessage(getResources().getString(R.string.loginActivity_error_unknown));
            }
            if (result.getStatus() == 200)
            {
                sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply();
                sharedPreferences.edit().putBoolean(SplashActivity.ISLOGGEDIN, true).apply();
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(REGISTRATION_SUCCESS));
            }
            else {
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(REGISTRATION_ERROR).putExtra(REGISTRATION_RESULT, result.getMessage()));
            }

        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(REGISTRATION_ERROR).putExtra(REGISTRATION_RESULT, getResources().getString(R.string.networkError)));
        }

//        Intent registrationComplete = new Intent(REGISTRATION_COMPLETE);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

}