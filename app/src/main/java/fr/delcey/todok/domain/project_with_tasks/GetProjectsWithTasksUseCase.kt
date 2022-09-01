package fr.delcey.todok.domain.project_with_tasks

import fr.delcey.todok.data.dao.TaskDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetProjectsWithTasksUseCase @Inject constructor(
    private val taskDao: TaskDao,
) {
    fun invoke(): Flow<List<ProjectWithTasksEntity>> = taskDao.getAllProjectsWithTasks()
}
