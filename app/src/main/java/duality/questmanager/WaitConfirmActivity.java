package duality.questmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.Hashtable;
import java.util.Map;

import duality.questmanager.gcm.MessageGCMListener;
import duality.questmanager.intent.GetTasksServiceHelper;
import duality.questmanager.intent.GetTokenService;
import duality.questmanager.rest.ResultListener;

/**
 * Created by olegermakov on 11.05.16.
 */
public class WaitConfirmActivity extends AppCompatActivity implements ResultListener {
    private static final String TAG = "WaitConfirmActivity";

    private static int mIdCounter = 1;
    private static Map<Integer, ResultListener> mListeners = new Hashtable<>();

    private ProgressBar waitConfirmProgressBar;
    private Button cancelButton;

    private ResultListener backgroundListener = new ResultListener() {
        @Override
        public void onSuccess(String result) {
            Intent intent = new Intent(getApplicationContext(), FragmentsActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        public void onFail(String result) {
            Intent intent = new Intent(getApplicationContext(), FragmentsActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Preferences.updateLocaleIfNeeded(this);

        setContentView(R.layout.activity_waitconfirm);

        final IntentFilter filter = new IntentFilter();
        filter.addAction(GetTokenService.GETTOKEN_SUCCESS);

        waitConfirmProgressBar = (ProgressBar) this.findViewById(R.id.waitConfirmProgressBar);
        waitConfirmProgressBar.setVisibility(View.INVISIBLE);
        cancelButton = (Button) this.findViewById(R.id.WaitConfirm_btn);

        BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                for (Map.Entry<Integer, ResultListener> pair : mListeners.entrySet()) {
                    pair.getValue().onSuccess("");
                }
                mListeners.clear();
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, filter);

        mListeners.put(mIdCounter, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mListeners.remove(mIdCounter);
    }

    public void onWaitConfirmCancel(View view) {
        LoginActivity.quit(this);
    }

    @Override
    public void onSuccess(String result) {
        waitConfirmProgressBar.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.GONE);

        GetTasksServiceHelper.start(getApplicationContext(), backgroundListener, false);
        GetTasksServiceHelper.start(getApplicationContext(), backgroundListener, true);
    }

    @Override
    public void onFail(String result) {

    }
}
