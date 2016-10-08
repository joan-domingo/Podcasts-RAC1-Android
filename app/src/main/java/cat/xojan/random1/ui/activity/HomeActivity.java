package cat.xojan.random1.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import cat.xojan.random1.R;
import cat.xojan.random1.injection.HasComponent;
import cat.xojan.random1.injection.component.DaggerHomeComponent;
import cat.xojan.random1.injection.component.HomeComponent;
import cat.xojan.random1.injection.module.HomeModule;
import cat.xojan.random1.ui.BaseActivity;
import cat.xojan.random1.ui.adapter.HomePagerAdapter;
import cat.xojan.random1.ui.fragment.DownloadsFragment;
import cat.xojan.random1.ui.fragment.PodcastListFragment;
import cat.xojan.random1.ui.fragment.ProgramFragment;

public class HomeActivity extends BaseActivity implements HasComponent {

    @BindView(R.id.viewpager) ViewPager mViewPager;
    @BindView(R.id.tabs) TabLayout mTabLayout;

    private HomeComponent mComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        if ((intent.getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0
                && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                && intent.getAction() != null
                && intent.getAction().equals(Intent.ACTION_MAIN)) {
            finish();
            return;
        }

        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initView();
        initInjector();
    }

    private void initInjector() {
        mComponent = DaggerHomeComponent.builder()
                .appComponent(getApplicationComponent())
                .baseActivityModule(getActivityModule())
                .homeModule(new HomeModule(this))
                .build();
        mComponent.inject(this);
    }

    private void initView() {
        HomePagerAdapter mFragmentAdapter = new HomePagerAdapter(getSupportFragmentManager(), this);
        mFragmentAdapter.addFragment(new ProgramFragment());
        mFragmentAdapter.addFragment(new PodcastListFragment());
        mFragmentAdapter.addFragment(new DownloadsFragment());

        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public HomeComponent getComponent() {
        return mComponent;
    }
}
