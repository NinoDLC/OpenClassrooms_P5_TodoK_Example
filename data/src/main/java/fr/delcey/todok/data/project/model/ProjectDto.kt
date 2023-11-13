package fr.delcey.todok.data.project.model

import androidx.annotation.ColorInt
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "project")
data class ProjectDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    @ColorInt
    val colorInt: Int,
)