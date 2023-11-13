package fr.delcey.todok.uixml.add_task

import fr.delcey.todok.uixml.utils.NativeText

sealed class AddTaskEvent {
    object Dismiss : AddTaskEvent()
    data class Toast(val text: NativeText) : AddTaskEvent()
}
