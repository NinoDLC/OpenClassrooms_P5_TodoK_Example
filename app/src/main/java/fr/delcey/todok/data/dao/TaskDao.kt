package fr.delcey.todok.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import fr.delcey.todok.domain.project_with_tasks.ProjectWithTasksEntity
import fr.delcey.todok.domain.task.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(tasksEntity: TaskEntity)

    @Query("SELECT * FROM project")
    @Transaction
    fun getAllProjectsWithTasks(): Flow<List<ProjectWithTasksEntity>>

    @Query("DELETE FROM task WHERE id=:taskId")
    suspend fun delete(taskId: Long): Int
}