package fr.delcey.todok.uixml.add_task

import androidx.annotation.ColorInt

data class AddTaskViewStateItem(
    val projectId: Long,
    @get:ColorInt @param:ColorInt
    val projectColor: Int,
    val projectName: String,
)