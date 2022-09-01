package fr.delcey.todok.ui.add_task

import androidx.annotation.ColorInt

data class AddTaskViewStateItem(
    val projectId: Long,
    @ColorInt
    val projectColor: Int,
    val projectName: String,
)