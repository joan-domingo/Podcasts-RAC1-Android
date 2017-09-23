package cat.xojan.random1.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import cat.xojan.random1.R
import cat.xojan.random1.domain.entities.Program
import cat.xojan.random1.injection.HasComponent
import cat.xojan.random1.injection.component.BrowseComponent
import cat.xojan.random1.injection.component.DaggerBrowseComponent
import cat.xojan.random1.injection.module.BrowseModule
import cat.xojan.random1.ui.fragment.HourByHourListFragment
import cat.xojan.random1.ui.fragment.SectionFragment
import cat.xojan.random1.viewmodel.PodcastsViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class BrowseActivity: BaseActivity(), HasComponent<BrowseComponent> {
    companion object {
        private val EXTRA_PROGRAM = "extra_program"
        private val EXTRA_IS_SECTION = "extra_is_section"

        fun newIntent(context: Context, program: Program, isSection: Boolean): Intent {
            val intent = Intent(context, BrowseActivity::class.java)
            intent.putExtra(EXTRA_PROGRAM, program)
            intent.putExtra(EXTRA_IS_SECTION, isSection)
            return intent
        }
    }

    @Inject internal lateinit var mViewModel: PodcastsViewModel
    private val mCompositeDisposable = CompositeDisposable()

    override val component: BrowseComponent by lazy {
        DaggerBrowseComponent.builder()
                .appComponent(applicationComponent)
                .baseActivityModule(activityModule)
                .browseModule(BrowseModule(this))
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse)
        component.inject(this)
        initView()
    }

    private fun initView() {
        val program = intent.getParcelableExtra<Program>(EXTRA_PROGRAM)
        if (intent.getBooleanExtra(EXTRA_IS_SECTION, false)) {
            addFragment(SectionFragment.newInstance(program), SectionFragment.TAG, true)
        } else {
            addFragment(HourByHourListFragment.newInstance(program), HourByHourListFragment.TAG, true)
        }
    }
}