package duality.questmanager.processor;

import java.io.IOException;

import duality.questmanager.rest.RESTAnswer;
import duality.questmanager.rest.Register;


/**
 * Created by olegermakov on 20.04.16.
 */
public class RegisterProcessor {
    private final static String LOG_TAG = RegisterProcessor.class.getSimpleName();

    public static RESTAnswer processSignUp(final String token,final String email) throws IOException {
        RESTAnswer result = new Register().processText(token, email);
        return result;
    }
}
