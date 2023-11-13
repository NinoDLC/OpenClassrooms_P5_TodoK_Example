package fr.delcey.todok

import fr.delcey.todok.domain.project.model.ProjectEntity
import fr.delcey.todok.domain.task.model.TaskEntity

// region DOMAIN
// region ProjectEntity
fun getDefaultProjectEntitySimple(projectId: Number) = ProjectEntity.Simple(
    id = projectId.toLong(),
    name = "PROJECT_ENTITY_NAME:$projectId",
    colorInt = projectId.toInt(),
)

fun getDefaultProjectEntitySimpleList(projectCount: Number = 3): List<ProjectEntity.Simple> = List(projectCount.toInt()) { projectId ->
    getDefaultProjectEntitySimple(projectId)
}

fun getDefaultProjectEntityWithTasks(projectId: Number, taskIdModifier: ((projectId: Int, taskId: Int) -> Int)? = null) =
    ProjectEntity.WithTasks(
        id = projectId.toLong(),
        name = "PROJECT_ENTITY_NAME:$projectId",
        colorInt = projectId.toInt(),
        tasks = getDefaultTaskEntityList(projectId = projectId) { taskId ->
            taskIdModifier?.invoke(projectId.toInt(), taskId) ?: taskId
        },
    )

/**
 * ```
 *  TaskId - ProjectId
 * [  0  ] - [   0   ]
 * [  1  ] - [   1   ]
 * [  2  ] - [   2   ]
 * [  3  ] - [   0   ]
 * [  4  ] - [   1   ]
 * [  5  ] - [   2   ]
 * [  6  ] - [   0   ]
 * [  7  ] - [   1   ]
 * [  8  ] - [   2   ]
 * ```
 */
fun getDefaultProjectEntityWithTasksList(projectCount: Number = 3): List<ProjectEntity.WithTasks> = List(projectCount.toInt()) {
    getDefaultProjectEntityWithTasks(it) { projectId, taskId ->
        projectId + taskId * 3
    }
}
// endregion ProjectEntity

// region TaskEntity
fun getDefaultTaskEntity(projectId: Number, taskId: Number) = TaskEntity(
    id = taskId.toLong(),
    projectId = projectId.toLong(),
    description = "TASK_ENTITY_DESCRIPTION:$projectId:$taskId"
)

fun getDefaultTaskEntityList(projectId: Number, taskCount: Number = 3, taskIdModifier: ((Int) -> Int)? = null): List<TaskEntity> =
    List(taskCount.toInt()) { taskIndex ->
        val taskId = taskIdModifier?.invoke(taskIndex)?.toLong() ?: taskIndex.toLong()
        TaskEntity(
            id = taskId,
            projectId = projectId.toLong(),
            description = "TASK_ENTITY_DESCRIPTION:$projectId:$taskId"
        )
    }
// endregion TaskEntity
// endregion DOMAIN