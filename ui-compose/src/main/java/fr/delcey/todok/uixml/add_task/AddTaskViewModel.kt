package fr.delcey.todok.uixml.add_task

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.delcey.todok.domain.CoroutineDispatcherProvider
import fr.delcey.todok.domain.project.GetProjectsUseCase
import fr.delcey.todok.domain.task.AddTaskUseCase
import fr.delcey.todok.uixml.R
import fr.delcey.todok.uixml.utils.NativeText
import fr.delcey.todok.uixml.utils.SingleLiveEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    getProjectsUseCase: GetProjectsUseCase,
) : ViewModel() {

    private val isAddingTaskInDatabaseMutableStateFlow = MutableStateFlow(false)

    private var projectId: Long? = null
    private var description: String? = null

    val viewStateLiveData: LiveData<AddTaskViewState> = liveData {
        combine(
            getProjectsUseCase.invoke(),
            isAddingTaskInDatabaseMutableStateFlow
        ) { projects, isAddingTaskInDatabase ->
            emit(
                AddTaskViewState(
                    items = projects.map { projectEntity ->
                        AddTaskViewStateItem(
                            projectId = projectEntity.id,
                            projectColor = projectEntity.colorInt,
                            projectName = projectEntity.name,
                        )
                    },
                    isProgressBarVisible = isAddingTaskInDatabase,
                    isOkButtonVisible = !isAddingTaskInDatabase
                )
            )
        }.collect()
    }

    val singleLiveEvent = SingleLiveEvent<AddTaskEvent>()

    fun onProjectSelected(projectId: Long) {
        this.projectId = projectId
    }

    fun onTaskDescriptionChanged(description: String?) {
        this.description = description
    }

    fun onOkButtonClicked() {
        val capturedProjectId = projectId
        val capturedDescription = description

        if (capturedProjectId != null && !capturedDescription.isNullOrBlank()) {
            isAddingTaskInDatabaseMutableStateFlow.value = true

            viewModelScope.launch {
                val success = addTaskUseCase.invoke(
                    projectId = capturedProjectId,
                    description = capturedDescription
                )

                isAddingTaskInDatabaseMutableStateFlow.value = false

                singleLiveEvent.value = if (success) {
                    AddTaskEvent.Dismiss
                } else {
                    AddTaskEvent.Toast(text = NativeText.Resource(R.string.cant_insert_task))
                }
            }
        } else {
            singleLiveEvent.value = AddTaskEvent.Toast(text = NativeText.Resource(R.string.error_inserting_task))
        }
    }
}