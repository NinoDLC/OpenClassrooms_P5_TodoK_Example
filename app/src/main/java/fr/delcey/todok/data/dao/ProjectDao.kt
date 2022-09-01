package fr.delcey.todok.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fr.delcey.todok.domain.project.ProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Insert
    suspend fun insert(projectEntity: ProjectEntity)

    @Query("SELECT * FROM project")
    fun getAll(): Flow<List<ProjectEntity>>
}