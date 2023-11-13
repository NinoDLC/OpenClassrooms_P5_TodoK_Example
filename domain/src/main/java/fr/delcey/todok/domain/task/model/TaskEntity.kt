package fr.delcey.todok.domain.task.model

data class TaskEntity(
    val id: Long,
    val projectId: Long,
    val description: String,
)