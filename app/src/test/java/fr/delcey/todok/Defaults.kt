package fr.delcey.todok

import fr.delcey.todok.domain.project.ProjectEntity
import fr.delcey.todok.domain.project_with_tasks.ProjectWithTasksEntity
import fr.delcey.todok.domain.task.TaskEntity

// region ProjectEntity
fun getDefaultProjectEntity(projectId: Number) = ProjectEntity(
    id = projectId.toLong(),
    name = "PROJECT_ENTITY_NAME:$projectId",
    colorInt = projectId.toInt(),
)

fun getDefaultProjectEntities(projectCount: Number = 3) = List(projectCount.toInt()) {
    getDefaultProjectEntity(it)
}

fun getDefaultProjectEntitiesAsJson() = """
    [
    {"id":0,"name":"PROJECT_ENTITY_NAME:0","colorInt":0},
    {"id":1,"name":"PROJECT_ENTITY_NAME:1","colorInt":1},
    {"id":2,"name":"PROJECT_ENTITY_NAME:2","colorInt":2}
    ]
""".trimIndent()
// endregion ProjectEntity


// region TaskEntity
fun getDefaultTaskEntity(projectId: Number, taskId: Number) = TaskEntity(
    id = taskId.toLong(),
    projectId = projectId.toLong(),
    description = "TASK_ENTITY_DESCRIPTION:$projectId:$taskId",
)
// endregion TaskEntity


// region ProjectWithTasksEntity
/**
 * ```
 * ProjectId - TaskId
 * [   0   ] - [  0  ]
 * [   1   ] - [  1  ]
 * [   2   ] - [  2  ]
 * [   0   ] - [  3  ]
 * [   1   ] - [  4  ]
 * [   2   ] - [  5  ]
 * [   0   ] - [  6  ]
 * [   1   ] - [  7  ]
 * [   2   ] - [  8  ]
 * ```
 */
fun getDefaultProjectWithTasksEntities(
    projectCount: Number = 3,
    taskCount: Number = 3,
) = List(projectCount.toInt()) { projectId ->
    ProjectWithTasksEntity(
        projectEntity = getDefaultProjectEntity(projectId = projectId),
        taskEntities = List(taskCount.toInt()) { taskId ->
            getDefaultTaskEntity(
                projectId = projectId,
                taskId = projectId + (taskId * projectCount.toInt())
            )
        },
    )
}