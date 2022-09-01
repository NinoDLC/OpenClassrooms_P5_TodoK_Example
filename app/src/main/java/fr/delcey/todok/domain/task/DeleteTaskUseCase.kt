package fr.delcey.todok.domain.task

import fr.delcey.todok.data.dao.TaskDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteTaskUseCase @Inject constructor(private val taskDao: TaskDao) {
    suspend fun invoke(taskId: Long) {
        taskDao.delete(taskId)
    }
}
