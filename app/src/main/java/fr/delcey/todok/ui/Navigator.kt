package fr.delcey.todok.ui

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.scopes.ActivityScoped
import fr.delcey.todok.R
import fr.delcey.todok.domain.navigate.GetNavigationUseCase
import fr.delcey.todok.domain.navigate.NavigateUseCase
import fr.delcey.todok.domain.navigate.model.DestinationEntity
import fr.delcey.todok.ui.add_task.AddTaskDialogFragment
import fr.delcey.todok.ui.task_detail.TaskDetailActivity
import fr.delcey.todok.ui.tasks.TasksFragment
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityScoped
class Navigator @Inject constructor(
    private val activity: FragmentActivity,
    private val navigateUseCase: NavigateUseCase,
    private val getNavigationUseCase: GetNavigationUseCase,
) {
    companion object {
        private const val TAG_DIALOG_FRAGMENT = "TAG_DIALOG_FRAGMENT"
    }

    init {
        activity.lifecycleScope.launch {
            activity.repeatOnLifecycle(Lifecycle.State.STARTED) {
                getNavigationUseCase.invoke().collect { destination ->
                    handle(destination)
                }
            }
        }
    }

    // TODO NINO Fausse bonne idée ?
    fun navigate(destination: DestinationEntity) {
        activity.lifecycleScope.launch {
            navigateUseCase.invoke(destination)
        }
    }

    private fun handle(destination: DestinationEntity) {
        when (destination) {
            is DestinationEntity.Activity -> handleActivityDestination(destination)
            is DestinationEntity.Fragment -> handleFragmentDestination(destination)
            is DestinationEntity.DialogFragment -> handleDialogFragmentDestination(destination)
            is DestinationEntity.Back -> activity.supportFragmentManager.popBackStack()
            is DestinationEntity.Dismiss -> (activity.supportFragmentManager.findFragmentByTag(TAG_DIALOG_FRAGMENT) as? DialogFragment)?.dismiss()
            is DestinationEntity.FinishActivity -> activity.finish()
        }
    }

    private fun handleActivityDestination(destination: DestinationEntity.Activity) {
        activity.startActivity(
            when (destination) {
                is DestinationEntity.Activity.Detail -> TaskDetailActivity.navigate(activity, destination.taskId)
            }
        )
    }

    private fun handleFragmentDestination(destination: DestinationEntity.Fragment) {
        activity.supportFragmentManager.commit {
            replace(
                R.id.main_FrameLayout_container,
                when (destination) {
                    is DestinationEntity.Fragment.Tasks -> TasksFragment.newInstance() // TODO NINO Reflection because we are cool ?
                }
            )
        }
    }

    private fun handleDialogFragmentDestination(destination: DestinationEntity.DialogFragment) {
        when (destination) {
            is DestinationEntity.DialogFragment.AddTask -> AddTaskDialogFragment.newInstance()
        }.show(activity.supportFragmentManager, TAG_DIALOG_FRAGMENT)
    }
}
