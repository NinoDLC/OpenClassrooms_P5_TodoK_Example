package fr.delcey.todok.domain.project

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProjectsUseCase @Inject constructor(
    private val projectRepository: ProjectRepository,
) {
    fun invoke(): Flow<List<ProjectEntity>> = projectRepository.getProjectsAsFlow()
}