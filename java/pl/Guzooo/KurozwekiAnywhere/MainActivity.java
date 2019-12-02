package pl.Guzooo.KurozwekiAnywhere;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends GActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private final String PHONE_NUMBER = "tel:123456789";
    private final String MAIL = "123@o2.pl";
    private final String FACEBOOK_G = "https://www.facebook.com/GuzoooApps";
    private final String MESSENGER_G = "https://www.messenger.com/t/GuzoooApps";
    private final String FACEBOOK_PRINCIPAL = "https://www.facebook.com/Pałac-w-Kurozwękach-Kraina-bizonów-299669121906";

    private final String BUNDLE_SOCIAL_HEADER = "socialheader";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private View navigationHeader;

    private View headerSync;
    private boolean headerSyncOtherImage;

    private View socialHeader = null;
    int[] socialHeaderRoundCenter = new int[2];

    private NavigationFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetTheme();
        setContentView(R.layout.activity_main);

        Initial();
        LoadInstanceState(savedInstanceState);
        SetFragment();
        SetDrawerLayout();
        SetNavigationView();
        SetNavigationHeader();
        SetActionBar();

        //TODO:Pobieranie check
    }

    private void SetTheme(){
        AppCompatDelegate.setDefaultNightMode(SettingsActivity.getTheme(this));
    }

    private void Initial(){
        navigationView = findViewById(R.id.navigation);
        navigationHeader = navigationView.getHeaderView(0);
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

    private void LoadInstanceState(Bundle save){
        if(save != null) {
            int socialHeaderId = save.getInt(BUNDLE_SOCIAL_HEADER, 0);
            socialHeader = navigationHeader.findViewById(socialHeaderId);
            if(socialHeader != null)
                socialHeader.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(canHideSocialHeader(false))
            outState.putInt(BUNDLE_SOCIAL_HEADER, socialHeader.getId());
    }

    @Override
    public void onBackPressed() {
        if(isChangeVisibilitySocialHeader(false, false))
            return;
        else if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else if(!currentFragment.isHome())
            onNavigationItemSelected(navigationView.getMenu().getItem(0).setChecked(true));
        else
            super.onBackPressed();
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
            case R.id.socials:
                ClickSocials();
                return true;
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
            case R.id.logo_g:
                isChangeVisibilitySocialHeader(v, navigationHeader.findViewById(R.id.header_g));
                break;
            case R.id.logo_principal:
                isChangeVisibilitySocialHeader(v, navigationHeader.findViewById(R.id.header_principal));
                break;
            case R.id.news_feed:
                ClickNewsFeed();
                break;
            case R.id.sync:
                ClickSync();
                break;
            case R.id.facebook_g:
                ClickFacebookG();
                break;
            case R.id.messenger_g:
                ClickMessengerG();
                break;
            case R.id.facebook_principal:
                ClickFacebookPrincipal();
                break;
        }
    }

    private void ClickCall(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(PHONE_NUMBER));
        Intent intentChose = Intent.createChooser(intent, getString(R.string.intent_call));
        startActivity(intentChose);
    }

    private void ClickMail(){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.fromParts("mailto", MAIL, null));
        Intent intentChose = Intent.createChooser(intent, getString(R.string.intent_email));
        startActivity(intentChose);
    }

    private void ClickSocials(){
        if(canShowSocialHeader(true))
            isChangeVisibilitySocialHeader(navigationHeader.findViewById(R.id.logo_principal), navigationHeader.findViewById(R.id.header_principal));
        else {
            ScrollAndVisibilitySocialHeader(true);
        }
    }

    private void ClickSettings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void ClickReportError(){
        Uri uri = Uri.parse(MESSENGER_G);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void ReplaceFragment(NavigationFragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN); //TODO: zmiana FADE
        transaction.commit();
        currentFragment = fragment;
        RefreshActionBar();
    }

    private void ClickNewsFeed(){
        //TODO: otwórz aktywność z aktualnościami
    }

    private void ClickSync(){
        //TODO:pobieranko
        DownloadManager.DownloadEffects effects = new DownloadManager.DownloadEffects() {
            ObjectAnimator infinityRotate;
            @Override
            public void Start() {
                if(headerSyncOtherImage) {
                    Animations.ChangeIcon((ImageView) headerSync, R.drawable.sync);
                    headerSyncOtherImage = false;
                }
                infinityRotate = Animations.StartSpinIcon(headerSync);
            }

            @Override
            public void End(boolean successful) {
                infinityRotate.setRepeatCount(0);
                if(!successful) {
                    Animations.ChangeIcon((ImageView) headerSync, R.drawable.sync_problem);
                    headerSyncOtherImage = true;
                }
            }

            @Override
            public void NoInternet() {
                Animations.ErrorSpinIcon(headerSync);
            }
        };
        DownloadManager.Check(false, effects, this);
    }

    private void ClickFacebookG(){
        OpenPage(FACEBOOK_G);
    }

    private void ClickMessengerG(){
        OpenPage(MESSENGER_G);
    }

    private void ClickFacebookPrincipal(){
        OpenPage(FACEBOOK_PRINCIPAL);
    }

    private void OpenPage(String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void SetFragment(){
        NavigationFragment fragment = (NavigationFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if(fragment != null)
            ReplaceFragment(fragment);
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
                isChangeVisibilitySocialHeader(false, true);
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case android.R.id.home:
                        if(isChangeVisibilitySocialHeader(false, false))
                            return true;
                        else
                            return super.onOptionsItemSelected(item);
                    default:
                        return super.onOptionsItemSelected(item);
                }
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
    }

    private void SetNavigationView(){
        navigationView.setNavigationItemSelectedListener(this);
        if(currentFragment == null)
            onNavigationItemSelected(navigationView.getMenu().getItem(0).setChecked(true));
    }

    private void SetNavigationHeader(){
        headerSync = navigationHeader.findViewById(R.id.sync);
        navigationHeader.findViewById(R.id.logo_g).setOnClickListener(this);
        navigationHeader.findViewById(R.id.logo_principal).setOnClickListener(this);
        navigationHeader.findViewById(R.id.news_feed).setOnClickListener(this);
        headerSync.setOnClickListener(this);
        navigationHeader.findViewById(R.id.facebook_g).setOnClickListener(this);
        navigationHeader.findViewById(R.id.messenger_g).setOnClickListener(this);
        navigationHeader.findViewById(R.id.facebook_principal).setOnClickListener(this);
        SetHeaderVersion();
        SetHeaderLastDataSync();
    }

    private void SetHeaderVersion(){
        TextView version = navigationHeader.findViewById(R.id.version);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            version.setText("v" + info.versionName);
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
    }

    private void SetHeaderLastDataSync(){
        TextView lastSync = navigationHeader.findViewById(R.id.last_data_sync);
        lastSync.setText(DownloadManager.getPreferenceLastSync(this));
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

    private boolean isChangeVisibilitySocialHeader(View center, View header){
        center.getLocationOnScreen(socialHeaderRoundCenter);
        socialHeaderRoundCenter[0] += center.getWidth()/2;
        socialHeaderRoundCenter[1] += center.getHeight()/2;
        socialHeader = header;
        return isChangeVisibilitySocialHeader(true, true);
    }

    private boolean isChangeVisibilitySocialHeader(boolean show, boolean scroll){
        if(socialHeader == null)
            return false;
        if(!scroll && !ViewCompat.isAttachedToWindow(socialHeader))
            return false;
        if(scroll)
            return ScrollAndVisibilitySocialHeader(show);
        return VisibilitySocialHeader(show);
    }

    private boolean ScrollAndVisibilitySocialHeader(final boolean show){
        RecyclerView recyclerView = (RecyclerView) navigationView.getChildAt(0);
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        LinearSmoothScroller smoothScroller = new LinearSmoothScroller(this){

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return 1;
            }

            @Override
            protected void onStop() {
                super.onStop();
                VisibilitySocialHeader(show);
            }
        };
        smoothScroller.setTargetPosition(0);
        manager.startSmoothScroll(smoothScroller);

        if(canShowSocialHeader(show) || canHideSocialHeader(show))
            return true;
        return false;
    }

    private boolean VisibilitySocialHeader(boolean show){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int x = socialHeader.getWidth();
            int y = socialHeader.getHeight();
            float radius = (float) Math.hypot(x, y);

            if (canShowSocialHeader(show)) {
                Animations.ShowCircle(socialHeader, socialHeaderRoundCenter[0], socialHeaderRoundCenter[1], radius);
                return true;
            } else if (canHideSocialHeader(show)) {
                Animations.HideCircle(socialHeader, socialHeaderRoundCenter[0], socialHeaderRoundCenter[1], radius);
                return true;
            }
        } else {
            if (canShowSocialHeader(show)) {
                socialHeader.setVisibility(View.VISIBLE);
                return true;
            } else if (canHideSocialHeader(show)) {
                socialHeader.setVisibility(View.INVISIBLE);
                return true;
            }
        }
        return false;
    }

    private boolean canShowSocialHeader(boolean show){
        return (socialHeader == null || (socialHeader.getVisibility() != View.VISIBLE && show));
    }

    private boolean canHideSocialHeader(boolean show){
        return (socialHeader != null && socialHeader.getVisibility() == View.VISIBLE && !show);
    }
}