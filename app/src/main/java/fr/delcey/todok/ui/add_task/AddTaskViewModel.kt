package fr.delcey.todok.ui.add_task

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.delcey.todok.R
import fr.delcey.todok.domain.CoroutineDispatcherProvider
import fr.delcey.todok.domain.navigate.NavigateUseCase
import fr.delcey.todok.domain.navigate.model.DestinationEntity
import fr.delcey.todok.domain.project.GetProjectsUseCase
import fr.delcey.todok.domain.task.InsertTaskUseCase
import fr.delcey.todok.domain.task.TaskEntity
import fr.delcey.todok.ui.utils.NativeText
import fr.delcey.todok.ui.utils.SingleLiveEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val insertTaskUseCase: InsertTaskUseCase,
    private val navigateUseCase: NavigateUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    getProjectsUseCase: GetProjectsUseCase,
) : ViewModel() {

    private val isAddingTaskInDatabaseMutableStateFlow = MutableStateFlow(false)

    private var projectId: Long? = null
    private var description: String? = null

    val viewStateLiveData: LiveData<AddTaskViewState> = liveData(coroutineDispatcherProvider.io) {
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

            viewModelScope.launch(coroutineDispatcherProvider.io) {
                val success = insertTaskUseCase.invoke(
                    TaskEntity(
                        projectId = capturedProjectId,
                        description = capturedDescription
                    )
                )

                isAddingTaskInDatabaseMutableStateFlow.value = false

                if (success) {
                    navigateUseCase.invoke(DestinationEntity.Dismiss)
                } else {
                    withContext(coroutineDispatcherProvider.main) {
                        singleLiveEvent.value = AddTaskEvent.Toast(text = NativeText.Resource(R.string.cant_insert_task))
                    }
                }
            }
        } else {
            singleLiveEvent.value = AddTaskEvent.Toast(text = NativeText.Resource(R.string.error_inserting_task))
        }
    }

    fun onCancelButtonClicked() {
        viewModelScope.launch {
            navigateUseCase.invoke(DestinationEntity.Dismiss)
        }
    }
}