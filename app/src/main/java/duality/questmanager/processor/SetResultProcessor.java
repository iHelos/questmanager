package duality.questmanager.processor;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import duality.questmanager.content.QuestDatabaseHelper;
import duality.questmanager.rest.RESTAnswer;
import duality.questmanager.rest.Register;
import duality.questmanager.rest.SetResult;

/**
 * Created by root on 23.05.16.
 */
public class SetResultProcessor {
    private final static String LOG_TAG = RegisterProcessor.class.getSimpleName();

    public static RESTAnswer process(final String token, final String task_id, final String user_result, final Context context) throws IOException {

        QuestDatabaseHelper DB = new QuestDatabaseHelper(context);

        RESTAnswer temp = new SetResult().start(task_id, user_result, token);
        String tempstr = temp.getMessage();
        JsonObject msg = new Gson().fromJson(tempstr, JsonObject.class);
        String detail;
        if (temp.getStatus()==200) {
            detail="";
            int res = msg.get("result").getAsInt();
            int task_id_int = Integer.parseInt(task_id);
            DB.setComplete(task_id_int, res, true);
        }
        else {
            detail = msg.get("email").getAsString();
        }
        temp.setMessage(detail);
        return temp;
    }
}
