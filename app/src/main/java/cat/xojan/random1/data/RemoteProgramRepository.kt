package cat.xojan.random1.data

import cat.xojan.random1.domain.model.Program
import cat.xojan.random1.domain.model.Section
import cat.xojan.random1.domain.repository.ProgramRepository
import io.reactivex.Single
import java.io.IOException

class RemoteProgramRepository(private val service: Rac1ApiService): ProgramRepository {

    var programs: LinkedHashMap<String,  Program> = linkedMapOf()

    override fun getPrograms(): Single<List<Program>> {
        return Single.create { subscriber ->
            if (programs.isEmpty()) {
                val response = service.getProgramData().execute()
                if (response.isSuccessful) {
                    for (item in response.body()!!.programs) {
                        programs.put(item.id, item)
                    }
                } else {
                    subscriber.onError(Throwable(response.message()))
                }
            }
            subscriber.onSuccess(programs.values.toList())
        }
    }

    override fun getProgram(programId: String): Program? {
        return programs[programId]
    }

    override fun hasSections(programId: String): Boolean {
        if (programs[programId]?.sections?.size!! > 1) {
            return true
        }
        return false
    }

    override fun getSections(programId: String): Single<List<Section>> {
        return Single.create { subscriber ->
            try {
                subscriber.onSuccess(programs[programId]!!.sections)
            } catch (e: IOException) {
                subscriber.onError(e)
            }
        }
    }
}