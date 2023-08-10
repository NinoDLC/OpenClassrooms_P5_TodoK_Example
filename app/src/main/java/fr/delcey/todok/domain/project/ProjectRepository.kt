package fr.delcey.todok.domain.project

import fr.delcey.todok.domain.project_with_tasks.ProjectWithTasksEntity
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    suspend fun add(projectEntity: ProjectEntity)
    fun getProjectsAsFlow(): Flow<List<ProjectEntity>>
    fun getProjectsWithTasksAsFlow(): Flow<List<ProjectWithTasksEntity>>
}
