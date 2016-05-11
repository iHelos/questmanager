package duality.questmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import duality.questmanager.gcm.GCMServiceHelper;
import duality.questmanager.rest.ResultListener;

/**
 * Created by olegermakov on 17.04.16.
 */
public class LoginActivity extends AppCompatActivity implements ResultListener {

    private static final String TAG = "MainActivity";


    private ProgressBar mRegistrationProgressBar;
    private TextView mInformationTextView;
    private EditText emailEditText;
    private Button logButton;
    private int mRequestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcm);

        mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);
        mInformationTextView = (TextView) findViewById(R.id.loginErrorInfo);
        mInformationTextView.setVisibility(View.GONE);
        emailEditText = (EditText) findViewById(R.id.input_email);
        logButton = (Button) findViewById(R.id.btn_login);

        mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
    }

    @Override
    protected void onStop() {
        GCMServiceHelper.removeListener(mRequestId);
        super.onStop();
    }

    public void onSignUpClick(View view) {
        mRegistrationProgressBar.setVisibility(ProgressBar.VISIBLE);
        logButton.setVisibility(View.INVISIBLE);
        String email = emailEditText.getText().toString();
        String dev_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        mRequestId = GCMServiceHelper.GCMRegister(this, email, dev_id, this);
    }

    @Override
    public void onSuccess(String result) {
        mRegistrationProgressBar.setVisibility(ProgressBar.GONE);

        Intent intent = new Intent(this, WaitConfirmActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onFail(String result) {
        mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
        logButton.setVisibility(View.VISIBLE);
        mInformationTextView.setText(result);
        mInformationTextView.setVisibility(View.VISIBLE);

    }

    public static void quit(Context context)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(SplashActivity.ISLOGGEDIN, false).apply();
        sharedPreferences.edit().putBoolean(SplashActivity.GOTTOKEN, false).apply();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
