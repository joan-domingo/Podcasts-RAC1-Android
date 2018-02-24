package cat.xojan.random1.domain.repository

import cat.xojan.random1.domain.model.Program
import cat.xojan.random1.domain.model.Section
import io.reactivex.Single

interface ProgramRepository {
    fun getPrograms(refresh: Boolean): Single<List<Program>>

    fun getProgram(programId: String): Single<Program>

    fun hasSections(programId: String): Single<Boolean>

    fun getSections(programId: String): Single<List<Section>>
}