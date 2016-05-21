package duality.questmanager.processor;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import duality.questmanager.Task;
import duality.questmanager.rest.RESTAnswer;
import duality.questmanager.rest.Register;

/**
 * Created by root on 21.05.16.
 */
public class GetTaskProcessor {
    private final static String LOG_TAG = GetTaskProcessor.class.getSimpleName();

    public static RESTAnswer getTasks(
            final Task task,
            final String token,
            Context context
    ) throws IOException {
        RESTAnswer temp = new Register().processText(token, "", "");
        String tempstr = temp.getMessage();
        JsonObject msg = new Gson().fromJson(tempstr, JsonObject.class);
        String detail;
        if (temp.getStatus()==200) {
            detail="";
        }
        else {
            detail = msg.get("email").getAsString();
        }
        temp.setMessage(detail);
        return temp;
    }
}
