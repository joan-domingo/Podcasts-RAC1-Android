package cat.xojan.random1.feature.mediaplayback

import cat.xojan.random1.domain.model.EventLogger
import cat.xojan.random1.feature.mediaplayback.QueueManager.Companion.MEDIA_ID_PLAY_ALL
import cat.xojan.random1.testutil.queueList1Item
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class QueueManagerTest {

    private lateinit var queueManager: QueueManager
    private lateinit var eventLogger: EventLogger
    private lateinit var listener: MetaDataUpdateListener

    @Before
    fun setup() {
        eventLogger = EventLogger(null)
        queueManager = QueueManager(eventLogger)

        listener = mock(MetaDataUpdateListener::class.java)
        queueManager.initListener(listener)
    }

    @Test
    fun all_podcasts_playlist_has_extended_media_controls() {
        // update playlist with 1 item
        queueManager.updateDownloadsPlaylist(queueList1Item)
        queueManager.setQueue(QueueManager.MEDIA_ID_PLAY_ALL)
        assertThat(queueManager.currentPlaylist.size, equalTo(1))

        // it has extended media controls
        assertThat(queueManager.hasNextOrPrevious(MEDIA_ID_PLAY_ALL), equalTo(1L))
    }
}