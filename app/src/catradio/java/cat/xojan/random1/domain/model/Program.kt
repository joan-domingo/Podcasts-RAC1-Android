package cat.xojan.random1.domain.model

class Program(val id: String,
              val sections: List<Section>,
              val imageUrl: String,
              private val bigImageUrl: String,
              val active: Boolean,
              val title: String) {

    fun imageUrl(): String = imageUrl
    fun bigImageUrl(): String = bigImageUrl
}