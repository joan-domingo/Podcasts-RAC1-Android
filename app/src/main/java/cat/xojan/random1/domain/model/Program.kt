package cat.xojan.random1.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.TestOnly

@Parcelize
class Program(val id: String,
              var title: String,
              var sections: List<Section>,
              private var images: Images,
              var active: Boolean
): Parcelable {

    @TestOnly
    constructor(id: String): this(id,
            "title",
            emptyList<Section>(),
            Images("bigImage.url", "image.url"),
            false)

    fun imageUrl(): String = images.imageUrl

    fun bigImageUrl(): String = images.bigImageUrl


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val program = other as Program?

        return id == program!!.id

    }

    override fun hashCode(): Int = id.hashCode()
}