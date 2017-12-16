package cat.xojan.random1.feature.browser

import android.app.Activity
import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimationDrawable
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.xojan.random1.R
import cat.xojan.random1.domain.model.Podcast
import cat.xojan.random1.domain.model.Podcast.Companion.PODCAST_FILE_PATH
import cat.xojan.random1.domain.model.Podcast.Companion.PODCAST_STATE
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.podcast_item.*
import java.util.*


class PodcastListAdapter(private val viewModel: BrowserViewModel,
                         private val activity: Activity) : RecyclerView
.Adapter<PodcastListAdapter.MediaItemViewHolder>() {

    var podcasts = emptyList<MediaBrowserCompat.MediaItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun updatePodcastsState(updatedStatePodcasts: List<MediaBrowserCompat.MediaItem>) {
        for (mediaItem in podcasts) {
            val podcast = mediaItem.description
            podcast.extras?.putString(PODCAST_FILE_PATH, null)
            podcast.extras?.putSerializable(PODCAST_STATE, Podcast.State.LOADED)
        }

        for (p in updatedStatePodcasts) {
            val updatedPodcast = p.description
            val currentPodcast = podcasts.firstOrNull { it.mediaId == p.mediaId }
            currentPodcast?.let {
                val description = currentPodcast.description
                description.extras?.putString(PODCAST_FILE_PATH,
                        updatedPodcast.extras?.getString(PODCAST_FILE_PATH))
                description.extras?.putSerializable(PODCAST_STATE,
                        updatedPodcast.extras?.getSerializable(PODCAST_STATE) as Podcast.State)
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.podcast_item, parent, false)
        return MediaItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MediaItemViewHolder?, position: Int) {
        holder?.bind(podcasts[position], viewModel, activity)
    }

    override fun getItemCount(): Int = podcasts.size

    class MediaItemViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private val STATE_NONE = 0
        private val STATE_PAUSED = 1
        private val STATE_PLAYING = 2
        private val STATE_BUFFERING = 3

        fun bind(item: MediaBrowserCompat.MediaItem, viewModel: BrowserViewModel,
                 activity: Activity) {

            val podcast = item.description

            itemView.setOnClickListener {
                MediaControllerCompat.getMediaController(itemView.context as BrowseActivity)
                        .transportControls.playFromMediaId(item.mediaId, null)
            }

            val state = podcast.extras?.getSerializable(PODCAST_STATE) as Podcast.State
            when (state) {
                Podcast.State.LOADED -> podcast_icon.setImageResource(R.drawable.ic_arrow_down)
                Podcast.State.DOWNLOADING -> {
                    podcast_icon.setImageResource(R.drawable.animated_arrow)
                    if (podcast_icon.drawable is Animatable) {
                        (podcast_icon.drawable as Animatable).start()
                    }
                }
                Podcast.State.DOWNLOADED -> podcast_icon.setImageResource(R.drawable.ic_delete)
            }

            podcast_icon.setOnClickListener {
                when (state) {
                    Podcast.State.LOADED -> viewModel.downloadPodcast(podcast)
                    Podcast.State.DOWNLOADING -> {}
                    Podcast.State.DOWNLOADED -> viewModel.deletePodcast(podcast)
                }
                viewModel.refreshDownloadedPodcast()
            }

            podcast_title.text = podcast.title
            Picasso.with(itemView.context)
                    .load(podcast.iconUri.toString() + "?w=" + getWeekOfTheYear())
                    .resize(200, 200)
                    .placeholder(R.drawable.default_rac1)
                    .transform(CircleTransform())
                    .into(podcast_image)

            val playbackState = getMediaItemState(activity, item)
            when (playbackState) {
                STATE_PLAYING -> {
                    playback_state.setImageResource(R.drawable.ic_equalizer_white_36dp)
                    if (playback_state.drawable is Animatable) {
                        (playback_state.drawable as Animatable).start()
                    }
                }
                STATE_PAUSED -> playback_state.setImageResource(R.drawable.ic_equalizer_white_24px)
                STATE_BUFFERING -> playback_state.setImageResource(R.drawable.ic_equalizer_white_24px)
                else -> playback_state.setImageResource(R.drawable.ic_play_arrow)
            }
        }

        private fun getWeekOfTheYear(): Int {
            val cal = Calendar.getInstance()
            return cal.get(Calendar.WEEK_OF_YEAR)
        }

        private fun getMediaItemState(activity: Activity,
                                      mediaItem: MediaBrowserCompat.MediaItem): Int {
            val controller = MediaControllerCompat.getMediaController(activity)
            controller?.let {
                val controllerMediaId = controller.metadata?.description?.mediaId
                if (controllerMediaId == mediaItem.mediaId) {
                    val playbackState = controller.playbackState
                    playbackState?.let {
                        return when (playbackState.state) {
                            PlaybackStateCompat.STATE_PLAYING -> STATE_PLAYING
                            PlaybackStateCompat.STATE_PAUSED -> STATE_PAUSED
                            PlaybackStateCompat.STATE_BUFFERING -> STATE_BUFFERING
                            else -> STATE_NONE
                        }
                    }
                }
            }
            return STATE_NONE
        }
    }
}