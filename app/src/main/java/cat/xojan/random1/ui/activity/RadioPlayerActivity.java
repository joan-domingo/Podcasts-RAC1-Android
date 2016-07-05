package cat.xojan.random1.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import butterknife.ButterKnife;
import cat.xojan.random1.R;
import cat.xojan.random1.domain.entity.Podcast;
import cat.xojan.random1.injection.HasComponent;
import cat.xojan.random1.injection.component.DaggerRadioPlayerComponent;
import cat.xojan.random1.injection.component.RadioPlayerComponent;
import cat.xojan.random1.injection.module.RadioPlayerModule;
import cat.xojan.random1.ui.BaseActivity;
import cat.xojan.random1.ui.fragment.RadioPlayerFragment;

public class RadioPlayerActivity extends BaseActivity implements HasComponent {

    public static final String EXTRA_PODCAST = "PODCAST";

    private RadioPlayerComponent mComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.radio_player_activity);

        ButterKnife.bind(this);
        initInjector();
        initView();
    }

    private void initView() {
        Fragment fragment = getFragment(RadioPlayerFragment.TAG);
        if (fragment == null) {
            Bundle bundle = getIntent().getExtras();
            Podcast podcast = bundle.getParcelable(EXTRA_PODCAST);

            RadioPlayerFragment radioPlayerFragment = RadioPlayerFragment.newInstance(podcast);
            addFragment(R.id.container_fragment, radioPlayerFragment, RadioPlayerFragment.TAG,
                    false);
        }
    }

    @Override
    public RadioPlayerComponent getComponent() {
        return mComponent;
    }

    private void initInjector() {
        mComponent = DaggerRadioPlayerComponent.builder()
                .appComponent(getApplicationComponent())
                .baseActivityModule(getActivityModule())
                .radioPlayerModule(new RadioPlayerModule())
                .build();
        mComponent.inject(this);
    }
}
