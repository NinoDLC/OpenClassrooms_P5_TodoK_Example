package fr.delcey.todok.ui

import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.scopes.ActivityScoped
import fr.delcey.todok.ui.add_task.AddTaskDialogFragment
import javax.inject.Inject

@ActivityScoped
class NavigationListener @Inject constructor(private val activity: FragmentActivity) {
    fun displayAddTaskDialog() {
        AddTaskDialogFragment.newInstance().show(activity.supportFragmentManager, null)
    }
}