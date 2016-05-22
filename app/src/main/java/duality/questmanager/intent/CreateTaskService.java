package duality.questmanager.intent;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import duality.questmanager.AuthToken;
import duality.questmanager.R;
import duality.questmanager.SplashActivity;
import duality.questmanager.processor.CreateTaskProcessor;
import duality.questmanager.rest.RESTAnswer;

/**
 * Created by olegermakov on 11.05.16.
 */
public class CreateTaskService extends IntentService {

    public static final String CREATE_TASK_TITLE = "CreateTaskTitle";
    public static final String CREATE_TASK_TEXT = "CreateTaskText";
    public static final String CREATE_TASK_PRICE = "CreateTaskPrice";
    public static final String CREATE_TASK_RECIEVER = "CreateTaskReciever";
    public static final String CREATE_TASK_YEAR = "CreateTaskYear";
    public static final String CREATE_TASK_MONTH = "CreateTaskMonth";
    public static final String CREATE_TASK_DAY = "CreateTaskDay";

    public static final String CREATE_TASK_DATE = "CreateTaskDay";

    public static final String CREATE_TASK_SUCCESS = "CreateTaskSuccess";
    public static final String CREATE_TASK_ERROR = "CreateTaskError";
    public static final String CREATE_TASK_RESULT = "CreateTaskResult";

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

            RESTAnswer result = CreateTaskProcessor.createTaskProccess(title, text, price, reciever, year, month, day, token, getApplicationContext());
            if (result.getStatus() == 200)
            {
                String date = year + '-' + month + '-' + day;
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(CREATE_TASK_SUCCESS)
                        .putExtra(CREATE_TASK_RESULT, result.getMessage())
                        .putExtra(CREATE_TASK_TITLE, title)
                        .putExtra(CREATE_TASK_PRICE, price)
                        .putExtra(CREATE_TASK_DATE, date)
                );
            }
            else {

                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(CREATE_TASK_ERROR).putExtra(CREATE_TASK_RESULT, result.getMessage()));
            }

        } catch (Exception e) {
            e.printStackTrace();
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(CREATE_TASK_ERROR).putExtra(CREATE_TASK_RESULT, getResources().getString(R.string.networkError)));
        }

    }
}
