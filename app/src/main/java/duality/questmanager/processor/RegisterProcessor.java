package duality.questmanager.processor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import duality.questmanager.rest.RESTAnswer;
import duality.questmanager.rest.Register;


/**
 * Created by olegermakov on 20.04.16.
 */
public class RegisterProcessor {
    private final static String LOG_TAG = RegisterProcessor.class.getSimpleName();

    public static RESTAnswer processSignUp(final String token, final String dev_id, final String email) throws IOException {
        RESTAnswer temp = new Register().processText(token, dev_id, email);
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
