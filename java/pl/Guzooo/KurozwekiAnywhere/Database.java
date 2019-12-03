package pl.Guzooo.KurozwekiAnywhere;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import pl.Guzooo.KurozwekiAnywhere.Events.EventObject;

public class Database extends SQLiteOpenHelper {

    private static final String DB_NAME = "kurozwekianywhere";
    private static final int DB_VERSION = 1;

    public static final String ID = "_id";

    public static final String DATABASES_VERSION_TITLE = "DATABASES_VERSION";
    public static final String DATABASES_VERSION_DATABASE_NAME = "DATABASE_NAME";
    public static final String DATABASES_VERSION_VERSION_ON_DEVICE = "VERSION_ON_DEVICES";
    public static final String DATABASES_VERSION_VERSION_ONLINE = "VERSION_ONLINE";

    public Database(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Update(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Update(db, oldVersion, newVersion);
    }

    private void Update(SQLiteDatabase db, int oldVersion, int newVersion){
        if(oldVersion < 1){
            db.execSQL("CREATE TABLE " + DATABASES_VERSION_TITLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                                    + DATABASES_VERSION_DATABASE_NAME + " TEXT,"
                                    + DATABASES_VERSION_VERSION_ON_DEVICE + " INTEGER,"
                                    + DATABASES_VERSION_VERSION_ONLINE + " INTEGER)");
            CompleteDatabasesVersion(db);
            db.execSQL("CREATE TABLE " + EventObject.TITLE + " (" + ID + " INTEGER PRIMARY KEY,"
                                    + EventObject.NAME + " TEXT,"
                                    + EventObject.DESCRIPTION + " TEXT,"
                                    + EventObject.DATA + " TEXT,"
                                    + EventObject.TIME_START + " INTEGER,"
                                    + EventObject.CONNECTED_WITH + " TEXT,"
                                    + EventObject.TIME_END + " INTEGER,"
                                    + EventObject.IMAGES_PAGE + " TEXT,"
                                    + EventObject.IMAGES_ON_DEVICES + " TEXT,"
                                    + EventObject.TAGS + " TEXT,"
                                    + EventObject.PLACE_ID + " INTEGER)");
        }
    }

    private void CompleteDatabasesVersion(SQLiteDatabase db){
        db.insert(DATABASES_VERSION_TITLE, null, DatabasesVersion(EventObject.TITLE));
    }

    private ContentValues DatabasesVersion(String name){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATABASES_VERSION_DATABASE_NAME, name);
        contentValues.put(DATABASES_VERSION_VERSION_ON_DEVICE, 0);
        contentValues.put(DATABASES_VERSION_VERSION_ONLINE, 0);
        return contentValues;
    }

    public static SQLiteDatabase getWrite(Context context){
        return new Database(context).getWritableDatabase();
    }

    public static SQLiteDatabase getRead(Context context){
        return new Database(context).getReadableDatabase();
    }

    public static void ErrorToast(Context context){
        Toast.makeText(context, R.string.error_database, Toast.LENGTH_SHORT).show();
    }

    //TODO: grupowe usuwanie każdej tabeli

    public static SharedPreferences getSharedPreferences(String name, Context context){
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getSharedPreferencesEditor(String name, Context context){
        return getSharedPreferences(name, context).edit();
    }
}
