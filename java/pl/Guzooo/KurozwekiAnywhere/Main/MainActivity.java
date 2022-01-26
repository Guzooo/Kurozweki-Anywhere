package pl.Guzooo.KurozwekiAnywhere.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import pl.Guzooo.KurozwekiAnywhere.R;

public class MainActivity extends AppCompatActivity {

    private MainNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();
    }


    @Override
    public void onBackPressed() {
        if(navigationView.isLogosHeaderOpen())
            navigationView.closeLogosHeader();
        else
            super.onBackPressed();
    }

    private void initialization(){
        navigationView = new MainNavigationView();
        navigationView.initialization(this);
    }
}