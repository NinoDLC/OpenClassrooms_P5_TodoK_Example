package fr.delcey.todok.uixml.add_task

data class AddTaskViewState(
    val items: List<AddTaskViewStateItem>,
    val isProgressBarVisible: Boolean,
    val isOkButtonVisible: Boolean,
)