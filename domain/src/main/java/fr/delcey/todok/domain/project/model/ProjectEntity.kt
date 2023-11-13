package fr.delcey.todok.domain.project.model

import androidx.annotation.ColorInt
import fr.delcey.todok.domain.task.model.TaskEntity

sealed class ProjectEntity {

    abstract val id: Long
    abstract val name: String

    @get:ColorInt
    abstract val colorInt: Int

    data class Simple(
        override val id: Long,
        override val name: String,
        @ColorInt override val colorInt: Int,
    ) : ProjectEntity()

    data class WithTasks(
        override val id: Long,
        override val name: String,
        @ColorInt override val colorInt: Int,

        val tasks: List<TaskEntity>,
    ) : ProjectEntity()
}