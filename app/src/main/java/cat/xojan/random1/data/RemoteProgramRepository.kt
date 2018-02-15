package cat.xojan.random1.data

import cat.xojan.random1.domain.model.Program
import cat.xojan.random1.domain.model.ProgramData
import cat.xojan.random1.domain.model.Section
import cat.xojan.random1.domain.repository.ProgramRepository
import io.reactivex.Observable
import io.reactivex.Single



class RemoteProgramRepository(service: Rac1ApiService): ProgramRepository {

    private var programData: Single<ProgramData> = service.getProgramData().cache()
    private var programs: Single<List<Program>> = programData.map {pd: ProgramData -> pd.programs }

    override fun getPrograms(): Single<List<Program>> = programs

    override fun getProgram(programId: String): Single<Program> =
            programs.flatMap {
                programs -> Observable.fromIterable(programs)
                    .filter { p: Program ->  p.id == programId }
                    .firstOrError()
            }

    override fun hasSections(programId: String): Single<Boolean> =
            getProgram(programId).map { p -> p.sections.size > 1 }

    override fun getSections(programId: String): Single<List<Section>> =
            getProgram(programId)
                    .map { p -> p.sections }
}