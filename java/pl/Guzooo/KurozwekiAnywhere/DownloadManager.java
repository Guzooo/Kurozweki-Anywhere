package pl.Guzooo.KurozwekiAnywhere;

import org.json.JSONObject;

import java.util.ArrayList;

public class DownloadManager {

    private static final String VERSION = "";

    public static void Check(boolean automatic){
        if(automatic) {
        //TODO: sprawdzić ustawienia czy można pobierać
        }
        ReadJSON readJSON = new ReadJSON(new ReadJSON.ReadJSONMethod() {
            @Override
            public void onPreRead() {

            }

            @Override
            public void onBackground(ArrayList<ArrayList<JSONObject>> objects) {
                //TODO:zapis w bazie danych versji
                //TODO:sprawdzenie które wersje są nieaktualne w bazie danych
                //TO z oknami chyba trzeba w post read
                //wyświetlenie okna dialogowego czy chce zaktualizować konkretne bazy
                //array z bazami które użytkownik pozwolił updatnąć
                //ReadJSON wszystkich na raz
            }

            @Override
            public void onUpdate(Integer[] integers) {

            }

            @Override
            public void onPostRead(boolean successful) {
                //Okna dzialogowe albo że nie dało rady
            }
        });
        readJSON.execute(VERSION);
    }
}
