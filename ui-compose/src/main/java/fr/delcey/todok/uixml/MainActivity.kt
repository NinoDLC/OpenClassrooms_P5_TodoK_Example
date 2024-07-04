package fr.delcey.todok.uixml

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commitNow
import dagger.hilt.android.AndroidEntryPoint
import fr.delcey.todok.uixml.databinding.MainActivityBinding
import fr.delcey.todok.uixml.add_task.AddTaskDialogFragment
import fr.delcey.todok.uixml.tasks.TasksFragment
import fr.delcey.todok.uixml.utils.viewBinding

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