package fr.delcey.todok.ui.add_task

import fr.delcey.todok.ui.utils.NativeText

sealed class AddTaskEvent {
    data class Toast(val text: NativeText) : AddTaskEvent()
}
