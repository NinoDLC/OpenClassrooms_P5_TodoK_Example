package fr.delcey.todok.data.project

import fr.delcey.todok.data.task.TaskMapper
import fr.delcey.todok.domain.CoroutineDispatcherProvider
import fr.delcey.todok.domain.project.model.ProjectEntity
import fr.delcey.todok.domain.project.ProjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepositoryRoom @Inject constructor(
    private val projectDao: ProjectDao,
    private val projectMapper: ProjectMapper,
    private val taskMapper: TaskMapper,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ProjectRepository {
    override suspend fun add(projectEntity: ProjectEntity.Simple): Unit = withContext(coroutineDispatcherProvider.io) {
        projectDao.insert(projectMapper.map(projectEntity))
    }

    override fun getProjectsAsFlow(): Flow<List<ProjectEntity.Simple>> = projectDao
        .getProjectsAsFlow()
        .map { projectDtos ->
            projectDtos.map { projectDto ->
                projectMapper.map(projectDto)
            }
        }
        .flowOn(coroutineDispatcherProvider.io)

    override fun getProjectsWithTasksAsFlow(): Flow<List<ProjectEntity.WithTasks>> = projectDao
        .getProjectsWithTasksAsFlow()
        .map { projectWithTasksDtos ->
            projectWithTasksDtos.map { projectWithTasksDto ->
                projectMapper.map(
                    projectDto = projectWithTasksDto.projectDto,
                    taskEntities = projectWithTasksDto.taskDtos.map { taskMapper.map(it) }
                )
            }
        }
        .flowOn(coroutineDispatcherProvider.io)
}