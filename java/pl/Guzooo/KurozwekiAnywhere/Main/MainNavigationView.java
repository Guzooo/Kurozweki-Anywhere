package pl.Guzooo.KurozwekiAnywhere.Main;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import pl.Guzooo.Base.Elements.ElementOfActivity;
import pl.Guzooo.Base.ModifiedElements.GActivity;
import pl.Guzooo.Base.Utils.ActionFromItemMenuUtils;
import pl.Guzooo.KurozwekiAnywhere.R;

public class MainNavigationView extends ElementOfActivity {

    ActionBarDrawerToggle drawerToggle;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MainNavigationHeader header;

    public MainNavigationView(GActivity activity) {
        super(activity);
        find();
        setActionBar();
        setDrawerLayout();
        setNavigationController();
    }

    //TODO: jak szuflada sie zamknie pozamykać logos header
    @Override
    public void setWindowsInsets(WindowInsetsCompat insets){
        header.setWindowsInsets(insets);
        setWindowsInsetsOnNavigationView(insets);
    }

    @Override
    public void loadInstanceStage(Bundle bundle) {
        header.loadInstanceStage(bundle);
    }

    @Override
    public void saveInstanceStage(Bundle bundle) {
        header.saveInstanceStage(bundle);
    }

    @Override
    public boolean ifClosedSomethingOnBackPressed() {
        if(header.ifClosedSomethingOnBackPressed())
            return true;
        if(drawerLayout.isDrawerOpen(navigationView))
            drawerLayout.closeDrawer(navigationView);
        return false;
    }

    public ActionBarDrawerToggle getDrawerToggle(){
        return drawerToggle;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        return ActionFromItemMenuUtils.doAction(item, getActivity());
    }

    private void find(){
        navigationView = getActivity().findViewById(R.id.navigation_view);
        header = new MainNavigationHeader(getActivity(), navigationView);
    }

    private void setActionBar(){
        ActionBar actionBar = getActivity().getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
    }

    private void setDrawerLayout(){
        drawerLayout = getActivity().findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                Log.d("MainNavView", "onOptionItemSelected --> " + item.getItemId());
                /*if(header.ifClosedSomethingOnBackPressed())
                    return true;*/
                return super.onOptionsItemSelected(item);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                header.closeLogosHeader();
            }

        };
        drawerLayout.addDrawerListener(drawerToggle);
        //navigationView.setNavigationItemSelectedListener(item -> );
    }

    private void setNavigationController(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder().build();

        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupActionBarWithNavController(getActivity(), navController, appBarConfiguration);

        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> drawerToggle.syncState());
    }

    private void setWindowsInsetsOnNavigationView(WindowInsetsCompat insets){
        navigationView.getChildAt(0).setPadding(0, 0, 0, insets.getSystemWindowInsetBottom());
        navigationView.setItemHorizontalPadding(navigationView.getItemHorizontalPadding() + insets.getSystemWindowInsetLeft());
        navigationView.setSubheaderInsetStart(navigationView.getSubheaderInsetStart() + insets.getSystemWindowInsetLeft());

        ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = getNavigationViewWidthOnGlobalLayoutListener(insets);
        ViewTreeObserver viewTreeObserver = navigationView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    private ViewTreeObserver.OnGlobalLayoutListener getNavigationViewWidthOnGlobalLayoutListener(WindowInsetsCompat insets){
        return new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                DrawerLayout.LayoutParams layoutParams = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
                layoutParams.width = navigationView.getWidth() + insets.getSystemWindowInsetLeft();
                navigationView.setLayoutParams(layoutParams);
                navigationView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        };
    }
}