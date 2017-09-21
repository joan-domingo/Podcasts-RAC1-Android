package cat.xojan.random1.domain.entities

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.os.Parcelable
import android.util.Log
import com.android.databinding.library.baseAdapters.BR
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class Podcast(private var audio: Audio,
              var path: String,
              var filePath: String?,
              var dateTime: String,
              private var durationSeconds: Long,
              var programId: String,
              private var _imageUrl: String?,
              private var _state: State?,
              private var appMobileTitle: String,
              var downloadReference: Long
) : BaseObservable(), Parcelable {

    val title: String
        @Bindable get() = appMobileTitle

    var imageUrl: String?
        get() = _imageUrl
        set(value) {
            _imageUrl = value
            notifyPropertyChanged(BR.imageUrl)
        }

    var state: State
        get() {
            _state?.let {
                return _state as State
            }
            return State.LOADED
        }
        set(value) {
            _state = value
            notifyPropertyChanged(BR.state)
        }

    @Transient
    var audioId: String? = null
        get() = audio.id

    enum class State {
        LOADED,
        DOWNLOADING,
        DOWNLOADED
    }
}