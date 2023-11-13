package fr.delcey.todok.domain.task

import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend fun invoke(taskId: Long) {
        taskRepository.delete(taskId)
    }
}
