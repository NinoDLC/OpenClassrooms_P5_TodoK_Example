package fr.delcey.todok.domain.task

interface TaskRepository {
    suspend fun add(taskEntity: TaskEntity): Boolean
    suspend fun delete(taskId: Long): Boolean
}
