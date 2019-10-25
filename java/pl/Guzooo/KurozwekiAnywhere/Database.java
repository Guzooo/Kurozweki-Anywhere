package pl.Guzooo.KurozwekiAnywhere;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class Database extends SQLiteOpenHelper {

    private static final String DB_NAME = "kurozwekianywhere"; //TODO:zmiana nazwy
    private static final int DB_VERSION = 1;

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
            //TODO:twórz tabele
        }
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
}
