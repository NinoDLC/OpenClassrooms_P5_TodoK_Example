package fr.delcey.todok.data

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import fr.delcey.todok.data.project.model.ProjectDto
import fr.delcey.todok.data.utils.fromJson
import fr.delcey.todok.domain.CoroutineDispatcherProvider
import fr.delcey.todok.domain.project.AddProjectUseCase
import fr.delcey.todok.domain.project.model.ProjectEntity
import kotlinx.coroutines.withContext

@HiltWorker
class InitializeDatabaseWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val addProjectUseCase: AddProjectUseCase,
    private val gson: Gson,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val KEY_INPUT_DATA = "KEY_INPUT_DATA"
    }

    override suspend fun doWork(): Result = withContext(coroutineDispatcherProvider.io) {
        val dtoAsJson = inputData.getString(KEY_INPUT_DATA)

        if (dtoAsJson != null) {
            val dtoProjects = gson.fromJson<List<ProjectDto>>(json = dtoAsJson)

            if (dtoProjects != null) {
                dtoProjects.forEach { dtoProject ->
                    addProjectUseCase.invoke(
                        ProjectEntity.Simple(
                            dtoProject.id,
                            dtoProject.name,
                            dtoProject.colorInt
                        )
                    )
                }
                Result.success()
            } else {
                Log.e(javaClass.simpleName, "Gson can't parse projects : $dtoAsJson")
                Result.failure()
            }
        } else {
            Log.e(javaClass.simpleName, "Failed to get data with key $KEY_INPUT_DATA from data: $inputData")
            Result.failure()
        }
    }
}