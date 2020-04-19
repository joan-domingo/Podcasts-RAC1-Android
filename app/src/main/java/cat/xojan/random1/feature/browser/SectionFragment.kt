package cat.xojan.random1.feature.browser

import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import cat.xojan.random1.R
import cat.xojan.random1.domain.model.CrashReporter
import cat.xojan.random1.feature.BaseFragment
import cat.xojan.random1.feature.IsMediaBrowserFragment
import cat.xojan.random1.feature.MediaBrowserProvider
import cat.xojan.random1.feature.MediaPlayerBaseActivity
import cat.xojan.random1.feature.home.ProgramFragment
import cat.xojan.random1.injection.component.BrowseComponent
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.recycler_view_fragment.*
import javax.inject.Inject

class SectionFragment : BaseFragment(), IsMediaBrowserFragment {

    @Inject internal lateinit var crashReporter: CrashReporter
    @Inject internal lateinit var viewModel: BrowserViewModel

    private lateinit var adapter: SectionListAdapter
    private val mCompositeDisposable = CompositeDisposable()

    private var mediaBrowserProvider: MediaBrowserProvider? = null

    companion object {
        val TAG = SectionFragment::class.java.simpleName
        val ARG_PROGRAM = "program_param"
        val MEDIA_ID_SECTION = "/SECTIONS"

        fun newInstance(programId: String?): SectionFragment {
            val args = Bundle()
            args.putString(ARG_PROGRAM, programId)

            val sectionFragment = SectionFragment()
            sectionFragment.arguments = args

            return sectionFragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mediaBrowserProvider = context as MediaPlayerBaseActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        getComponent(BrowseComponent::class.java).inject(this)
        val view = inflater.inflate(R.layout.recycler_view_fragment, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.visibility = View.VISIBLE
        recycler_view.layoutManager = LinearLayoutManager(activity)
        swipe_refresh.isEnabled = false
        adapter = SectionListAdapter(activity as BrowseActivity)
        recycler_view.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.sections, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                return true
            }
            R.id.action_hour_by_hour -> {
                showHourByHour()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        // fetch browsing information to fill the recycler view
        val mediaBrowser = mediaBrowserProvider?.getMediaBrowser()
        mediaBrowser?.let {
            Log.d(TAG, "onStart, onConnected=" + mediaBrowser.isConnected)
            if (mediaBrowser.isConnected) {
                onMediaControllerConnected()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mCompositeDisposable.clear()
    }

    override fun onStop() {
        super.onStop()
        val mediaBrowser = mediaBrowserProvider?.getMediaBrowser()
        mediaBrowser?.let {
            if (mediaBrowser.isConnected) {
                val mediaId = mediaId()
                mediaId?.let {
                    mediaBrowser.unsubscribe(mediaId)
                }
            }
        }
    }

    private fun mediaId(): String? {
        return arguments?.getString(ARG_PROGRAM) + MEDIA_ID_SECTION
    }

    override fun onMediaControllerConnected() {
        if (isDetached) {
            return
        }
        val mediaBrowser = mediaBrowserProvider?.getMediaBrowser()

        // Unsubscribing before subscribing is required if this mediaId already has a subscriber
        // on this MediaBrowser instance. Subscribing to an already subscribed mediaId will replace
        // the callback, but won't trigger the initial callback.onChildrenLoaded.
        //
        // This is temporary: A bug is being fixed that will make subscribe
        // consistently call onChildrenLoaded initially, no matter if it is replacing an existing
        // subscriber or not. Currently this only happens if the mediaID has no previous
        // subscriber or if the media content changes on the service side, so we need to
        // unsubscribe first.
        mediaBrowser?.let {
            val mediaId = mediaId() ?: ProgramFragment.MEDIA_ID_ROOT
            mediaBrowser.unsubscribe(mediaId)
            mediaBrowser.subscribe(mediaId, mediaBrowserSubscriptionCallback)
        }
    }

    private val mediaBrowserSubscriptionCallback = object : MediaBrowserCompat.SubscriptionCallback() {
        override fun onChildrenLoaded(parentId: String,
                                      children: List<MediaBrowserCompat.MediaItem>) {
            Log.d(TAG, "onChildrenLoaded, parentId=" + parentId + "  count=" + children.size)
            adapter.sections = children
        }

        override fun onError(id: String) {
            val msg = "section fragment subscription onError, id=" + id
            Log.e(TAG, msg)
            crashReporter.logException(msg)
        }
    }

    private fun showHourByHour() {
        viewModel.selectedSection(false)
        (activity as BrowseActivity).addFragment(
                HourByHourListFragment.newInstance(arguments?.getString(ARG_PROGRAM)),
                HourByHourListFragment.TAG,
                false)
    }
}
