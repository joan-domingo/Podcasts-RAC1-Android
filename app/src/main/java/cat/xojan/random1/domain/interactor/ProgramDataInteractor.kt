package cat.xojan.random1.domain.interactor
import cat.xojan.random1.domain.model.Program
import cat.xojan.random1.domain.model.Section
import cat.xojan.random1.domain.model.SectionType
import cat.xojan.random1.domain.repository.ProgramRepository
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class ProgramDataInteractor @Inject constructor(private val programRepo: ProgramRepository) {

    fun loadPrograms(): Single<List<Program>> {
        return programRepo.getPrograms()
                .flatMap {
                    programs -> Observable.just(programs)
                        .flatMapIterable { p -> p }
                        .filter { p -> p.active }
                        .toList()
                }
    }

    fun hasSections(programId: String): Boolean {
        return programRepo.hasSections(programId)
    }

    fun loadSections(programId: String): Single<List<Section>> {
        val program = programRepo.getProgram(programId)
        return programRepo.getSections(programId)
                .flatMap {
                    sections -> Observable.just(sections)
                        .flatMapIterable { s -> s }
                        .filter { s -> s.active }
                        .filter { s -> s.type == SectionType.SECTION }
                        .map { s ->
                            program?.let {
                                s.imageUrl = program.imageUrl()
                                s.programId = program.id
                            }
                            s
                        }
                        .toList()
                }
    }
}