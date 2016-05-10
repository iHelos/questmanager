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
 * Created by olegermakov on 10.05.16.
 */
public class GetTokenServiceHelper {
    private static int mIdCounter = 1;
    private static Map<Integer, ResultListener> mListeners = new Hashtable<>();

    public static int start(final Context context, final ResultListener listener) {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(GetTokenService.GETTOKEN_SUCCESS);
        filter.addAction(GetTokenService.GETTOKEN_ERROR);

        BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String result = intent.getStringExtra(GetTokenService.GETTOKEN_RESULT);
                final boolean success = intent.getAction().equals(GetTokenService.GETTOKEN_SUCCESS);
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
        context.startService((intent));

        return mIdCounter++;
    }

    public static void removeListener(final int id) {
        mListeners.remove(id);
    }
}
