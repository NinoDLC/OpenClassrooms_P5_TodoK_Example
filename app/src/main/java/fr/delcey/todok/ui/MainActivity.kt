package fr.delcey.todok.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commitNow
import dagger.hilt.android.AndroidEntryPoint
import fr.delcey.todok.databinding.MainActivityBinding
import fr.delcey.todok.ui.add_task.AddTaskDialogFragment
import fr.delcey.todok.ui.tasks.TasksFragment
import fr.delcey.todok.ui.utils.viewBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationListener {

    private val binding by viewBinding { MainActivityBinding.inflate(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.commitNow {
                replace(binding.mainFrameLayout.id, TasksFragment.newInstance())
            }
        }
    }

    override fun displayAddTaskDialog() {
        AddTaskDialogFragment.newInstance().show(supportFragmentManager, null)
    }
}