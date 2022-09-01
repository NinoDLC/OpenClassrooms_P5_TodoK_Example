package fr.delcey.todok.domain.project

import fr.delcey.todok.data.dao.ProjectDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetProjectsUseCase @Inject constructor(
    private val projectDao: ProjectDao,
) {
    fun invoke(): Flow<List<ProjectEntity>> = projectDao.getAll()
}