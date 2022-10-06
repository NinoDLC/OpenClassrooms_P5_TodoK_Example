package fr.delcey.todok.ui.task_detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import fr.delcey.todok.databinding.TaskDetailActivityBinding
import fr.delcey.todok.ui.BaseActivity
import fr.delcey.todok.ui.utils.viewBinding

@AndroidEntryPoint
class TaskDetailActivity : BaseActivity() {

    companion object {
        private const val EXTRA_TASK_ID = "EXTRA_TASK_ID"

        fun navigate(context: Context, taskId: Long) = Intent(context, TaskDetailActivity::class.java).apply {
            putExtra(EXTRA_TASK_ID, taskId)
        }
    }

    private val binding by viewBinding { TaskDetailActivityBinding.inflate(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.taskDetailTextView.text = "Task id: ${intent.getLongExtra(EXTRA_TASK_ID, 0)}"
    }
}