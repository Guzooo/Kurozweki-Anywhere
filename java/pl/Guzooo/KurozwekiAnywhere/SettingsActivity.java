package pl.Guzooo.KurozwekiAnywhere;

import android.content.Context;
import android.content.res.Configuration;
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
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            SetTheme();
            SetHardDarkTheme();
        }

        private void SetTheme(){
            ListPreference theme = findPreference("theme");
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
            final ListPreference theme = findPreference("theme");
            SwitchPreference hardDarkTheme = findPreference("harddark");
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

    public static String getTheme(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString("theme", "-1");
    }

    public static Boolean getHardDarkTheme(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("harddark", false);
    }
}