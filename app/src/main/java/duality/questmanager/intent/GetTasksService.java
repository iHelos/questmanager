package duality.questmanager.intent;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import duality.questmanager.AuthToken;
import duality.questmanager.R;
import duality.questmanager.processor.GetTaskProcessor;
import duality.questmanager.rest.RESTAnswer;

/**
 * GETd by root on 22.05.16.
 */
public class GetTasksService extends IntentService {

   // public static final String GET_TASK_ID = "GetHashTaskID";
    public static final String GET_TASK_IS_OUTPUT = "GetHashTaskIsOutput";

    public static final String GET_TASK_SUCCESS = "GetHashTaskSuccess";
    public static final String GET_TASK_ERROR = "GetHashTaskError";
    public static final String GET_TASK_RESULT = "GetHashTaskResult";

    public GetTasksService() {
        super("GetTasksService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
           // final String idStr = intent.getStringExtra(GET_TASK_ID);
            final Boolean output = intent.getBooleanExtra(GET_TASK_IS_OUTPUT, false);

            String token = AuthToken.getToken(sharedPreferences);



            RESTAnswer result = GetTaskProcessor.getTasks(output, token, getApplicationContext());
            if (result.getStatus() == 200)
            {
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(GET_TASK_SUCCESS)
                        .putExtra(GET_TASK_RESULT, result.getMessage())
                );
            }
            else {

                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(GET_TASK_ERROR).putExtra(GET_TASK_RESULT, result.getMessage()));
            }

        } catch (Exception e) {
            e.printStackTrace();
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(GET_TASK_ERROR).putExtra(GET_TASK_RESULT, getResources().getString(R.string.networkError)));
        }

    }
}
