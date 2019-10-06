package pl.Guzooo.KurozwekiAnywhere;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Home extends NavigationFragment {

    @Override
    public boolean isHome() {
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_navigation_header, container, false);
    }
}
