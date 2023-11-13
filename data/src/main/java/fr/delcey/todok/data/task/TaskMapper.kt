package fr.delcey.todok.data.task

import fr.delcey.todok.data.task.model.TaskDto
import fr.delcey.todok.domain.task.model.TaskEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskMapper @Inject constructor() {
    fun map(taskDto: TaskDto) = TaskEntity(
        id = taskDto.id,
        projectId = taskDto.projectId,
        description = taskDto.description,
    )
}