package fr.delcey.todok.domain

import fr.delcey.todok.domain.project.model.ProjectEntity
import fr.delcey.todok.domain.task.model.TaskEntity

// region DOMAIN
// region ProjectEntity
fun getDefaultProjectEntitySimple(projectId: Number) = ProjectEntity.Simple(
    id = projectId.toLong(),
    name = "PROJECT_ENTITY_NAME:$projectId",
    colorInt = projectId.toInt(),
)

fun getDefaultProjectEntitySimpleList(projectCount: Number = 3): List<ProjectEntity.Simple> = List(projectCount.toInt()) {
    getDefaultProjectEntitySimple(it)
}

fun getDefaultProjectEntityWithTasks(projectId: Number) = ProjectEntity.WithTasks(
    id = projectId.toLong(),
    name = "PROJECT_ENTITY_NAME:$projectId",
    colorInt = projectId.toInt(),
    tasks = getDefaultTaskEntityList(projectId),
)

fun getDefaultProjectEntityWithTasksList(projectCount: Number = 3): List<ProjectEntity.WithTasks> = List(projectCount.toInt()) {
    getDefaultProjectEntityWithTasks(it)
}
// endregion ProjectEntity

// region TaskEntity
fun getDefaultTaskEntity(projectId: Number, taskId: Number) = TaskEntity(
    id = taskId.toLong(),
    projectId = projectId.toLong(),
    description = "TASK_ENTITY_DESCRIPTION:$projectId:$taskId"
)

fun getDefaultTaskEntityList(projectId: Number, taskCount: Number = 3): List<TaskEntity> = List(taskCount.toInt()) { taskId ->
    TaskEntity(
        id = taskId.toLong(),
        projectId = projectId.toLong(),
        description = "TASK_ENTITY_DESCRIPTION:$projectId:$taskId"
    )
}
// endregion TaskEntity
// endregion DOMAIN