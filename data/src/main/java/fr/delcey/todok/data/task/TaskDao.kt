package fr.delcey.todok.data.task

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fr.delcey.todok.data.task.model.TaskDto

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(taskDto: TaskDto)

    @Query("DELETE FROM task WHERE id=:taskId")
    suspend fun delete(taskId: Long): Int
}