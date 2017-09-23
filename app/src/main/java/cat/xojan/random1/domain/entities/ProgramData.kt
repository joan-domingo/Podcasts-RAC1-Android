package cat.xojan.random1.domain.entities

import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.TestOnly


class ProgramData {

    @SerializedName("result")
    @set:TestOnly
    var programs: List<Program> = listOf()
}