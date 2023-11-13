package fr.delcey.todok.uixml.tasks

import androidx.annotation.ColorInt

sealed class TasksViewStateItem(
    val type: Type,
) {
    enum class Type {
        HEADER,
        TASK,
        EMPTY_STATE,
    }

    data class Header(
        val title: String,
    ) : TasksViewStateItem(Type.HEADER)

    data class Task(
        val taskId: Long,
        @get:ColorInt @param:ColorInt
        val projectColor: Int,
        val description: String,
    ) : TasksViewStateItem(Type.TASK)

    object EmptyState : TasksViewStateItem(Type.EMPTY_STATE)
}