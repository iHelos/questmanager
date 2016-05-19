package duality.questmanager.intent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.util.Hashtable;
import java.util.Map;

import duality.questmanager.gcm.RegistrationIntentService;
import duality.questmanager.rest.ResultListener;

/**
 * Created by olegermakov on 11.05.16.
 */
public class CreateTaskServiceHelper {
    private static int mIdCounter = 1;
    private static Map<Integer, ResultListener> mListeners = new Hashtable<>();

    public static int start(final Context context, final ResultListener listener, final String title, final String text, final String price, final String reciever, final String year, final String month, final String day) {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(CreateTaskService.CREATE_TASK_SUCCESS);
        filter.addAction(CreateTaskService.CREATE_TASK_ERROR);

        BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String result = intent.getStringExtra(CreateTaskService.CREATE_TASK_RESULT);
                final boolean success = intent.getAction().equals(CreateTaskService.CREATE_TASK_SUCCESS);
                if (success) {
                    for (Map.Entry<Integer, ResultListener> pair : mListeners.entrySet()) {
                        pair.getValue().onSuccess(result);
                    }
                } else {
                    for (Map.Entry<Integer, ResultListener> pair : mListeners.entrySet()) {
                        pair.getValue().onFail(result);
                    }
                }
                mListeners.clear();
            }
        };
        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver, filter);

        mListeners.put(mIdCounter, listener);

        Intent intent = new Intent(context, GetTokenService.class);
        intent.putExtra(CreateTaskService.CREATE_TASK_TITLE, title);
        intent.putExtra(CreateTaskService.CREATE_TASK_TEXT, text);
        intent.putExtra(CreateTaskService.CREATE_TASK_PRICE, price);
        intent.putExtra(CreateTaskService.CREATE_TASK_RECIEVER, reciever);
        intent.putExtra(CreateTaskService.CREATE_TASK_YEAR, year);
        intent.putExtra(CreateTaskService.CREATE_TASK_MONTH, month);
        intent.putExtra(CreateTaskService.CREATE_TASK_DAY, day);

        context.startService((intent));

        return mIdCounter++;
    }

    public static void removeListener(final int id) {
        mListeners.remove(id);
    }
}
