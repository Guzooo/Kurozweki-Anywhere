package pl.Guzooo.KurozwekiAnywhere;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pl.Guzooo.KurozwekiAnywhere.Events.EventObject;

public class DownloadManager {

    private static final String PREFERENCES_NAME = "preferencedatabase";
    private static final String PREFERENCE_LAST_SYNC = "lastsync";

    private static final String SLASH = "/";
    private static final String FILTES_EXTENDENTS = ".txt";

    private static final String LINK = "https://raw.githubusercontent.com/Guzooo/Info/master/Kurozweki%20Anywhere/";
    private static final String LANGUAGE = "en";
    private static final String DATABASES_VERSION = "/DATABASES_VERSION.txt";

    public interface DownloadEffects{
        void Start();
        void End(boolean successful);
        void IsUpdate();
        void NoInternet();
    }

    public static void Check(final boolean automatic, final DownloadEffects effects, final Context context) {
        String preferenceDownloadInfo = SettingsActivity.getDownloadInfo(context);
        final int internetConnect = getInternetConnection(context);

        if (automatic && !canAutoDownloadInfo(preferenceDownloadInfo, internetConnect, context)) {
            return;
        }

        if (internetConnect == INTERNET_DISCONNECT) {
            effects.NoInternet();
            return;
        }

        ReadJSON.ReadJSONMethod readJSONMethod = new ReadJSON.ReadJSONMethod() {

            @Override
            public void onPreRead() {
                effects.Start();
            }

            boolean autoDownloadDatabases;
            @Override
            public boolean onBackground(ArrayList<ArrayList<JSONObject>> objects) {
                SQLiteDatabase db = Database.getWrite(context);
                if(!SaveInfo(db, objects.get(0)))
                    return false;
                Cursor cursor = getCursor(db);
                SetNamesAndLinks(cursor);
                cursor.close();
                db.close();
                String preferenceDownloadDatabases = SettingsActivity.getDownloadDatabase(context);
                autoDownloadDatabases = canAutoDownloadDatabases(preferenceDownloadDatabases, internetConnect, context);
                if(autoDownloadDatabases && names.length != 0)
                    Positive(getBooleans());
                return true;
            }

            private boolean SaveInfo(SQLiteDatabase db, ArrayList<JSONObject> objects){
                try {
                    for (int i = 0; i < objects.size(); i++) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(Database.DATABASES_VERSION_VERSION_ONLINE, objects.get(i).getInt(Database.DATABASES_VERSION_VERSION_ONLINE));
                        db.update(Database.DATABASES_VERSION_TITLE, contentValues, Database.DATABASES_VERSION_DATABASE_NAME + " = ?", new String[]{objects.get(i).getString(Database.DATABASES_VERSION_DATABASE_NAME)});
                    }
                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            private Cursor getCursor(SQLiteDatabase db){
                return db.query(Database.DATABASES_VERSION_TITLE,
                        new String[]{Database.DATABASES_VERSION_DATABASE_NAME},
                        Database.DATABASES_VERSION_VERSION_ON_DEVICE + " != " + Database.DATABASES_VERSION_VERSION_ONLINE,
                        null, null, null, null);
            }

            String[] names;
            String[] links;
            private void SetNamesAndLinks(Cursor cursor){
                names = new String[cursor.getCount()];
                links = new String[cursor.getCount()];
                for(int i = 0; i < cursor.getCount(); i++) {
                    if(cursor.moveToPosition(i)) {
                        String name = cursor.getString(0);
                        names[i] = TranslateDatabaseName(name);
                        links[i] = SLASH + name + FILTES_EXTENDENTS;
                    }
                }
            }

            private String TranslateDatabaseName(String name){ //TODO:kolejne bazy danych
                switch (name){
                    case EventObject.TITLE: //TODO:brać z object events :))
                        return context.getString(R.string.database_events);
                }
                return context.getString(R.string.error_database);
            }

            @Override
            public void onUpdate(Integer[] integers) {

            }

            @Override
            public void onPostRead(boolean successful) {
                if (successful && !autoDownloadDatabases && names.length != 0) {
                    final boolean[] booleans = getBooleans();
                    new AlertDialog.Builder(context)
                            .setTitle(context.getString(R.string.update_database_now))
                            .setMultiChoiceItems(names, booleans, null)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Positive(booleans);//TODO: po zaaktualizawaniu all End;
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Negative();
                                }
                            })
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    Negative();
                                }
                            })
                            .create()
                            .show();
                } else {
                    effects.End(successful);
                }
            }

            private boolean[] getBooleans(){
                boolean[] booleans = new boolean[names.length];
                for(int i = 0; i < names.length; i++)
                    booleans[i] = true;
                return booleans;
            }

            ArrayList<JsonObjects> modelObjects = new ArrayList<>();
            private void Positive(boolean[] b){
                String[] strings = new String[getTrueLinksLength(b)];
                for (int i = 0; i < b.length; i++) {
                    if (b[i]) {
                        strings[i] = LINK + LANGUAGE + links[i];
                        modelObjects.add(getJSONObject(links[i]));
                    }
                }
                ReadJSON readJSON = new ReadJSON(getMethodDownloadDatabase());
                readJSON.execute(strings);
            }

            private int getTrueLinksLength(boolean[] b){
                int length = 0;
                for(boolean bool : b)
                    if (bool)
                        length++;
                return length;
            }

            private JsonObjects getJSONObject(String name){  //TODO: kolejne bazy danych
                name = name.replace(SLASH, "");
                name = name.replace(FILTES_EXTENDENTS, "");
                switch (name){
                    case EventObject.TITLE: //TODO:brać z obiektu
                        return new EventObject();
                }
                return null;
            }

            private void Negative(){
                effects.End(true);
                effects.IsUpdate();
            }

            private ReadJSON.ReadJSONMethod getMethodDownloadDatabase(){
                return new ReadJSON.ReadJSONMethod() {
                    @Override
                    public void onPreRead() {

                    }

                    @Override
                    public boolean onBackground(ArrayList<ArrayList<JSONObject>> objects) {
                        SQLiteDatabase db = Database.getWrite(context);
                        for(int i = 0; i < objects.size(); i++){
                            String databaseName = modelObjects.get(i).databaseName();
                            db.delete(databaseName, null, null);
                            for(int ii = 0; ii < objects.get(i).size(); ii++){
                                modelObjects.get(i).SetOfJSON(objects.get(i).get(ii));
                                modelObjects.get(i).Insert(context);
                            }
                            db.update(Database.DATABASES_VERSION_TITLE, getUpdateDatabaseVersion(db, databaseName), Database.DATABASES_VERSION_DATABASE_NAME + " = ?", new String[] {databaseName});
                        }
                        db.close();
                        return true;
                        //TODO:jak cała baza zaaktualizowana dawaj save dzisiaj;
                    }

                    private ContentValues getUpdateDatabaseVersion(SQLiteDatabase db, String databaseName){
                        int databaseVersion = 0;
                        Cursor cursor = db.query(Database.DATABASES_VERSION_TITLE,
                                new String[]{Database.DATABASES_VERSION_VERSION_ONLINE},
                                Database.DATABASES_VERSION_DATABASE_NAME + " = ?",
                                new String[]{databaseName},null, null, null);
                        if(cursor.moveToFirst())
                            databaseVersion = cursor.getInt(0);
                        cursor.close();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(Database.DATABASES_VERSION_VERSION_ON_DEVICE, databaseVersion);
                        return contentValues;
                    }

                    @Override
                    public void onUpdate(Integer[] integers) {
                        //TODO: maybe postep
                    }

                    @Override
                    public void onPostRead(boolean successful) {
                        effects.End(successful);
                    }
                };
            }
        };
        ReadJSON readJSON = new ReadJSON(readJSONMethod);
        readJSON.execute(LINK + LANGUAGE + DATABASES_VERSION);
    }

    private static boolean canAutoDownloadInfo(String pref, int connect, Context context){
        if(pref.equals(context.getString(R.string.DOWNLOAD_INFO_NEVER)))
            return false;
        if(pref.equals(context.getString(R.string.DOWNLOAD_INFO_ONLY_WIFI)) && connect != INTERNET_WIFI)
            return false;
        return true;
    }

    private static boolean canAutoDownloadDatabases(String pref, int connect, Context context){
        if(pref.equals(context.getString(R.string.DOWNLOAD_DATABASE_MANUAL)))
            return false;
        if(pref.equals(context.getString(R.string.DOWNLOAD_DATABASE_AUTO_ONLY_WIFI)) && connect != INTERNET_WIFI)
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