package fr.delcey.todok.data.project.model

import androidx.room.Embedded
import androidx.room.Relation
import fr.delcey.todok.data.task.model.TaskDto

data class ProjectWithTasksDto(
    @Embedded
    val projectDto: ProjectDto,
    @Relation(
        parentColumn = "id",
        entityColumn = "projectId"
    )
    val taskDtos: List<TaskDto>,
)