package duality.questmanager.intent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.util.Hashtable;
import java.util.Map;

import duality.questmanager.rest.ResultListener;

/**
 * Created by root on 22.05.16.
 */
public class GetTasksServiceHelper {
    private static int mIdCounter = 1;
    private static Map<Integer, ResultListener> mListeners = new Hashtable<>();

    public static int start(final Context context, final ResultListener listener, Boolean output) {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(GetTasksService.GET_TASK_SUCCESS);
        filter.addAction(GetTasksService.GET_TASK_ERROR);

        BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String result = intent.getStringExtra(GetTasksService.GET_TASK_RESULT);
                final boolean success = intent.getAction().equals(GetTasksService.GET_TASK_SUCCESS);
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

        Intent intent = new Intent(context, GetTasksService.class);
        intent.putExtra(GetTasksService.GET_TASK_IS_OUTPUT, output);

        context.startService((intent));

        return mIdCounter++;
    }

    public static void removeListener(final int id) {
        mListeners.remove(id);
    }
}
