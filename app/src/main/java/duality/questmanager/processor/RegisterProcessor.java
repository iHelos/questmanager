package duality.questmanager.processor;

import java.io.IOException;

import duality.questmanager.rest.Register;


/**
 * Created by olegermakov on 20.04.16.
 */
public class RegisterProcessor {
    private final static String LOG_TAG = RegisterProcessor.class.getSimpleName();

    public static String processSignUp(final String token,final String email) throws IOException {
        return new Register().processText(token, email);
    }
}
