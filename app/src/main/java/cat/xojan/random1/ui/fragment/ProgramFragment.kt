package cat.xojan.random1.ui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.xojan.random1.R
import cat.xojan.random1.domain.entities.CrashReporter
import cat.xojan.random1.domain.entities.Program
import cat.xojan.random1.domain.interactor.ProgramDataInteractor
import cat.xojan.random1.injection.component.HomeComponent
import cat.xojan.random1.ui.adapter.ProgramListAdapter
import cat.xojan.random1.viewmodel.ProgramsViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.recycler_view_fragment.*
import javax.inject.Inject


class ProgramFragment: BaseFragment() {

    @Inject internal lateinit var programsViewModel: ProgramsViewModel
    @Inject internal lateinit var programInteractor: ProgramDataInteractor
    @Inject internal lateinit var crashReporter: CrashReporter

    private val mCompositeDisposable = CompositeDisposable()
    private lateinit var adapter: ProgramListAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        getComponent(HomeComponent::class.java).inject(this)
        return inflater!!.inflate(R.layout.recycler_view_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipe_refresh.setColorSchemeResources(R.color.colorAccent)
        swipe_refresh.setOnRefreshListener { Handler().postDelayed({ this.loadPrograms() }, 0) }
        adapter = ProgramListAdapter(programInteractor)
        recycler_view.adapter = adapter
        setLayoutManager(resources.configuration.orientation)
        loadPrograms()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mCompositeDisposable.clear()
    }

    private fun setLayoutManager(orientation: Int) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recycler_view.layoutManager = GridLayoutManager(activity, 3)
        } else {
            recycler_view.layoutManager = GridLayoutManager(activity, 2)
        }
    }

    private fun loadPrograms() {
        swipe_refresh.isRefreshing = true
        mCompositeDisposable.add(programsViewModel.loadPrograms()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ this.updateView(it) },
                        { this.handleError(it) }))
    }

    private fun updateView(programs: List<Program>) {
        swipe_refresh.isRefreshing = false
        adapter.programs = programs
        empty_list.visibility = View.GONE
        recycler_view.visibility = View.VISIBLE
    }

    private fun handleError(e: Throwable) {
        crashReporter.logException(e)
        swipe_refresh.isRefreshing = false
        empty_list.visibility = View.VISIBLE
        recycler_view.visibility = View.GONE
    }
}