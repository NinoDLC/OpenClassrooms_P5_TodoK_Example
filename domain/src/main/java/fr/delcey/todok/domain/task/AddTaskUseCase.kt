package fr.delcey.todok.domain.task

import javax.inject.Inject

class AddTaskUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend fun invoke(projectId: Long, description: String): Boolean = taskRepository.add(projectId, description)
}
