package fr.delcey.todok.ui.tasks

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import fr.delcey.todok.R
import fr.delcey.todok.databinding.TasksFragmentBinding
import fr.delcey.todok.ui.utils.viewBinding

@AndroidEntryPoint
class TasksFragment : Fragment(R.layout.tasks_fragment) {

    companion object {
        fun newInstance() = TasksFragment()
    }

    private val binding by viewBinding { TasksFragmentBinding.bind(it) }
    private val viewModel by viewModels<TasksViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TaskAdapter()
        binding.tasksRecyclerView.adapter = adapter
        binding.tasksFloatingActionButtonAddTask.setOnClickListener { viewModel.onAddButtonClicked() }
        binding.tasksFloatingActionButtonAddTask.setOnLongClickListener {
            viewModel.onAddButtonLongClicked()
            true
        }
        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { taskViewStates ->
            adapter.submitList(taskViewStates)
        }

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.task_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when (menuItem.itemId) {
                R.id.sort_task -> {
                    viewModel.onSortButtonClicked()
                    true
                }
                else -> false
            }
        })
    }
}