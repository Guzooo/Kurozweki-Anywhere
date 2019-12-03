package pl.Guzooo.KurozwekiAnywhere.Events;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

import pl.Guzooo.KurozwekiAnywhere.Database;
import pl.Guzooo.KurozwekiAnywhere.JsonObjects;

public class EventObject extends JsonObjects {

    public static final String TITLE = "EVENTS";
    public static final String NAME = "NAME";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String DATA = "DATA";
    public static final String TIME_START = "TIME_START";
    public static final String CONNECTED_WITH = "CONNECTED_WITH";
    public static final String TIME_END = "TIME_END";
    public static final String IMAGES_PAGE = "IMAGES_PAGE";
    public static final String IMAGES_ON_DEVICES = "IMAGES_ON_DEVICES";
    public static final String TAGS = "TAGS";
    public static final String PLACE_ID = "PLACE_ID";

    public static final String[] onCursor = new String[] {Database.ID, NAME, DESCRIPTION, DATA, TIME_START, CONNECTED_WITH, TIME_END, IMAGES_PAGE, IMAGES_ON_DEVICES, TAGS, PLACE_ID};

    private String name;
    private String description;
    private String data;
    private int timeStart;
    private String connectedWith;
    private int timeEnd;
    private String imagesPage;
    private String imagesOnDevices;
    private String tags;
    private int placeID;

    @Override
    public String[] onCursor(Context context) {
        return onCursor;
    }

    @Override
    public String databaseName() {
        return TITLE;
    }

    private void Template(int id, String name, String description, String data, String timeStart, String connectedWith, String timeEnd, String imagesPage, String imagesOnDevices, String tags, int placeID){
        setId(id);
        setName(name);
        setDescription(description);
        setData(data);
        setTimeStart(timeStart);
        setConnectedWith(connectedWith);
        setTimeEnd(timeEnd);
        setImagesPage(imagesPage);
        setImagesOnDevices(imagesOnDevices);
        setTags(tags);
        setPlaceID(placeID);
    }

    @Override
    public void SetOfJSON(JSONObject object) {
        try {
            Template(object.getInt(Database.ID),
                    object.getString(NAME),
                    object.getString(DESCRIPTION),
                    object.getString(DATA),
                    object.getString(TIME_START),
                    object.getString(CONNECTED_WITH),
                    object.getString(TIME_END),
                    object.getString(IMAGES_PAGE),
                    "",
                    object.getString(TAGS),
                    object.getInt(PLACE_ID));
        } catch (JSONException e) {
            e.printStackTrace();
            SetEmpty();
        }
    }

    @Override
    public void SetOfCursor(Cursor cursor) {
        Template(cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getString(8),
                cursor.getString(9),
                cursor.getInt(10));
    }

    @Override
    public void SetEmpty() {
        Template(0, "", "", "", "", "", "", "", "", "", 0);
    }

    @Override
    public ContentValues getContentValues(Context context) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.ID, getId());
        contentValues.put(NAME, getName());
        contentValues.put(DESCRIPTION, getDescription());
        contentValues.put(DATA, getData());
        contentValues.put(TIME_START, getTimeStart());
        contentValues.put(CONNECTED_WITH, getConnectedWith());
        contentValues.put(TIME_END, getTimeEnd());
        contentValues.put(IMAGES_PAGE, getImagesPage());
        contentValues.put(IMAGES_ON_DEVICES, getImagesOnDevices());
        contentValues.put(TAGS, getTags());
        contentValues.put(PLACE_ID, getPlaceID());
        return contentValues;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public String getData() {
        return data;
    }

    private void setData(String data) {
        this.data = data;
    }

    public int getTimeStart() {
        return timeStart;
    }

    private void setTimeStart(String timeStart){
        this.timeStart = 20;
    }

    private void setTimeStart(int timeStart) {
        this.timeStart = timeStart;
    }

    public String getConnectedWith() {
        return connectedWith;
    }

    private void setConnectedWith(String connectedWith) {
        this.connectedWith = connectedWith;
    }

    public int getTimeEnd() {
        return timeEnd;
    }

    private void setTimeEnd(String timeEnd){
        this.timeEnd = 20;
    }

    private void setTimeEnd(int timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getImagesPage() {
        return imagesPage;
    }

    private void setImagesPage(String imagesPage) {
        this.imagesPage = imagesPage;
    }

    public String getImagesOnDevices() {
        return imagesOnDevices;
    }

    private void setImagesOnDevices(String imagesOnDevices) {
        this.imagesOnDevices = imagesOnDevices;
    }

    public String getTags() {
        return tags;
    }

    private void setTags(String tags) {
        this.tags = tags;
    }

    public int getPlaceID() {
        return placeID;
    }

    private void setPlaceID(int placeID) {
        this.placeID = placeID;
    }
}
