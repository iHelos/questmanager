package duality.questmanager.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import duality.questmanager.AuthToken;
import duality.questmanager.R;
import duality.questmanager.SplashActivity;
import duality.questmanager.content.QuestDatabaseHelper;
import duality.questmanager.intent.GetTokenService;

/**
 * Created by olegermakov on 18.04.16.
 */
public class MessageGCMListener extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String title = "Quest Manager";
        String message = "";
        String typeStr = data.getString("type");
        int type = Integer.parseInt(typeStr);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        switch (type)
        {
            case 0:
                message = data.getString("message");
                String auth_token = data.getString("auth_token");
                sharedPreferences.edit().putBoolean(SplashActivity.GOTTOKEN, true).apply();
                AuthToken.setToken(auth_token, sharedPreferences);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(GetTokenService.GETTOKEN_SUCCESS));
                sendNotification(0, title, message);
                break;
            case 1:
                String idStr = data.getString("id");
                int id = Integer.parseInt(idStr);
                message = data.getString("text");
                title = data.getString("title");

                String user = data.getString("user");
                String date = data.getString("date");

                String priceStr = data.getString("price");
                int price = Integer.parseInt(priceStr);

                QuestDatabaseHelper DB = new QuestDatabaseHelper(getApplicationContext());
                DB.addTask(id, title,message,price,user,date,false);

                sendNotification(id, title, message);
                break;
            case 2:
                break;
            default:
                break;
        }
    }

    private void sendNotification(int id, String title, String message) {
        int color = ContextCompat.getColor(getApplicationContext(), R.color.material_drawer_primary);

        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_qm)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setColor(color);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(id, notificationBuilder.build());
    }
}