package duality.questmanager.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by olegermakov on 20.04.16.
 */
public class GCMServiceHelper {
    private static int mIdCounter = 1;
    private static Map<Integer, ResultListener> mListeners = new Hashtable<>();
    private static BroadcastReceiver mRegistrationBroadcastReceiver;

    public static int GCMRegister(final Context context, final String email, final ResultListener listener) {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(RegistrationIntentService.REGISTRATION_SUCCESS);
        filter.addAction(RegistrationIntentService.REGISTRATION_ERROR);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String result = intent.getStringExtra(RegistrationIntentService.REGISTRATION_RESULT);
                final boolean success = intent.getAction().equals(RegistrationIntentService.REGISTRATION_SUCCESS);
                for (Map.Entry<Integer, ResultListener> pair : mListeners.entrySet()) {
                    pair.getValue().onRegisterResult(success, result);
                }
                mListeners.clear();
//                mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
//                SharedPreferences sharedPreferences =
//                        PreferenceManager.getDefaultSharedPreferences(context);
//                boolean sentToken = sharedPreferences
//                        .getBoolean(RegistrationIntentService.SENT_TOKEN_TO_SERVER, false);
//                if (sentToken) {
//                    mInformationTextView.setText(getString(R.string.gcm_send_message));
//                } else {
//                    mInformationTextView.setText(getString(R.string.token_error_message));
//                }
            }
        };
        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver, filter);

        mListeners.put(mIdCounter, listener);

        Intent intent = new Intent(context, RegistrationIntentService.class);
        intent.putExtra(RegistrationIntentService.REGISTER_EMAIL, email);
        context.startService((intent));

        return mIdCounter++;
    }

    public static void removeListener(final int id) {
        mListeners.remove(id);
    }


    public interface ResultListener {
        void onRegisterResult(final boolean success, final String result);
    }
}
