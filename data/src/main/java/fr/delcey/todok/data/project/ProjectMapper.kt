package fr.delcey.todok.data.project

import fr.delcey.todok.data.project.model.ProjectDto
import fr.delcey.todok.domain.project.model.ProjectEntity
import fr.delcey.todok.domain.task.model.TaskEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectMapper @Inject constructor() {
    fun map(projectDto: ProjectDto) = ProjectEntity.Simple(
        id = projectDto.id,
        name = projectDto.name,
        colorInt = projectDto.colorInt,
    )

    fun map(projectDto: ProjectDto, taskEntities: List<TaskEntity>) = ProjectEntity.WithTasks(
        id = projectDto.id,
        name = projectDto.name,
        colorInt = projectDto.colorInt,
        tasks = taskEntities,
    )

    fun map(projectEntity: ProjectEntity.Simple) = ProjectDto(
        id = projectEntity.id,
        name = projectEntity.name,
        colorInt = projectEntity.colorInt,
    )
}