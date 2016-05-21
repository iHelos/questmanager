package duality.questmanager.processor;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import duality.questmanager.R;
import duality.questmanager.content.QuestDatabaseHelper;
import duality.questmanager.rest.CreateTask;
import duality.questmanager.rest.RESTAnswer;

/**
 * Created by olegermakov on 08.05.16.
 */
public class CreateTaskProcessor {
    private final static String LOG_TAG = CreateTaskProcessor.class.getSimpleName();


    public static RESTAnswer createTaskProccess(
            final String title,
            final String text,
            final String price,
            final String reciever,
            final String year,
            final String month,
            final String day,
            final String token,
            Context context
    ) throws IOException {
        RESTAnswer temp = new CreateTask().start(title, text, price, reciever, year, month, day, token);
        String tempstr = temp.getMessage();
        JsonObject msg = new Gson().fromJson(tempstr, JsonObject.class);
        String detail;
        if (temp.getStatus()==200) {
            detail=msg.get("task").getAsString();

            int id = Integer.parseInt(detail);
            QuestDatabaseHelper DB = new QuestDatabaseHelper(context);
            int num_price = Integer.parseInt(price);
            DB.addTask(id,title,text,num_price,reciever,year+'-'+month+'-'+day,true);
        }
        else {
            detail = msg.get("email").getAsString();
        }
        temp.setMessage(detail);
        return temp;
    }

}
