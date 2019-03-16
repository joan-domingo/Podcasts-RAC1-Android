package cat.xojan.random1.domain.model

import android.util.Log

enum class PodcastState(val code: Int) {
    LOADED(0),
    DOWNLOADING(1),
    DOWNLOADED(2),
    UNKNOWN(3);

    companion object {
        fun fromCode(code: Int): PodcastState {
            return when (code) {
                0 -> LOADED
                1 -> DOWNLOADING
                else -> DOWNLOADED
            }
        }

        fun fromString(code: String): PodcastState {
            return when (code.toUpperCase()) {
                LOADED.name.toUpperCase() -> LOADED
                DOWNLOADING.name.toUpperCase() -> DOWNLOADING
                DOWNLOADED.name.toUpperCase() -> DOWNLOADED
                else -> {
                    Log.w("PodcastState", "state not found: $code")
                    return UNKNOWN
                }
            }
        }
    }
}