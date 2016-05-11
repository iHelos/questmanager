package duality.questmanager.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import duality.questmanager.AuthToken;
import duality.questmanager.R;
import duality.questmanager.SplashActivity;
import duality.questmanager.intent.GetTokenService;

/**
 * Created by olegermakov on 18.04.16.
 */
public class MessageGCMListener extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        String auth_token = data.getString("auth_token");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        Log.d(TAG, "Auth-token: " + auth_token);

        if (auth_token!=null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

            if(sharedPreferences.getBoolean(SplashActivity.ISLOGGEDIN, false)) {
                sharedPreferences.edit().putBoolean(SplashActivity.GOTTOKEN, true).apply();
                AuthToken.setToken(auth_token, sharedPreferences);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(GetTokenService.GETTOKEN_SUCCESS));
                sendNotification(message);
            }
        }
        else
        {
            sendNotification(message);
        }
    }

    private void sendNotification(String message) {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}