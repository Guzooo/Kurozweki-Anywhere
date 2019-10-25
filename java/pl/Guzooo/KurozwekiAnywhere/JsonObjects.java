package pl.Guzooo.KurozwekiAnywhere;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import org.json.JSONObject;

public abstract class JsonObjects {
    private int id;

    public abstract String[] onCursor(Context context);
    public abstract String databaseName();

    public JsonObjects(){
        SetEmpty();
    }

    public abstract void SetOfJSON(JSONObject object);

    public abstract void SetOfCursor(Cursor cursor);

    public void SetOfId(int id, Context context){
        try {
            SQLiteDatabase db = Database.getRead(context);
            Cursor cursor = db.query(databaseName(),
                    onCursor(context),
                    "_id = ?",
                    new String[]{Integer.toString(id)},
                    null, null, null);

            if (cursor.moveToFirst())
                SetOfCursor(cursor);
            else
                SetEmpty();

            cursor.close();
            db.close();
        } catch (SQLiteException e){
            Database.ErrorToast(context);
        }
    }

    public abstract void SetEmpty();

    public void Insert(Context context){
        try{
            SQLiteDatabase db = Database.getWrite(context);
            db.insert(databaseName(), null, getContentValues(context));
            db.close();
        } catch (SQLiteException e){
            Database.ErrorToast(context);
        }
    }

    public void Update(Context context){
        try {
            SQLiteDatabase db = Database.getWrite(context);
            db.update(databaseName(), getContentValues(context), "_id = ?", new String[]{Integer.toString(getId())});
            db.close();
        } catch (SQLiteException e){
            Database.ErrorToast(context);
        }
    }

    public void Delete(Context context){
        try {
            SQLiteDatabase db = Database.getRead(context);
            db.delete(databaseName(), "_id = ?", new String[]{Integer.toString(getId())});
            db.close();
        } catch (SQLiteException e){
            Database.ErrorToast(context);
        }
    }

    public abstract ContentValues getContentValues(Context context);

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }
}
