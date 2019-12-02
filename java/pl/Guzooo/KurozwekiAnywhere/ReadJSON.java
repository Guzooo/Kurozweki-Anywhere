package pl.Guzooo.KurozwekiAnywhere;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;


public class ReadJSON extends AsyncTask<String, Integer, Boolean> {

    private ReadJSONMethod readJSONMethod;

    public interface ReadJSONMethod{
        void onPreRead();
        void onBackground(ArrayList<ArrayList<JSONObject>> objects);
        void onUpdate(Integer[] integers);
        void onPostRead(boolean successful);
    }

    public ReadJSON(ReadJSONMethod readJSONMethod) {
        super();
        this.readJSONMethod = readJSONMethod;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        readJSONMethod.onPreRead();
    }

    @Override
    protected void onPostExecute(Boolean bool) {
        super.onPostExecute(bool);
        readJSONMethod.onPostRead(bool);
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            ArrayList<ArrayList<JSONObject>> objects = new ArrayList<>();
            for (int i = 0; i < strings.length; i++){
                ArrayList<JSONObject> jsonObjects = new ArrayList<>();
                BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(strings[i]).openStream()));
                String line;
                while ((line = reader.readLine()) != null && !line.equals(""))
                    jsonObjects.add(new JSONObject(line));
                reader.close();
                objects.add(jsonObjects);
                publishProgress(i, strings.length);
            }
            readJSONMethod.onBackground(objects);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onProgressUpdate(Integer... integers) {
        super.onProgressUpdate(integers);
        readJSONMethod.onUpdate(integers);
    }
}
