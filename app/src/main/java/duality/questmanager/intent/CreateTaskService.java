package duality.questmanager.intent;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import duality.questmanager.AuthToken;
import duality.questmanager.SplashActivity;
import duality.questmanager.processor.CreateTaskProcessor;
import duality.questmanager.rest.RESTAnswer;

/**
 * Created by olegermakov on 11.05.16.
 */
public class CreateTaskService extends IntentService {

    private static final String CREATE_TASK_TITLE = "CreateTaskTitle";
    private static final String CREATE_TASK_TEXT = "CreateTaskText";
    private static final String CREATE_TASK_PRICE = "CreateTaskPrice";
    private static final String CREATE_TASK_RECIEVER = "CreateTaskReciever";
    private static final String CREATE_TASK_YEAR = "CreateTaskYear";
    private static final String CREATE_TASK_MONTH = "CreateTaskMonth";
    private static final String CREATE_TASK_DAY = "CreateTaskDAy";

    private static final String CREATE_TASK_SUCCESS = "CreateTaskSuccess";
    private static final String CREATE_TASK_ERROR = "CreateTaskError";
    private static final String CREATE_TASK_RESULT = "CreateTaskResult";

    public CreateTaskService() {
        super("CreateTaskService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            final String title = intent.getStringExtra(CREATE_TASK_TITLE);
            final String text = intent.getStringExtra(CREATE_TASK_TEXT);
            final String price = intent.getStringExtra(CREATE_TASK_PRICE);
            final String reciever = intent.getStringExtra(CREATE_TASK_RECIEVER);
            final String year = intent.getStringExtra(CREATE_TASK_YEAR);
            final String month = intent.getStringExtra(CREATE_TASK_MONTH);
            final String day = intent.getStringExtra(CREATE_TASK_DAY);

            String token = AuthToken.getToken(sharedPreferences);

            RESTAnswer result = CreateTaskProcessor.createTaskProccess(title, text, price, reciever, year, month, day, token);
            if (result.getStatus() == 200)
            {
                sharedPreferences.edit().putBoolean(SplashActivity.GOTTOKEN, true).apply();
                AuthToken.setToken(result.getMessage(), sharedPreferences);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(CREATE_TASK_SUCCESS).putExtra(CREATE_TASK_RESULT, result.getMessage()));
            }
            else {
                sharedPreferences.edit().putBoolean(SplashActivity.GOTTOKEN, false).apply();
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(CREATE_TASK_ERROR).putExtra(CREATE_TASK_RESULT, result.getMessage()));
            }

        } catch (Exception e) {
            sharedPreferences.edit().putBoolean(SplashActivity.GOTTOKEN, false).apply();
        }

    }
}
