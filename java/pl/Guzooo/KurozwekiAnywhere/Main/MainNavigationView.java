package pl.Guzooo.KurozwekiAnywhere.Main;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.transition.TransitionManager;

import com.google.android.material.navigation.NavigationView;

import pl.Guzooo.Base.Elements.BusinessCard;
import pl.Guzooo.KurozwekiAnywhere.R;

public class MainNavigationView {

    private Context context;
    private ConstraintLayout header;
    private View logosHeaderTrigger;
    private boolean logosHeader = false;

    public void initialization(Activity activity){
        context = activity;
        find(activity);
        setBusinessCards();
    }

    public void openLogosHeader(){
        if(isLogosHeaderOpen())
            return;
        changeHeaderLayout(R.layout.navigation_header_main_logos);
        logosHeaderTrigger.setVisibility(View.INVISIBLE);
        logosHeader = true;
    }

    public void closeLogosHeader(){
        if(!isLogosHeaderOpen())
            return;
        changeHeaderLayout(R.layout.navigation_header_main);
        logosHeaderTrigger.setVisibility(View.VISIBLE);
        logosHeader = false;
    }

    public boolean isLogosHeaderOpen(){
        return logosHeader;
    }

    private void find(Activity activity){
        NavigationView navigationView = activity.findViewById(R.id.navigation_view);
        header = (ConstraintLayout) navigationView.getHeaderView(0);
        logosHeaderTrigger = header.findViewById(R.id.logos_header_trigger);
    }

    private void setBusinessCards() {
        View logoG = header.findViewById(R.id.logo_g);
        View logoStud = header.findViewById(R.id.logo_stud);
        View logoBrewery = header.findViewById(R.id.logo_brewery);
        View logoPalace = header.findViewById(R.id.logo_palace);
        BusinessCard businessCardG = header.findViewById(R.id.business_card_g);
        BusinessCard businessCardStud = header.findViewById(R.id.business_card_stud);
        BusinessCard businessCardBrewery = header.findViewById(R.id.business_card_brewery);
        BusinessCard businessCardPalace = header.findViewById(R.id.business_card_palace);
        businessCardG.setOpenerView(logoG);
        businessCardStud.setOpenerView(logoStud);
        businessCardBrewery.setOpenerView(logoBrewery);
        businessCardPalace.setOpenerView(logoPalace);
        logosHeaderTrigger.setOnClickListener(view -> openLogosHeader());
    }

    private void changeHeaderLayout(int layout){
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.load(context, layout);
        TransitionManager.beginDelayedTransition(header);
        constraintSet.applyTo(header);
    }
}