package fr.delcey.todok.data.task

import android.database.sqlite.SQLiteException
import fr.delcey.todok.data.task.model.TaskDto
import fr.delcey.todok.domain.CoroutineDispatcherProvider
import fr.delcey.todok.domain.task.TaskRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryRoom @Inject constructor(
    private val taskDao: TaskDao,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : TaskRepository {

    override suspend fun add(projectId: Long, description: String): Boolean = withContext(coroutineDispatcherProvider.io) {
        try {
            taskDao.insert(
                TaskDto(
                    projectId = projectId,
                    description = description
                )
            )
            true
        } catch (e: SQLiteException) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun delete(taskId: Long): Boolean = withContext(coroutineDispatcherProvider.io) {
        taskDao.delete(taskId) == 1
    }
}