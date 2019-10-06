package pl.Guzooo.KurozwekiAnywhere;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Page3 extends NavigationFragment {

    @Override
    public int getActionBarTitle() {
        return R.string.page3;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_navigation_header, container, false);
    }
}
