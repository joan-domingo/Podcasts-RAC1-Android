package cat.xojan.random1.domain.model

import com.squareup.moshi.Json

data class Item(
        @Json(name = "lletra") val lletra: Char,
        @Json(name = "presentador") val presentador: String? = null,
        @Json(name = "url_podcast") val urlPodcast: String? = null,
        @Json(name = "id") val id: String,
        @Json(name = "titol") val titol: String,
        @Json(name = "data_publicacio") val dataPublicacio: String,
        @Json(name = "domini") val domini: String
        //@Json(name = "seccions") val seccions: Seccio,
        //@Json(name = "imatges") val imatges: Imatge,
)