package fr.delcey.todok.data.project

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import fr.delcey.todok.data.project.model.ProjectDto
import fr.delcey.todok.data.project.model.ProjectWithTasksDto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Insert
    suspend fun insert(projectDto: ProjectDto)

    @Query("SELECT * FROM project")
    fun getProjectsAsFlow(): Flow<List<ProjectDto>>

    @Query("SELECT * FROM project")
    @Transaction
    fun getProjectsWithTasksAsFlow(): Flow<List<ProjectWithTasksDto>>
}