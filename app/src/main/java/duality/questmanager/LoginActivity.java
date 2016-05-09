package duality.questmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import duality.questmanager.gcm.GCMServiceHelper;

/**
 * Created by olegermakov on 17.04.16.
 */
public class LoginActivity extends AppCompatActivity implements GCMServiceHelper.ResultListener{

    private static final String TAG = "MainActivity";


    private ProgressBar mRegistrationProgressBar;
    private TextView mInformationTextView;
    private EditText emailEditText;
    private Button logButton;
    private int mRequestId;
    private boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcm);

        mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);
        mInformationTextView = (TextView) findViewById(R.id.informationTextView);
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
        logButton.setVisibility(View.GONE);
        String email = emailEditText.getText().toString();
        mRequestId = GCMServiceHelper.GCMRegister(this, email, this);
    }

    @Override
    public void onRegisterResult(boolean success, String result) {
        mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
        if (success){
            Intent intent = new Intent(this, FragmentsActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            logButton.setVisibility(View.VISIBLE);
        }
    }
}
