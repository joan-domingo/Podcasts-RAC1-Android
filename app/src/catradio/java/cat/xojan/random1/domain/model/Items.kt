package cat.xojan.random1.domain.model

import com.squareup.moshi.Json

data class Items(
        @Json(name = "lletra") val lletres: List<Lletra>
)