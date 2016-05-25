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
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import duality.questmanager.AuthToken;
import duality.questmanager.FragmentsActivity;
import duality.questmanager.R;
import duality.questmanager.SplashActivity;
import duality.questmanager.content.QuestDatabaseHelper;
import duality.questmanager.intent.GetTokenService;

/**
 * Created by olegermakov on 18.04.16.
 */
public class MessageGCMListener extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    public static final String RECIEVE_TASK_SUCCESS = "GCMRecieveTask";
    public static final String RECIEVE_TASK_ID = "GCMRecieveTaskID";
    public static final String RECIEVE_TASK_TITLE = "GCMRecieveTaskTitle";
    public static final String RECIEVE_TASK_PRICE = "GCMRecieveTaskPrice";
    public static final String RECIEVE_TASK_DATE = "GCMRecieveTaskDate";


    public static final String GET_TASKRESULT_SUCCESS = "GCMGetTaskResult";
    public static final String GET_TASKRESULT_ID = "GCMGetTaskResult_ID";
    public static final String GET_TASKRESULT_RESULT = "GCMGetTaskResult_Result";

    public static final String SEND_OUT_TASK_SUCCESS = "GCMSendOutTask";
    public static final String SEND_OUT_TASK_ID = "GCMSendOutTaskID";
    public static final String SEND_OUT_TASK_TITLE = "GCMSendOutTaskTitle";
    public static final String SEND_OUT_TASK_PRICE = "GCMSendOutTaskPrice";
    public static final String SEND_OUT_TASK_DATE = "GCMSendOutTaskDate";

    public static final String OUT_TASKRESULT_SUCCESS = "GCMOUTTaskResult";
    public static final String OUT_TASKRESULT_ID = "GCMOUTTaskResult_ID";
    public static final String OUT_TASKRESULT_RESULT = "GCMOUTTaskResult_Result";


    public static final String NEW_BANK = "GCMNewBank";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String title = "Quest Manager";
        String message = "";
        String typeStr = data.getString("type");
        int type = Integer.parseInt(typeStr);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        QuestDatabaseHelper DB = new QuestDatabaseHelper(getApplicationContext());
        switch (type)
        {
            case 0:
                message = data.getString("message");
                String auth_token = data.getString("auth_token");
                String bankStr = data.getString("bank");
                Log.d(TAG + "-bk", bankStr);
                sharedPreferences.edit().putString(FragmentsActivity.BankSPTag, bankStr).apply();
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

                String hash = data.getString("hash");

                String priceStr = data.getString("price");
                int price = Integer.parseInt(priceStr);

                DB.addTask(id, title, message, price, user, date, hash, false);

                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(RECIEVE_TASK_SUCCESS)
                        .putExtra(RECIEVE_TASK_ID, idStr)
                        .putExtra(RECIEVE_TASK_TITLE, title)
                        .putExtra(RECIEVE_TASK_PRICE, priceStr)
                        .putExtra(RECIEVE_TASK_DATE, date)
                );

                sendNotification(id, title, message);
                break;
            case 2:
                String backIDStr = data.getString("id");
                String isCompletedStr = data.getString("isCompleted");
                int isCompleted = Integer.parseInt(isCompletedStr);
                int backID = Integer.parseInt(backIDStr);
                Log.d("isCompleted", isCompletedStr);
                DB.setComplete(backID, isCompleted, false);

                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(GET_TASKRESULT_SUCCESS)
                        .putExtra(GET_TASKRESULT_ID, backIDStr)
                        .putExtra(GET_TASKRESULT_RESULT, isCompletedStr)
                );

                break;

            case 3:
                String idStrOut = data.getString("id");
                int idOut = Integer.parseInt(idStrOut);
                message = data.getString("text");
                title = data.getString("title");

                String userOut = data.getString("user");
                String dateOut = data.getString("date");

                String hashOut = data.getString("hash");

                String priceStrOut = data.getString("price");
                int priceOut = Integer.parseInt(priceStrOut);

                DB.addTask(idOut, title, message, priceOut, userOut, dateOut, hashOut, true);

                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(SEND_OUT_TASK_SUCCESS)
                        .putExtra(SEND_OUT_TASK_ID, idStrOut)
                        .putExtra(SEND_OUT_TASK_TITLE, title)
                        .putExtra(SEND_OUT_TASK_PRICE, priceStrOut)
                        .putExtra(SEND_OUT_TASK_DATE, dateOut)
                );
                break;

            case 4:
                String backIDStrOut = data.getString("id");
                String isCompletedStrOut = data.getString("isCompleted");
                int isCompletedOut = Integer.parseInt(isCompletedStrOut);
                int backIDOut = Integer.parseInt(backIDStrOut);

                DB.setComplete(backIDOut, isCompletedOut, true);

                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(OUT_TASKRESULT_SUCCESS)
                        .putExtra(OUT_TASKRESULT_ID, backIDStrOut)
                        .putExtra(OUT_TASKRESULT_RESULT, isCompletedStrOut)
                );

                break;
            case 5:
                String bank = data.getString("bank");
                sharedPreferences.edit().putString(FragmentsActivity.BankSPTag, bank).apply();
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(NEW_BANK));
            default:
                break;
        }
    }

    private void sendNotification(int id, String title, String message) {
        int color = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);

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