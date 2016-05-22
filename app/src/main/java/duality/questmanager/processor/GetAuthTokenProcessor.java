package duality.questmanager.processor;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import duality.questmanager.FragmentsActivity;
import duality.questmanager.rest.GetAuthToken;
import duality.questmanager.rest.RESTAnswer;

/**
 * Created by olegermakov on 09.05.16.
 */
public class GetAuthTokenProcessor {

    private final static String LOG_TAG = GetAuthTokenProcessor.class.getSimpleName();

    public static RESTAnswer getToken(final String token, Context context) throws IOException {
        RESTAnswer temp = new GetAuthToken().start(token);
        String tempstr = temp.getMessage();
        JsonObject msg = new Gson().fromJson(tempstr, JsonObject.class);
        String detail;
        if (temp.getStatus()==200) {
            detail = msg.get("token").getAsString();
            String bankStr = msg.get("bank").getAsString();

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            sharedPreferences.edit().putString(FragmentsActivity.BankSPTag, bankStr).apply();
        }
        else {
            detail = msg.get("detail").getAsString();
        }
        temp.setMessage(detail);
        return temp;
    }

}
