package fr.delcey.todok.data.project

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import fr.delcey.todok.domain.project.ProjectEntity
import fr.delcey.todok.domain.project_with_tasks.ProjectWithTasksEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Insert
    suspend fun insert(projectEntity: ProjectEntity)

    @Query("SELECT * FROM project")
    fun getProjectsAsFlow(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM project")
    @Transaction
    fun getProjectsWithTasksAsFlow(): Flow<List<ProjectWithTasksEntity>>
}