package fr.delcey.todok.data.project

import fr.delcey.todok.domain.CoroutineDispatcherProvider
import fr.delcey.todok.domain.project.ProjectEntity
import fr.delcey.todok.domain.project.ProjectRepository
import fr.delcey.todok.domain.project_with_tasks.ProjectWithTasksEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomProjectRepository @Inject constructor(
    private val projectDao: ProjectDao,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ProjectRepository {
    override suspend fun add(projectEntity: ProjectEntity) = withContext(coroutineDispatcherProvider.io) {
        projectDao.insert(projectEntity)
    }

    override fun getProjectsAsFlow(): Flow<List<ProjectEntity>> = projectDao
        .getProjectsAsFlow()
        .flowOn(coroutineDispatcherProvider.io)

    override fun getProjectsWithTasksAsFlow(): Flow<List<ProjectWithTasksEntity>> = projectDao
        .getProjectsWithTasksAsFlow()
        .flowOn(coroutineDispatcherProvider.io)
}