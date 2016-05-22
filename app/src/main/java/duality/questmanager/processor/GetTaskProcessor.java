package duality.questmanager.processor;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.GregorianCalendar;

import duality.questmanager.Task;
import duality.questmanager.content.QuestDatabaseHelper;
import duality.questmanager.rest.GetTasks;
import duality.questmanager.rest.RESTAnswer;
import duality.questmanager.rest.Register;

/**
 * Created by root on 21.05.16.
 */
public class GetTaskProcessor {
    private final static String LOG_TAG = GetTaskProcessor.class.getSimpleName();

    public static RESTAnswer getTasks(
            final Boolean output,
            final String token,
            final Context context
    ) throws IOException {

        //int id = Integer.parseInt(idStr);

        QuestDatabaseHelper DB = new QuestDatabaseHelper(context);
        Task lastSyncedTask = DB.getLastSyncedTask(output);
        String hash = "";
        if (lastSyncedTask != null)
            hash = lastSyncedTask.getHash();

        Log.d(LOG_TAG, hash);
        RESTAnswer temp = new GetTasks().start(hash, output, token);
        String tempstr = temp.getMessage();
        //JsonObject msg = new Gson().fromJson(tempstr, JsonObject.class);
        String detail = "";
        if (temp.getStatus() == 200) {
            DB.deleteUnsynced(output);
            JsonArray entries = (JsonArray) new JsonParser().parse(tempstr);
            for (JsonElement entry : entries) {
                JsonObject json = (JsonObject) entry;

                int id = json.get("id").getAsInt();
                String title = json.get("name").getAsString();
                String message = json.get("text").getAsString();
                String user = json.get("user").getAsString();
                int price = json.get("price").getAsInt();
                String date = json.get("date").getAsString();
                String new_hash = json.get("hash").getAsString();
                Boolean isCompleted = json.get("isCompleted").getAsBoolean();

                DB.syncedAddTask(id, title, message, price, user, date, new_hash, isCompleted, output);
            }
        } else {
            JsonObject msg = new Gson().fromJson(tempstr, JsonObject.class);
            detail = msg.get("email").getAsString();
        }
        temp.setMessage(detail);
        return temp;
    }
}
