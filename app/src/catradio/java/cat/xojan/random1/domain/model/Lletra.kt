package cat.xojan.random1.domain.model

import com.squareup.moshi.Json

data class Lletra(
        @Json(name="valor") val valor: Char,
        @Json(name="item") val items: List<Item>
)