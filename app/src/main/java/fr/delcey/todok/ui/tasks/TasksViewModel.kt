package fr.delcey.todok.ui.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.delcey.todok.domain.CoroutineDispatcherProvider
import fr.delcey.todok.domain.navigate.NavigateUseCase
import fr.delcey.todok.domain.navigate.model.DestinationEntity
import fr.delcey.todok.domain.project.ProjectEntity
import fr.delcey.todok.domain.project_with_tasks.GetProjectsWithTasksUseCase
import fr.delcey.todok.domain.task.DeleteTaskUseCase
import fr.delcey.todok.domain.task.InsertRandomTaskUseCase
import fr.delcey.todok.domain.task.TaskEntity
import fr.delcey.todok.ui.utils.EquatableCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val insertRandomTaskUseCase: InsertRandomTaskUseCase,
    private val navigateUseCase: NavigateUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    getProjectsWithTasksUseCase: GetProjectsWithTasksUseCase,
) : ViewModel() {

    private val taskSortingMutableStateFlow = MutableStateFlow(TaskSortingType.TASK_CHRONOLOGICAL)

    val viewStateLiveData: LiveData<List<TasksViewStateItem>> = liveData(coroutineDispatcherProvider.io) {
        combine(
            getProjectsWithTasksUseCase.invoke(),
            taskSortingMutableStateFlow
        ) { projectsWithTasks, taskSorting ->
            emit(
                when (taskSorting) {
                    TaskSortingType.TASK_CHRONOLOGICAL -> projectsWithTasks.asSequence()
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

    private fun mapItem(projectEntity: ProjectEntity, taskEntity: TaskEntity) = TasksViewStateItem.Task(
        taskId = taskEntity.id,
        projectColor = projectEntity.colorInt,
        description = taskEntity.description,
        onClickEvent = EquatableCallback {
            viewModelScope.launch {
                navigateUseCase.invoke(DestinationEntity.Activity.Detail(taskEntity.id))
            }
        },
        onDeleteEvent = EquatableCallback {
            viewModelScope.launch(coroutineDispatcherProvider.io) {
                deleteTaskUseCase.invoke(taskEntity.id)
            }
        }
    )

    fun onSortButtonClicked() {
        taskSortingMutableStateFlow.update {
            TaskSortingType.values()[(it.ordinal + 1) % TaskSortingType.values().size]
        }
    }

    fun onAddButtonClicked() {
        viewModelScope.launch {
            navigateUseCase.invoke(DestinationEntity.DialogFragment.AddTask)
        }
    }

    fun onAddButtonLongClicked() {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            insertRandomTaskUseCase.invoke()
        }
    }
}
