package duality.questmanager.rest;

/**
 * Created by olegermakov on 09.05.16.
 */
public interface ResultListener {
    void onSuccess(final String result);
    void onFail(final String result);
}
