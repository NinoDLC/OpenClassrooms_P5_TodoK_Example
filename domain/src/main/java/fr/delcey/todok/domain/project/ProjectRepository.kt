package fr.delcey.todok.domain.project

import fr.delcey.todok.domain.project.model.ProjectEntity
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    suspend fun add(projectEntity: ProjectEntity.Simple)
    fun getProjectsAsFlow(): Flow<List<ProjectEntity.Simple>>
    fun getProjectsWithTasksAsFlow(): Flow<List<ProjectEntity.WithTasks>>
}
