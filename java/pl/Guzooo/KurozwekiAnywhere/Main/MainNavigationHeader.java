package pl.Guzooo.KurozwekiAnywhere.Main;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.IntDef;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.WindowInsetsCompat;
import androidx.transition.TransitionManager;

import com.google.android.material.navigation.NavigationView;

import pl.Guzooo.Base.Elements.BusinessCard;
import pl.Guzooo.Base.Elements.ElementOfActivity;
import pl.Guzooo.Base.ModifiedElements.GActivity;
import pl.Guzooo.KurozwekiAnywhere.R;

public class MainNavigationHeader extends ElementOfActivity {

    @IntDef(value = {CLOSE, OPEN, OPEN_G, OPEN_STUD, OPEN_BREWERY, OPEN_PALACE})
    private @interface stateOfLogosHeader{}

    private static final int CLOSE = -1;
    private static final int OPEN = 0;
    private static final int OPEN_G = 1;
    private static final int OPEN_STUD = 2;
    private static final int OPEN_BREWERY = 3;
    private static final int OPEN_PALACE = 4;

    private final String BUNDLE_STAGE_OF_LOGOS_HEADER = "bundlestageoflogosheader";

    private ConstraintLayout header;
    private BusinessCard businessCardG;
    private BusinessCard businessCardStud;
    private BusinessCard businessCardBrewery;
    private BusinessCard businessCardPalace;
    private View logosHeaderTrigger;

    private int stateOfLogosHeader = CLOSE;

    public MainNavigationHeader(GActivity activity, View parent) {
        super(activity, parent);
        find(parent);
        setBusinessCards();
    }

    @Override
    public void setWindowsInsets(WindowInsetsCompat insets) {
        setWindowsInsetsOnHeader(insets);
        setWindowsInsetsOnBusinessCards(insets);
    }

    @Override
    public void loadInstanceStage(Bundle bundle) {
        stateOfLogosHeader = bundle.getInt(BUNDLE_STAGE_OF_LOGOS_HEADER, CLOSE);
        switch (stateOfLogosHeader){
            case CLOSE:
                return;
            case OPEN_G:
                businessCardG.open();
                break;
            case OPEN_STUD:
                businessCardStud.open();
                break;
            case OPEN_BREWERY:
                businessCardBrewery.open();
                break;
            case OPEN_PALACE:
                businessCardPalace.open();
                break;
        }
        openLogosHeader();
    }

    @Override
    public void saveInstanceStage(Bundle bundle) {
        bundle.putInt(BUNDLE_STAGE_OF_LOGOS_HEADER, stateOfLogosHeader);
    }

    @Override
    public boolean ifClosedSomethingOnBackPressed() {
        switch (stateOfLogosHeader){
            case OPEN:
                closeLogosHeader();
                return true;
            case OPEN_G:
                businessCardG.animClose();
                return true;
            case OPEN_STUD:
                businessCardStud.animClose();
                return true;
            case OPEN_BREWERY:
                businessCardBrewery.animClose();
                return true;
            case OPEN_PALACE:
                businessCardPalace.animClose();
                return true;
            default:
                return false;
        }
    }

    public void closeLogosHeader(){
        changeHeaderLayout(R.layout.main_navigation_header);
        logosHeaderTrigger.setVisibility(View.VISIBLE);
        stateOfLogosHeader = CLOSE;
    }

    public void closeLogos(){
        businessCardG.close();
        businessCardStud.close();
        businessCardBrewery.close();
        businessCardPalace.close();
    }

    private void find(View v){
        NavigationView navigationView = (NavigationView) v;
        header = (ConstraintLayout) navigationView.getHeaderView(0);
        businessCardG = header.findViewById(R.id.business_card_g);
        businessCardStud = header.findViewById(R.id.business_card_stud);
        businessCardBrewery = header.findViewById(R.id.business_card_brewery);
        businessCardPalace = header.findViewById(R.id.business_card_palace);
        logosHeaderTrigger = header.findViewById(R.id.logos_header_trigger);
    }

    private void setBusinessCards() {
        View logoG = header.findViewById(R.id.logo_g);
        View logoStud = header.findViewById(R.id.logo_stud);
        View logoBrewery = header.findViewById(R.id.logo_brewery);
        View logoPalace = header.findViewById(R.id.logo_palace);
        businessCardG.setOpenerView(logoG);
        businessCardStud.setOpenerView(logoStud);
        businessCardBrewery.setOpenerView(logoBrewery);
        businessCardPalace.setOpenerView(logoPalace);
        businessCardG.setInvisibleCloseDetailsIcon();
        businessCardStud.setInvisibleCloseDetailsIcon();
        businessCardBrewery.setInvisibleCloseDetailsIcon();
        businessCardPalace.setInvisibleCloseDetailsIcon();
        businessCardG.setVisibilityChangeListener(visible ->
                setStateOfLogosHeaderByBusinessCard(visible, OPEN_G));
        businessCardStud.setVisibilityChangeListener(visible ->
                setStateOfLogosHeaderByBusinessCard(visible, OPEN_STUD));
        businessCardBrewery.setVisibilityChangeListener(visible ->
                setStateOfLogosHeaderByBusinessCard(visible, OPEN_BREWERY));
        businessCardPalace.setVisibilityChangeListener(visible ->
                setStateOfLogosHeaderByBusinessCard(visible, OPEN_PALACE));
        logosHeaderTrigger.setOnClickListener(view -> openLogosHeader());
    }

    private void setWindowsInsetsOnHeader(WindowInsetsCompat insets){
        View paddingTop = header.findViewById(R.id.padding_top);
        View paddingStart = header.findViewById(R.id.padding_start);
        paddingTop.setMinimumHeight(insets.getSystemWindowInsetTop());
        paddingStart.setMinimumWidth(insets.getSystemWindowInsetLeft());
    }

    private void setWindowsInsetsOnBusinessCards(WindowInsetsCompat insets){
        businessCardG.getChildAt(0).setPadding(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(), 0,0);
        businessCardStud.getChildAt(0).setPadding(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(), 0,0);
        businessCardBrewery.getChildAt(0).setPadding(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(), 0,0);
        businessCardPalace.getChildAt(0).setPadding(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(), 0,0);
    }

    private void openLogosHeader(){
        changeHeaderLayout(R.layout.main_navigation_logos_header);
        logosHeaderTrigger.setVisibility(View.INVISIBLE);
        if(stateOfLogosHeader == CLOSE)
            stateOfLogosHeader = OPEN;
    }

    private void changeHeaderLayout(int layout){
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.load(getActivity(), layout);
        TransitionManager.beginDelayedTransition(header);
        constraintSet.applyTo(header);
    }

    private void setStateOfLogosHeaderByBusinessCard(boolean visibility, @stateOfLogosHeader int businessCardType){
        stateOfLogosHeader = (visibility) ? businessCardType : OPEN;
    }
}