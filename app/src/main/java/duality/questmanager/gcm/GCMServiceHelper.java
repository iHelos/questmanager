package duality.questmanager.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.util.Hashtable;
import java.util.Map;

import duality.questmanager.rest.ResultListener;

/**
 * Created by olegermakov on 20.04.16.
 */
public class GCMServiceHelper {
    private static int mIdCounter = 1;
    private static Map<Integer, ResultListener> mListeners = new Hashtable<>();
    private static BroadcastReceiver mRegistrationBroadcastReceiver;

    public static int GCMRegister(final Context context, final String email, final String dev_id, final ResultListener listener) {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(RegistrationIntentService.REGISTRATION_SUCCESS);
        filter.addAction(RegistrationIntentService.REGISTRATION_ERROR);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String result = intent.getStringExtra(RegistrationIntentService.REGISTRATION_RESULT);
                final boolean success = intent.getAction().equals(RegistrationIntentService.REGISTRATION_SUCCESS);
                if (success) {
                    for (Map.Entry<Integer, ResultListener> pair : mListeners.entrySet()) {
                        pair.getValue().onSuccess(result);
                    }
                }
                else
                {
                    for (Map.Entry<Integer, ResultListener> pair : mListeners.entrySet()) {
                        pair.getValue().onFail(result);
                    }
                }
                mListeners.clear();
            }
        };
        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver, filter);

        mListeners.put(mIdCounter, listener);

        Intent intent = new Intent(context, RegistrationIntentService.class);
        intent.putExtra(RegistrationIntentService.REGISTER_EMAIL, email);
        intent.putExtra(RegistrationIntentService.REGISTER_DEV_ID, dev_id);
        context.startService((intent));

        return mIdCounter++;
    }

    public static void removeListener(final int id) {
        mListeners.remove(id);
    }

}
