package fr.delcey.todok.domain.project

import fr.delcey.todok.data.dao.ProjectDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsertProjectUseCase @Inject constructor(
    private val projectDao: ProjectDao,
) {
    suspend fun invoke(projectEntity: ProjectEntity) {
        projectDao.insert(projectEntity)
    }
}
