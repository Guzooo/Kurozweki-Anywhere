package pl.Guzooo.KurozwekiAnywhere;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Page3 extends NavigationFragment {

    @Override
    public int getActionBarTitle() {
        return R.string.page_events;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.page3, container, false);
    }
}
