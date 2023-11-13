package fr.delcey.todok.data

import fr.delcey.todok.data.project.model.ProjectDto
import fr.delcey.todok.data.project.model.ProjectWithTasksDto
import fr.delcey.todok.data.task.model.TaskDto
import fr.delcey.todok.domain.project.model.ProjectEntity
import fr.delcey.todok.domain.task.model.TaskEntity

// region DATA
// region ProjectDto
fun getDefaultProjectDto(projectId: Number) = ProjectDto(
    id = projectId.toLong(),
    name = "PROJECT_DTO_NAME:$projectId",
    colorInt = projectId.toInt(),
)

fun getDefaultProjectDtos(projectCount: Number = 3): List<ProjectDto> = List(projectCount.toInt()) {
    getDefaultProjectDto(it)
}

fun getDefaultProjectDtosAsJson(): String = """
    [
    {"id":0,"name":"PROJECT_ENTITY_NAME:0","colorInt":0},
    {"id":1,"name":"PROJECT_ENTITY_NAME:1","colorInt":1},
    {"id":2,"name":"PROJECT_ENTITY_NAME:2","colorInt":2}
    ]
""".trimIndent()
// endregion ProjectDto

// region ProjectWithTasksDto
fun getDefaultProjectWithTasksDto(projectId: Number) = ProjectWithTasksDto(
    projectDto = getDefaultProjectDto(projectId),
    taskDtos = getDefaultTaskDtos(projectId),
)

fun getDefaultProjectWithTasksDtos(projectWithTasksCount: Number = 3): List<ProjectWithTasksDto> = List(projectWithTasksCount.toInt()) {
    getDefaultProjectWithTasksDto(it)
}
// endregion ProjectWithTasksDto

// region TaskDto
fun getDefaultTaskDto(taskId: Number, projectId: Number) = TaskDto(
    id = taskId.toLong(),
    projectId = projectId.toLong(),
    description = getDefaultTaskDtoDescription(projectId, taskId),
)

fun getDefaultTaskDtos(projectId: Number, taskCount: Number = 3): List<TaskDto> = List(taskCount.toInt()) { taskId ->
    TaskDto(
        id = taskId.toLong(),
        projectId = projectId.toLong(),
        description = "TASK_DTO_DESCRIPTION:$projectId:$taskId",
    )
}

fun getDefaultTaskDtoDescription(projectId: Number, taskId: Number): String = "TASK_DTO_DESCRIPTION:$projectId:$taskId"
// endregion TaskDto
// endregion DATA

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