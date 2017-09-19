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
    companion object {
        private val PERMISSION_WRITE_EXTERNAL_STORAGE = 20
    }

    override val component: HomeComponent by lazy {
        DaggerHomeComponent.builder()
                .appComponent(applicationComponent)
                .baseActivityModule(activityModule)
                .homeModule(HomeModule(this))
                .build()
    }

    private val pageAdapter: HomePagerAdapter by lazy {
        HomePagerAdapter(supportFragmentManager, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initView()
        component.inject(this)
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        pageAdapter.addFragment(ProgramFragment())
        pageAdapter.addFragment(DownloadsFragment())

        viewPager.adapter = pageAdapter
        tabLayout.setupWithViewPager(viewPager)
    }
}