package cat.xojan.random1.domain.model

import com.squareup.moshi.Json

data class Resposta(
        @Json(name ="items") val items: Items
)