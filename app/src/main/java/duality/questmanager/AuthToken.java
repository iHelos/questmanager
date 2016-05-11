package duality.questmanager;

import android.content.SharedPreferences;

/**
 * Created by olegermakov on 10.05.16.
 */
public class AuthToken {
    private static final String tokenPrefTitle = "AuthTokenBackend";
    public static void setToken(String token, SharedPreferences pref)
    {
        pref.edit().putString(tokenPrefTitle, token).apply();
    }

    public static String getToken(SharedPreferences pref)
    {
        return pref.getString(tokenPrefTitle, "none");
    }
}
