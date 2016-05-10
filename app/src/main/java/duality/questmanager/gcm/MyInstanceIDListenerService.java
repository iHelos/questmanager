package duality.questmanager.gcm;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by olegermakov on 18.04.16.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {

    private static final String TAG = "MyInstanceIDLS";
    @Override
    public void onTokenRefresh() {
//        Intent intent = new Intent(this, RegistrationIntentService.class);
//        startService(intent);
        //TODO
    }
}
