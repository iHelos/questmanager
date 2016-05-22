package duality.questmanager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.DisplayMetrics;

;import java.util.Locale;


/**
 * Created by olegermakov on 20.05.16.
 */
public class Preferences extends PreferenceFragmentCompat implements
        Preference.OnPreferenceClickListener {


    public static final String LANGUAGE_SETTING = "lang_setting";
    public static final int LANGUAGE_CHANGED = 1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        findPreference(getString(R.string.pref_key_language)).setOnPreferenceClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle(R.string.menu_settings);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

    }

    public static PreferenceFragmentCompat newInstance() {
        PreferenceFragmentCompat myFragment = new Preferences();
        return myFragment;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String preferenceKey = preference != null ? preference.getKey() : "";

        if (preferenceKey.equals(getString(R.string.pref_key_language))) {
            return handleLanguagePreferenceClick();
        }

        return false;
    }
    private boolean handleLanguagePreferenceClick() {
        LanguagesDialog languagesDialog = new LanguagesDialog();
        languagesDialog.show(getFragmentManager(), "LanguagesDialogFragment");
        return true;
    }

    public static void updateLocaleIfNeeded(Activity act) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(act);

        if (sharedPreferences.contains(Preferences.LANGUAGE_SETTING)) {
            //String locale  = "en";
            String locale = sharedPreferences.getString(
                    Preferences.LANGUAGE_SETTING, "");
            Locale localeSetting = new Locale(locale);
            Locale.setDefault(localeSetting);
//            if (!localeSetting.equals(Locale.getDefault())) {
                Resources res = act.getResources();
                Configuration conf = new Configuration(res.getConfiguration());
                conf.locale = localeSetting;
                DisplayMetrics dm = res.getDisplayMetrics();
                res.updateConfiguration(conf, dm);
//            }
        }
    }
}