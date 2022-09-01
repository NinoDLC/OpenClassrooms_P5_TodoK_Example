package fr.delcey.todok.domain.project_with_tasks

import androidx.room.Embedded
import androidx.room.Relation
import fr.delcey.todok.domain.project.ProjectEntity
import fr.delcey.todok.domain.task.TaskEntity

data class ProjectWithTasksEntity(
    @Embedded
    val projectEntity: ProjectEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "projectId"
    )
    val taskEntities: List<TaskEntity>,
)