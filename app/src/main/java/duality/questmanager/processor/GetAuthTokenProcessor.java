package duality.questmanager.processor;

import java.io.IOException;

import duality.questmanager.rest.GetAuthToken;
import duality.questmanager.rest.RESTAnswer;

/**
 * Created by olegermakov on 09.05.16.
 */
public class GetAuthTokenProcessor {

    private final static String LOG_TAG = GetAuthTokenProcessor.class.getSimpleName();

    public static RESTAnswer getToken(final String token) throws IOException {
        return new GetAuthToken().start(token);
    }

}
