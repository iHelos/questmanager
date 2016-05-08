package duality.questmanager.processor;

import java.io.IOException;

import duality.questmanager.rest.Register;
import okhttp3.Response;


/**
 * Created by olegermakov on 20.04.16.
 */
public class RegisterProcessor {
    private final static String LOG_TAG = RegisterProcessor.class.getSimpleName();

    public static String processSignUp(final String token,final String email) throws IOException {
        Response answer = new Register().processText(token, email);
        if (answer.code() == 200) {
            return answer.body().string();
        }
        else
        {
            return "OMGWTFBBQ";
        }
    }
}
