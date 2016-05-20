package duality.questmanager.intent;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import duality.questmanager.AuthToken;
import duality.questmanager.R;
import duality.questmanager.SplashActivity;
import duality.questmanager.processor.GetAuthTokenProcessor;
import duality.questmanager.rest.RESTAnswer;

/**
 * Created by olegermakov on 10.05.16.
 */
public class GetTokenService extends IntentService {
    public static final String GETTOKEN_SUCCESS = "gotToken_success";
    public static final String GETTOKEN_ERROR = "gotToken_error";
    public static final String GETTOKEN_RESULT = "gotToken_result";

    public GetTokenService() {
        super("GetTokenService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            RESTAnswer result = GetAuthTokenProcessor.getToken(token);
            if (result.getStatus() == 200)
            {
                sharedPreferences.edit().putBoolean(SplashActivity.GOTTOKEN, true).apply();
                AuthToken.setToken(result.getMessage(), sharedPreferences);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(GETTOKEN_SUCCESS).putExtra(GETTOKEN_RESULT, result.getMessage()));
            }
            else {
                sharedPreferences.edit().putBoolean(SplashActivity.GOTTOKEN, false).apply();
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(GETTOKEN_ERROR).putExtra(GETTOKEN_RESULT, result.getMessage()));
            }

        } catch (Exception e) {
            sharedPreferences.edit().putBoolean(SplashActivity.GOTTOKEN, false).apply();
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(GETTOKEN_ERROR).putExtra(GETTOKEN_RESULT, getResources().getString(R.string.networkError)));
        }

    }
}
