package fr.delcey.todok.ui.add_task

import fr.delcey.todok.ui.utils.NativeText

sealed class AddTaskEvent {
    object Dismiss : AddTaskEvent()
    data class Toast(val text: NativeText) : AddTaskEvent()
}
