package fr.delcey.todok.ui.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.delcey.todok.domain.CoroutineDispatcherProvider
import fr.delcey.todok.domain.project.ProjectEntity
import fr.delcey.todok.domain.project_with_tasks.GetProjectsWithTasksUseCase
import fr.delcey.todok.domain.task.AddRandomTaskUseCase
import fr.delcey.todok.domain.task.DeleteTaskUseCase
import fr.delcey.todok.domain.task.TaskEntity
import fr.delcey.todok.ui.utils.SingleLiveEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val addRandomTaskUseCase: AddRandomTaskUseCase,
    getProjectsWithTasksUseCase: GetProjectsWithTasksUseCase,
) : ViewModel() {

    private val taskSortingMutableStateFlow = MutableStateFlow(TaskSortingType.TASK_CHRONOLOGICAL)

    val viewStateLiveData: LiveData<List<TasksViewStateItem>> = liveData {
        combine(
            getProjectsWithTasksUseCase.invoke(),
            taskSortingMutableStateFlow
        ) { projectsWithTasks, taskSorting ->
            emit(
                when (taskSorting) {
                    TaskSortingType.TASK_CHRONOLOGICAL -> projectsWithTasks
                        .asSequence()
                        .map { projectWithTasksEntity ->
                            projectWithTasksEntity.taskEntities.map { taskEntity ->
                                mapItem(projectWithTasksEntity.projectEntity, taskEntity)
                            }
                        }
                        .flatten()
                        .sortedBy { it.taskId }
                        .toList()
                    TaskSortingType.PROJECT_ALPHABETICAL -> buildList<TasksViewStateItem> {
                        projectsWithTasks.forEach { projectWithTasksEntity ->
                            if (projectWithTasksEntity.taskEntities.isNotEmpty()) {
                                add(TasksViewStateItem.Header(projectWithTasksEntity.projectEntity.name))

                                projectWithTasksEntity.taskEntities.forEach { taskEntity ->
                                    add(mapItem(projectWithTasksEntity.projectEntity, taskEntity))
                                }
                            }
                        }
                    }
                }.takeIf { it.isNotEmpty() } ?: listOf(TasksViewStateItem.EmptyState)
            )
        }.collect()
    }

    val singleLiveEvent = SingleLiveEvent<TasksEvent>()

    private fun mapItem(projectEntity: ProjectEntity, taskEntity: TaskEntity) = TasksViewStateItem.Task(
        taskId = taskEntity.id,
        projectColor = projectEntity.colorInt,
        description = taskEntity.description,
    )

    fun onSortButtonClicked() {
        taskSortingMutableStateFlow.update {
            TaskSortingType.values()[(it.ordinal + 1) % TaskSortingType.values().size]
        }
    }

    fun onAddButtonClicked() {
        singleLiveEvent.value = TasksEvent.NavigateToAddTask
    }

    fun onAddButtonLongClicked() {
        viewModelScope.launch {
            addRandomTaskUseCase.invoke()
        }
    }

    fun onTaskSwiped(taskId: Long) {
        viewModelScope.launch {
            deleteTaskUseCase.invoke(taskId)
        }
    }
}
