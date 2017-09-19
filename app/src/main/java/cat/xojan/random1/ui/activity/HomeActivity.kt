package cat.xojan.random1.ui.activity

import android.os.Bundle
import cat.xojan.random1.R
import cat.xojan.random1.injection.HasComponent
import cat.xojan.random1.injection.component.DaggerHomeComponent
import cat.xojan.random1.injection.component.HomeComponent
import cat.xojan.random1.injection.module.HomeModule
import cat.xojan.random1.ui.adapter.HomePagerAdapter
import cat.xojan.random1.ui.fragment.DownloadsFragment
import cat.xojan.random1.ui.fragment.ProgramFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity() : BaseActivity(), HasComponent<HomeComponent> {

    override val component: HomeComponent
        get() = homeComponent

    lateinit var homeComponent : HomeComponent
    private lateinit var pageAdapter: HomePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        inject()
        initView()
    }

    private fun initView() {
        //setSupportActionBar(toolbar)
        pageAdapter = HomePagerAdapter(supportFragmentManager, this)
        pageAdapter.addFragment(ProgramFragment())
        pageAdapter.addFragment(DownloadsFragment())

        viewPager.adapter = pageAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun inject() {
        homeComponent = DaggerHomeComponent.builder()
                .appComponent(applicationComponent)
                .baseActivityModule(activityModule)
                .homeModule(HomeModule(this))
                .build()
        homeComponent.inject(this)
    }
}