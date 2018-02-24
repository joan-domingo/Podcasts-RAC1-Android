package cat.xojan.random1.domain.model

import com.squareup.moshi.Json

data class ProgramData(
        @Json(name = "resposta") val resposta: Resposta,
        val programs: List<Program> = listOf()
)