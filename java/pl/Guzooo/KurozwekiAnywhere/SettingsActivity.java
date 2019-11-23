package pl.Guzooo.KurozwekiAnywhere;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

public class SettingsActivity extends GActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private  <T extends Preference> T findPref(int id){
            return findPreference(getString(id));
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            SetTheme();
            SetHardDarkTheme();
        }

        private void SetTheme(){
            ListPreference theme = findPref(R.string.THEME_ID);
            if(theme != null) {
                if (theme.getValue() == null) {
                    theme.setValueIndex(2);
                }

                theme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        AppCompatDelegate.setDefaultNightMode(Integer.valueOf(newValue.toString()));
                        return true;
                    }
                });
            }
        }

        private void SetHardDarkTheme(){
            final ListPreference theme = findPref(R.string.THEME_ID);
            SwitchPreference hardDarkTheme = findPref(R.string.HARD_DARK_ID);
            if(hardDarkTheme != null && theme != null){
                hardDarkTheme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        int currentNightMode = getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                        if(currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                            getActivity().recreate();
                        }
                        return true;
                    }
                });
            }
        }
    }

    public static SharedPreferences getPref(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static int getTheme(Context context){
        int theme = Integer.valueOf(getPref(context).getString(context.getString(R.string.THEME_ID), "-1"));
        if(theme == -1 && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
            theme = 3;
        return theme;
    }

    public static Boolean getHardDarkTheme(Context context){
        return getPref(context).getBoolean(context.getString(R.string.HARD_DARK_ID), false);
    }

    public static String getDownloadInfo(Context context){
        return getPref(context).getString(context.getString(R.string.DOWNLOAD_INFO_ID), "0");
    }

    public static String getDownloadDatabase(Context context){
        return getPref(context).getString(context.getString(R.string.DOWNLOAD_DATABASE_ID), "0");
    }

    public static String getDownloadImages(Context context){
        return getPref(context).getString(context.getString(R.string.DOWNLOAD_IMAGES_ID), "0");
    }
}