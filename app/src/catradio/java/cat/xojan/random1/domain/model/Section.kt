package cat.xojan.random1.domain.model

/**
 * Created by Joan on 24/02/2018.
 */
class Section(
        val id: String,
        val title: String,
        var imageUrl: String?,
        val active: Boolean,
        var type: SectionType,
        var programId: String?
)