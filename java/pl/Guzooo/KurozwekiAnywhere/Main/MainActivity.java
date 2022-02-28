package pl.Guzooo.KurozwekiAnywhere.Main;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.core.view.OnApplyWindowInsetsListener;

import pl.Guzooo.Base.ModifiedElements.GActivity;
import pl.Guzooo.Base.Utils.FullScreenUtils;
import pl.Guzooo.Base.Utils.ThemeUtils;
import pl.Guzooo.KurozwekiAnywhere.R;

public class MainActivity extends GActivity {

    private MainNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.setTheme(this);
        setContentView(R.layout.activity_main);

        initialization();
        setFullScreen();
        loadInstanceState(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        navigationView.getDrawerToggle().syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        navigationView.getDrawerToggle().onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return navigationView.getDrawerToggle().onOptionsItemSelected(item)
                || super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {//TODO:zamykanie business cardsów i to samo w up navi
        if(navigationView.ifClosedSomethingOnBackPressed())
            return;//TODO: zamyka szuflade

        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        navigationView.saveInstanceStage(outState);
    }

    private void initialization(){
        navigationView = new MainNavigationView(this);
    }

    private void setFullScreen(){
        View navHostFragment = findViewById(R.id.nav_host_fragment);
        View navigationView = findViewById(R.id.navigation_view);
        OnApplyWindowInsetsListener onApplyWindowInsetsListener = getWindowsInsetsListener();
        FullScreenUtils.setUIVisibility(navHostFragment);
        FullScreenUtils.setApplyWindowInsets(navigationView, onApplyWindowInsetsListener);
        FullScreenUtils.setPaddings(navHostFragment, this);
    }

    private void loadInstanceState(Bundle bundle){
        if(bundle != null)
            navigationView.loadInstanceStage(bundle);
    }

    private OnApplyWindowInsetsListener getWindowsInsetsListener(){
        return (v, insets) -> {
            navigationView.setWindowsInsets(insets);
            return insets;
        };
    }
}