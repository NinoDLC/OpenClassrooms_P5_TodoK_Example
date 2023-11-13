package fr.delcey.todok.domain.task

interface TaskRepository {
    suspend fun add(projectId: Long, description: String): Boolean
    suspend fun delete(taskId: Long): Boolean
}
