package cat.xojan.random1.viewmodel

import android.databinding.BaseObservable
import android.view.View
import cat.xojan.random1.domain.entities.Program
import cat.xojan.random1.domain.interactor.ProgramDataInteractor
import cat.xojan.random1.ui.activity.BaseActivity
import cat.xojan.random1.ui.activity.BrowseActivity


class ProgramViewModel(
        private val activity: BaseActivity,
        private val program: Program,
        private val programDataInteractor: ProgramDataInteractor
) : BaseObservable() {

    val imageUrl: String?
        get() = program.imageUrl

    val title: String
        get() = program.title

    fun onClickProgram(): View.OnClickListener {
        return View.OnClickListener {
            val isSection = programDataInteractor.isSectionSelected && program.sections.size > 1
            val intent = BrowseActivity.newIntent(activity, program, isSection)
            activity.startActivity(intent)
        }
    }
}