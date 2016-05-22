package duality.questmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.DisplayMetrics;

;import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;


/**
 * Created by olegermakov on 20.05.16.
 */
public class Preferences extends PreferenceFragmentCompat {

    public static final String FragmentTAG = "PreferenceTag";

    public static final String LANGUAGE_SETTING = "lang_setting";
    public static final String THEME_SETTING = "theme_setting";

    public static final int LANGUAGE_CHANGED = 1000;

    ListPreference language_preference;
    SwitchPreferenceCompat theme_preference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        PreferenceManager.setDefaultValues(getContext(), R.xml.preferences, false);

        //findPreference(getString(R.string.pref_key_language)).setOnPreferenceClickListener(this);
        language_preference = (ListPreference) findPreference(getString(R.string.pref_key_language));
        theme_preference = (SwitchPreferenceCompat) findPreference(getString(R.string.pref_key_theme));

        language_preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                final SharedPreferences settings = PreferenceManager
                        .getDefaultSharedPreferences(getActivity());
                final Map<String, String> localeMap = new HashMap<String, String>();
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = new Configuration(res.getConfiguration());
                conf.locale = new Locale((String) newValue);
                //Locale.setDefault(conf.locale);
                res.updateConfiguration(conf, dm);
                settings.edit()
                        .putString(Preferences.LANGUAGE_SETTING, (String) newValue).apply();

                refresh();
                return true;
            }
        });
        theme_preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                final SharedPreferences settings = PreferenceManager
                        .getDefaultSharedPreferences(getActivity());

                if((Boolean) o) {
                    settings.edit().putInt(THEME_SETTING, R.style.CustomTheme_Dark).apply();
                }
                else {
                    settings.edit().putInt(THEME_SETTING, R.style.CustomTheme).apply();
                }
                refresh();
                return true;
            }
        });
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

    public static void updateTheme(Activity act) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(act);
        int theme = sharedPreferences.getInt(
                Preferences.THEME_SETTING, R.style.CustomTheme);
        act.setTheme(theme);
    }


    public void refresh() {
        // Refresh the app
        Intent refresh = new Intent(getActivity(), getActivity()
                .getClass());
        refresh.putExtra(FragmentsActivity.START_FRAGMENT, FragmentTAG);
        startActivity(refresh);
        getActivity().setResult(Preferences.LANGUAGE_CHANGED);
        getActivity().finish();
    }
}