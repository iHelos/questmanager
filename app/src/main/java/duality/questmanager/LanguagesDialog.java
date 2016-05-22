package duality.questmanager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by olegermakov on 21.05.16.
 */
public class LanguagesDialog extends DialogFragment {

//    public LanguagesDialog(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        // TODO Auto-generated constructor stub
//    }




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        final Map<String, String> localeMap = new HashMap<String, String>();
        String[] availableLocales = getResources().getStringArray(
                R.array.languages);
        final String[] values = new String[availableLocales.length + 1];

        for (int i = 0; i < availableLocales.length; ++i) {
            String localString = availableLocales[i];
            if (localString.contains("-")) {
                localString = localString.substring(0,
                        localString.indexOf("-"));
            }
            Locale locale = new Locale(localString);
            values[i + 1] = locale.getDisplayLanguage() + " ("
                    + availableLocales[i] + ")";
            localeMap.put(values[i + 1], availableLocales[i]);
        }
        values[0] = getActivity().getString(R.string.device) + " ("
                + Locale.getDefault().getLanguage() + ")";
        localeMap.put(values[0], Locale.getDefault().getLanguage());
        Arrays.sort(values, 1, values.length);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                getActivity());
        dialogBuilder.setTitle(getString(R.string.language));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_autocomplite, values);

        ListView listView = new ListView(getActivity());
//        ListView listView = (ListView) getActivity().findViewById(R.id.languages_list);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = new Configuration(res.getConfiguration());
                String localString = localeMap.get(values[position]);
                if (localString.contains("-")) {
                    conf.locale = new Locale(localString.substring(0,
                            localString.indexOf("-")), localString.substring(
                            localString.indexOf("-") + 1, localString.length()));

                } else {
                    conf.locale = new Locale(localString);
                }
                //Locale.setDefault(conf.locale);
                res.updateConfiguration(conf, dm);
                settings.edit()
                        .putString(Preferences.LANGUAGE_SETTING, localString).apply();

                // Refresh the app
                Intent refresh = new Intent(getActivity(), getActivity()
                        .getClass());
                startActivity(refresh);
                getActivity().setResult(Preferences.LANGUAGE_CHANGED);
                getActivity().finish();

            }
        });

        dialogBuilder.setView(listView);
        dialogBuilder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return dialogBuilder.create();
    }

}
