package cat.xojan.random1.data

import cat.xojan.random1.domain.model.Podcast
import com.squareup.moshi.FromJson

class PodcastJsonAdapter {

    @FromJson
    fun fromJson(itemJson: PodcastJson): Podcast {
        return Podcast(
                itemJson.audio,
                itemJson.path,
                itemJson.mFilePath,
                itemJson.dateTime,
                itemJson.durationSeconds,
                itemJson.mProgramId,
                itemJson.mImageUrl,
                itemJson.mImageUrl,
                itemJson.mState,
                itemJson.appMobileTitle
        )
    }
}