package cat.xojan.random1.data

import cat.xojan.random1.domain.model.Podcast
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat

class PodcastJsonAdapter {

    private val df = SimpleDateFormat("MMM dd, yyyy HH:mm:ss a")

    @ToJson
    fun toJson(podcast: Podcast): PodcastJson {
        return PodcastJson(
                podcast.title,
                podcast.audio,
                df.format(podcast.dateTime),
                podcast.durationSeconds,
                podcast.filePath,
                podcast.imageUrl,
                podcast.programId,
                podcast.state,
                podcast.path
        )
    }

    @FromJson
    fun fromJson(itemJson: PodcastJson): Podcast {
        return Podcast(
                itemJson.audio,
                itemJson.path,
                itemJson.mFilePath,
                df.parse(itemJson.dateTime),
                itemJson.durationSeconds,
                itemJson.mProgramId,
                itemJson.mImageUrl,
                itemJson.mImageUrl,
                itemJson.mState,
                itemJson.appMobileTitle
        )
    }
}