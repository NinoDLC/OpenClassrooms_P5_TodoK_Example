package fr.delcey.todok.domain.task

import javax.inject.Inject

class AddTaskUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend fun invoke(taskEntity: TaskEntity): Boolean = taskRepository.add(taskEntity)
}
