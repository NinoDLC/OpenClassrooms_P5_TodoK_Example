package fr.delcey.todok.ui.tasks

sealed class TasksEvent {
    object NavigateToAddTask : TasksEvent()
}
