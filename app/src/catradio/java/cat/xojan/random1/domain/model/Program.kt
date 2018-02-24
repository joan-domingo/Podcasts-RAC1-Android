package cat.xojan.random1.domain.model

import com.squareup.moshi.Json
import org.jetbrains.annotations.TestOnly

class Program(val id: String,
              var title: String,
              var sections: List<Section>,
              private var images: Images,
              var active: Boolean
             ) {

    @TestOnly
    constructor(id: String): this(id,
            "title",
            emptyList<Section>(),
            Images("bigImage.url", "image.url"),
            false)

    fun imageUrl(): String = images.imageUrl
    fun bigImageUrl(): String = images.bigImageUrl
}

class Images(@Json(name = "person-small") val imageUrl: String,
             @Json(name = "app") val bigImageUrl: String)