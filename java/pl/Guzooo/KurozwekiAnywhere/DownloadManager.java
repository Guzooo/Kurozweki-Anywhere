package pl.Guzooo.KurozwekiAnywhere;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DownloadManager {

    private static final String PREFERENCES_NAME = "preferencedatabase";
    private static final String PREFERENCE_LAST_SYNC = "lastsync";

    private static final String LINK = "https://raw.githubusercontent.com/Guzooo/Info/master/Kurozweki%20Anywhere/";
    private static final String LANGUAGE = "en";
    private static final String DATABASES_VERSION = "/DATABASES_VERSION.txt";

    public static void Check(boolean automatic, final Context context) {
        String preferenceDownload = SettingsActivity.getDownloadInfo(context);
        int internetConnect = getInternetConnection(context);

        if (automatic && !canAutoDownload(preferenceDownload, internetConnect, context)) {
            Toast.makeText(context, "NIE SPRAWDZE BO ZAKAZANE", Toast.LENGTH_SHORT).show();
            return; //TODO: Auto failed;
        }

        if (internetConnect == INTERNET_DISCONNECT) {
            Toast.makeText(context, "BRAK INTERNETU, ZIOOOM", Toast.LENGTH_SHORT).show();
            return; //TODO:Brak internetu;
        }

        ReadJSON.ReadJSONMethod readJSONMethod = new ReadJSON.ReadJSONMethod() {
            @Override
            public void onPreRead() {
                Toast.makeText(context, "sprawdzam", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBackground(ArrayList<ArrayList<JSONObject>> objects) {
                for (int i = 0; i < objects.size(); i++) {
                    for (int ii = 0; ii < objects.get(i).size(); ii++) {
                        try {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(Database.DATABASES_VERSION_VERSION_ONLINE, objects.get(i).get(ii).getInt(Database.DATABASES_VERSION_VERSION_ONLINE));

                            SQLiteDatabase db = Database.getWrite(context);
                            db.update(Database.DATABASES_VERSION_TITLE, contentValues, Database.DATABASES_VERSION_DATABASE_NAME + " = ?", new String[]{objects.get(i).get(ii).getString(Database.DATABASES_VERSION_DATABASE_NAME)});
                            db.close();
                            Log.d("data", "poszło zapusane propably");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //TODO:zapis w bazie danych versji
                //TODO:sprawdzenie które wersje są nieaktualne w bazie danych
                //TO z oknami chyba trzeba w post read
                //wyświetlenie okna dialogowego czy chce zaktualizować konkretne bazy
                //array z bazami które użytkownik pozwolił updatnąć
                //ReadJSON wszystkich na raz

            }

            @Override
            public void onUpdate(Integer[] integers) {
                Toast.makeText(context, "a dam jakis update " + integers[0], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPostRead(boolean successful) {
                if (successful)
                    Toast.makeText(context, "uda się", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "błąd", Toast.LENGTH_SHORT).show();
                //Okna dzialogowe albo że nie dało rady
            }
        };
        ReadJSON readJSON = new ReadJSON(readJSONMethod);
        readJSON.execute(LINK + LANGUAGE + DATABASES_VERSION);
    }

    private static boolean canAutoDownload(String pref, int connect, Context context){
        if(pref.equals(context.getString(R.string.DOWNLOAD_INFO_NEVER)))
            return false;
        if(pref.equals(context.getString(R.string.DOWNLOAD_INFO_ONLY_WIFI)) && connect != INTERNET_WIFI)
            return false;
        return true;
    }

    private static void SavePreferenceLastSync(Context context){
        Database.getSharedPreferencesEditor(PREFERENCES_NAME, context)
                .putString(PREFERENCE_LAST_SYNC, UtilsCalendar.getTodayWithTime())
                .apply(); //Gdyby data nie zmieniała się po aktualizacji, trzeba rozważyć dodanie commit;
    }

    public static String getPreferenceLastSync(Context context){
        return Database.getSharedPreferences(PREFERENCES_NAME, context).getString(PREFERENCE_LAST_SYNC, context.getString(R.string.null_sync));
    }

    private static final int INTERNET_DISCONNECT = 0;
    private static final int INTERNET_CELLULAR = 1;
    private static final int INTERNET_WIFI = 2;

    private static int getInternetConnection(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if(capabilities == null)
                return INTERNET_DISCONNECT;
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                return INTERNET_WIFI;
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
                return INTERNET_CELLULAR;
        } else {
            NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo cellular = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if(wifi.isConnectedOrConnecting())
                return INTERNET_WIFI;
            if(cellular.isConnectedOrConnecting())
                return INTERNET_CELLULAR;
        }
        return INTERNET_DISCONNECT;
    }
}