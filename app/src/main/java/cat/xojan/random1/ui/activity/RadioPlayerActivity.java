package cat.xojan.random1.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;

import javax.inject.Inject;

import butterknife.ButterKnife;
import cat.xojan.random1.BuildConfig;
import cat.xojan.random1.R;
import cat.xojan.random1.domain.entity.Podcast;
import cat.xojan.random1.injection.HasComponent;
import cat.xojan.random1.injection.component.DaggerRadioPlayerComponent;
import cat.xojan.random1.injection.component.RadioPlayerComponent;
import cat.xojan.random1.injection.module.RadioPlayerModule;
import cat.xojan.random1.ui.BaseActivity;
import cat.xojan.random1.ui.controller.NotificationController;
import cat.xojan.random1.ui.fragment.RadioPlayerFragment;

public class RadioPlayerActivity extends BaseActivity implements HasComponent {

    public static final String EXTRA_PODCAST = "PODCAST";
    private static final int NOTIFICATION_ID = 0;

    @Inject NotificationController mNotificationController;

    private RadioPlayerComponent mComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.radio_player_activity);

        ButterKnife.bind(this);
        initInjector();
        initView();
        showActionBarNotification();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissActionBarNotification();
        mNotificationController.destroy();
    }

    @Override
    public RadioPlayerComponent getComponent() {
        return mComponent;
    }

    private void initView() {
        Fragment fragment = getFragment(RadioPlayerFragment.TAG);
        if (fragment == null) {
            Bundle bundle = getIntent().getExtras();
            Podcast podcast = bundle.getParcelable(EXTRA_PODCAST);

            RadioPlayerFragment radioPlayerFragment = RadioPlayerFragment.newInstance(podcast);
            addFragment(R.id.container_fragment, radioPlayerFragment, RadioPlayerFragment.TAG,
                    false);

            logEvent(podcast);
        }
    }

    private void initInjector() {
        mComponent = DaggerRadioPlayerComponent.builder()
                .appComponent(getApplicationComponent())
                .baseActivityModule(getActivityModule())
                .radioPlayerModule(new RadioPlayerModule())
                .build();
        mComponent.inject(this);
    }

    private void logEvent(Podcast podcast) {
        if (!BuildConfig.DEBUG) {
            Answers.getInstance().logContentView(new ContentViewEvent()
                    .putContentName(podcast.category())
                    .putContentType(podcast.description()));
        }
    }

    private void showActionBarNotification() {
        mNotificationController.showNotification(RadioPlayerActivity.class, NOTIFICATION_ID);
    }

    private void dismissActionBarNotification() {
        mNotificationController.dissmissNotification(NOTIFICATION_ID);
    }
}
