package fr.delcey.todok.ui.tasks

import androidx.annotation.ColorInt
import fr.delcey.todok.ui.utils.EquatableCallback

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
        @ColorInt
        val projectColor: Int,
        val description: String,
        val onDeleteEvent: EquatableCallback,
    ) : TasksViewStateItem(Type.TASK)

    object EmptyState : TasksViewStateItem(Type.EMPTY_STATE)
}