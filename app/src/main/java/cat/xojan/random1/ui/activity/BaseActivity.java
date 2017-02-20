package cat.xojan.random1.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import cat.xojan.random1.Application;
import cat.xojan.random1.R;
import cat.xojan.random1.injection.component.AppComponent;
import cat.xojan.random1.injection.module.BaseActivityModule;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);
    }

    /**
     * Get the Main Application component for dependency injection.
     *
     * @return {@link AppComponent}
     */
    protected AppComponent getApplicationComponent() {
        return ((Application)getApplication()).getAppComponent();
    }

    /**
     * Get an Activity module for dependency injection.
     *
     * @return {@link cat.xojan.random1.injection.component.BaseActivityComponent}
     */
    protected BaseActivityModule getActivityModule() {
        return new BaseActivityModule(this);
    }

    /**
     * Adds a {@link Fragment} to this activity's layout.
     */
    public void addFragment(Fragment fragment, String tag,
                            boolean addToBackSTack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, fragment, tag);
        if (addToBackSTack) fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    /**
     * Search for a fragment on the back stack.
     * @param tag fragment's tag.
     */
    public Fragment getFragment(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }
}
