package duality.questmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import duality.questmanager.content.QuestDatabaseHelper;
import duality.questmanager.gcm.GCMServiceHelper;
import duality.questmanager.intent.GetTasksServiceHelper;
import duality.questmanager.intent.GetTokenServiceHelper;
import duality.questmanager.rest.ResultListener;

/**
 * Created by olegermakov on 17.04.16.
 */
public class LoginActivity extends AppCompatActivity implements ResultListener {

    private static final String TAG = "LoginActivity";


    private ProgressBar mRegistrationProgressBar;
    private TextView mInformationTextView;
    private EditText emailEditText;
    private TextInputLayout emailLayout;
    private Button logButton;
    private int mRequestId;
    private int check_token_id;

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

       // Preferences.updateTheme(this);
        Preferences.updateLocaleIfNeeded(this);

        setContentView(R.layout.activity_gcm);

        mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);
        mInformationTextView = (TextView) findViewById(R.id.loginErrorInfo);
        mInformationTextView.setVisibility(View.GONE);
        emailEditText = (EditText) findViewById(R.id.input_email);
        emailLayout = (TextInputLayout) findViewById(R.id.input_layout_name_email);
        logButton = (Button) findViewById(R.id.btn_login);

        mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
    }

    @Override
    protected void onStop() {
        GCMServiceHelper.removeListener(mRequestId);
        super.onStop();
    }

    public void onSignUpClick(View view) {
        Boolean isValid = false;
        String email = emailEditText.getText().toString();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String old_email =  sharedPreferences.getString(FragmentsActivity.EmailSPTag, "");
        if (email.equals(old_email))
        {
            mRegistrationProgressBar.setVisibility(ProgressBar.VISIBLE);
            logButton.setVisibility(View.INVISIBLE);
            check_token_id =  GetTokenServiceHelper.start(this, new ResultListener() {
                @Override
                public void onSuccess(String result) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    sharedPreferences.edit().putBoolean(SplashActivity.ISLOGGEDIN, true).apply();
                    sharedPreferences.edit().putBoolean(SplashActivity.GOTTOKEN, true).apply();
                    GetTasksServiceHelper.start(getApplicationContext(), backgroundListener, false);
                    GetTasksServiceHelper.start(getApplicationContext(), backgroundListener, true);
                }

                @Override
                public void onFail(String result) {
                    mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                    logButton.setVisibility(View.VISIBLE);
                }
            });
        }
        else {

            if (email.length() == 0) {
                emailLayout.setError(getString(R.string.field_required));
                isValid = false;
            } else {

                if (!isValidEmail(email)) {
                    emailLayout.setError(getString(R.string.invalid_email));
                } else {
                    isValid = true;
                    emailLayout.setError(null);
                }

            }
            if (isValid) {
                mRegistrationProgressBar.setVisibility(ProgressBar.VISIBLE);
                logButton.setVisibility(View.INVISIBLE);


                sharedPreferences.edit().putString(FragmentsActivity.EmailSPTag, email).apply();

                String dev_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
                mRequestId = GCMServiceHelper.GCMRegister(this, email, dev_id, this);
            }
        }
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString(FragmentsActivity.EmailSPTag, "").apply();

    }

    public static void quit(Context context)
    {
        QuestDatabaseHelper DB = new QuestDatabaseHelper(context);
        DB.cleanDB();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(SplashActivity.ISLOGGEDIN, false).apply();
        sharedPreferences.edit().putBoolean(SplashActivity.GOTTOKEN, false).apply();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    static public boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
