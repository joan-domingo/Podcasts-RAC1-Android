package cat.xojan.random1.domain.model

/**
 * Created by Joan on 24/02/2018.
 */
class Section(
        var programId: String,
        var imageUrl: String,
        val active: Boolean,
        val type: SectionType,
        val id: String,
        val title: String
)