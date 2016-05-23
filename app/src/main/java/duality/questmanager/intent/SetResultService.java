package duality.questmanager.intent;

import android.app.IntentService;
import android.content.Context;
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
import duality.questmanager.processor.SetResultProcessor;
import duality.questmanager.rest.RESTAnswer;

/**
 * Created by root on 23.05.16.
 */
public class SetResultService extends IntentService {
    public static final String SETRESULT_SUCCESS = "setResult_success";
    public static final String SETRESULT_ERROR = "setResult_error";
    public static final String SETRESULT_RESULT = "setResult_result";

    public static final String SETRESULT_TASK_ID = "setResult_TASK_ID";
    public static final String SETRESULT_USER_RESULT= "setResult_USER_RESULT";

    public SetResultService() {
        super("SetResultService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String token = AuthToken.getToken(sharedPreferences);
        final String task_id = intent.getStringExtra(SETRESULT_TASK_ID);
        final String user_result = intent.getStringExtra(SETRESULT_USER_RESULT);

        Context context = getApplicationContext();

        try {
            RESTAnswer result = SetResultProcessor.process(token, task_id, user_result, context);
            if (result.getStatus() == 200)
            {
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(SETRESULT_SUCCESS).putExtra(SETRESULT_RESULT, result.getMessage()));
            }
            else {
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(SETRESULT_ERROR).putExtra(SETRESULT_RESULT, result.getMessage()));
            }

        } catch (Exception e) {
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(SETRESULT_ERROR).putExtra(SETRESULT_RESULT, getResources().getString(R.string.networkError)));
        }

    }
}
