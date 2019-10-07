package pl.Guzooo.KurozwekiAnywhere;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private final String PHONE_NUMBER = "tel:123456789";
    private final String MAIL = "123@o2.pl";
    private final String G_MESSENGER = "https://www.messenger.com/t/GuzoooApps";

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;
    View navigationHeader;

    NavigationFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SetDrawerLayout();
        SetNavigationView();
        SetNavigationHeader();
        SetActionBar();


        //TODO: jak sie obraca, sprawdz czy fragment jest nulem
        //TODO:Pobieranie check
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if(currentFragment.isHome())
            super.onBackPressed();
        else
            onNavigationItemSelected(navigationView.getMenu().getItem(0).setChecked(true));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        NavigationFragment fragment = null;

        switch (menuItem.getItemId()){
            case R.id.home:
                fragment = new Home();
                break;
            case R.id.page1:
                fragment = new Page1();
                break;
            case R.id.page2:
                fragment = new Page2();
                break;
            case R.id.page3:
                fragment = new Page3();
                break;
            case R.id.call:
                ClickCall();
                break;
            case R.id.mail:
                ClickMail();
                break;
            case R.id.settings:
                ClickSettings();
                break;
            case R.id.report_error:
                ClickReportError();
                break;
        }

        if(fragment != null)
            ReplaceFragment(fragment);

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.news_feed:
                ClickNewsFeed();
                break;
            case R.id.sync:
                ClickSync();
                break;
        }
    }

    private void ClickCall(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(PHONE_NUMBER));
        Intent intentChose = Intent.createChooser(intent, "CALL");
        startActivity(intentChose);
    }

    private void ClickMail(){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.fromParts("mailto", MAIL, null));
        Intent intentChose = Intent.createChooser(intent, "SEND E-MAIL...");
        startActivity(intentChose);
    }

    private void ClickSettings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void ClickReportError(){
        Uri uri = Uri.parse(G_MESSENGER);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void ReplaceFragment(NavigationFragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); //TODO: zmiana
        transaction.commit();
        currentFragment = fragment;
        RefreshActionBar();
    }

    private void ClickNewsFeed(){
        //TODO: otwórz aktywność z aktualnościami
    }

    private void ClickSync(){
        //TODO:pobieranko
    }

    private void SetDrawerLayout(){
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_navigation_menu, R.string.close_navigation_menu){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
    }

    private void SetNavigationView(){
        navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        onNavigationItemSelected(navigationView.getMenu().getItem(0).setChecked(true));
    }

    private void SetNavigationHeader(){
        navigationHeader = navigationView.getHeaderView(0);
        navigationHeader.findViewById(R.id.news_feed).setOnClickListener(this);
        navigationHeader.findViewById(R.id.sync).setOnClickListener(this);
        SetHeaderVersion();
        SetHeaderLastDataSync();
    }

    private void SetHeaderVersion(){
        TextView version = navigationHeader.findViewById(R.id.version);
        //TODO znajdz aktualną wersje aplikacji dodaj na poczatku "v" i bedzie git
    }

    private void SetHeaderLastDataSync(){
        //TODO: z bazy danych bierze dane;
    }

    private void SetActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        RefreshActionBar();
    }

    private void RefreshActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(currentFragment.getActionBarTitle());
    }
}
