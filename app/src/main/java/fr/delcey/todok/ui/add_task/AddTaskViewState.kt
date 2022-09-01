package fr.delcey.todok.ui.add_task

data class AddTaskViewState(
    val items: List<AddTaskViewStateItem>,
    val isProgressBarVisible: Boolean,
    val isOkButtonVisible: Boolean,
)