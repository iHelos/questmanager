package duality.questmanager.intent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.util.Hashtable;
import java.util.Map;

import duality.questmanager.rest.ResultListener;
import duality.questmanager.rest.SetResult;

/**
 * Created by root on 23.05.16.
 */
public class SetResultServiceHelper {
    private static int mIdCounter = 1;
    private static Map<Integer, ResultListener> mListeners = new Hashtable<>();

    public static int start(final Context context, final ResultListener listener, final String task_id, final String user_result) {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(SetResultService.SETRESULT_SUCCESS);
        filter.addAction(SetResultService.SETRESULT_ERROR);

        BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String result = intent.getStringExtra(SetResultService.SETRESULT_RESULT);
                final boolean success = intent.getAction().equals(SetResultService.SETRESULT_SUCCESS);
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

        Intent intent = new Intent(context, SetResultService.class);
        intent.putExtra(SetResultService.SETRESULT_TASK_ID, task_id);
        intent.putExtra(SetResultService.SETRESULT_USER_RESULT, user_result);

        context.startService((intent));

        return mIdCounter++;
    }

    public static void removeListener(final int id) {
        mListeners.remove(id);
    }
}
