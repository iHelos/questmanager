package duality.questmanager.processor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import duality.questmanager.rest.CreateTask;
import duality.questmanager.rest.RESTAnswer;

/**
 * Created by olegermakov on 08.05.16.
 */
public class CreateTaskProcessor {
    private final static String LOG_TAG = CreateTaskProcessor.class.getSimpleName();


    public static RESTAnswer createTaskProccess(final String title, final String text, final String price, final String reciever, final String year, final String month, final String day, final String token) throws IOException {
        RESTAnswer temp = new CreateTask().start(title, text, price, reciever, year, month, day, token);
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
