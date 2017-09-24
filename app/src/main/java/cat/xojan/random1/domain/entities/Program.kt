package cat.xojan.random1.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Program(var id: String,
              var title: String,
              var sections: List<Section>,
              private var images: Images,
              var active: Boolean
): Parcelable {

    fun imageUrl(): String {
        return images.imageUrl
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val program = other as Program?

        return id == program!!.id

    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}