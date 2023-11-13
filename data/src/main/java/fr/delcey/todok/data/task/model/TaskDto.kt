package fr.delcey.todok.data.task.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import fr.delcey.todok.data.project.model.ProjectDto

@Entity(
    tableName = "task",
    foreignKeys = [
        ForeignKey(
            entity = ProjectDto::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
        )
    ],
)
data class TaskDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(index = true)
    val projectId: Long,
    val description: String,
)