package pl.Guzooo.KurozwekiAnywhere;

import androidx.fragment.app.Fragment;

public abstract class NavigationFragment extends Fragment {

    public boolean isHome(){
        return false;
    }

    public int getActionBarTitle(){
        return R.string.app_name;
    }

    public int getActionBarMenu(){
        return 0;
    }
}
