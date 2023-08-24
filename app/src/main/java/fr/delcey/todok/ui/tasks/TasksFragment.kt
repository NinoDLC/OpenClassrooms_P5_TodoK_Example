package fr.delcey.todok.ui.tasks

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import fr.delcey.todok.R
import fr.delcey.todok.databinding.TaskItemBinding
import fr.delcey.todok.databinding.TasksFragmentBinding
import fr.delcey.todok.ui.NavigationListener
import fr.delcey.todok.ui.utils.viewBinding

@AndroidEntryPoint
class TasksFragment : Fragment(R.layout.tasks_fragment) {

    companion object {
        fun newInstance() = TasksFragment()
    }

    private val binding by viewBinding { TasksFragmentBinding.bind(it) }
    private val viewModel by viewModels<TasksViewModel>()

    private val maxDeleteSwipePx by lazy {
        resources.getDimensionPixelSize(R.dimen.max_delete_swipe).toFloat()
    }

    private lateinit var navigationListener: NavigationListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigationListener = context as NavigationListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TaskAdapter()
        binding.tasksRecyclerView.adapter = adapter
        binding.tasksFloatingActionButtonAddTask.setOnClickListener { viewModel.onAddButtonClicked() }
        binding.tasksFloatingActionButtonAddTask.setOnLongClickListener {
            viewModel.onAddButtonLongClicked()
            true
        }

        setupSwipe()

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { taskViewStates ->
            adapter.submitList(taskViewStates)
        }
        viewModel.singleLiveEvent.observe(viewLifecycleOwner) { tasksEvent ->
            when (tasksEvent) {
                TasksEvent.NavigateToAddTask -> navigationListener.displayAddTaskDialog()
            }
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

    private fun setupSwipe() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.START or ItemTouchHelper.END
        ) {
            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float = maxDeleteSwipePx / viewHolder.itemView.width

            override fun onChildDraw(
                canvas: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDrawOver(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val binding = TaskItemBinding.bind(viewHolder.itemView)
                    binding.taskItemCardView.translationX = dX.coerceIn(-maxDeleteSwipePx..maxDeleteSwipePx)
                }
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val taskId = viewHolder.itemView.tag as Long
                viewModel.onTaskSwiped(taskId)
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)

                val binding = TaskItemBinding.bind(viewHolder.itemView)
                binding.taskItemCardView.translationX = 0f
            }

            // Mandatory but useless when swiping, ignore
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false
        }).attachToRecyclerView(binding.tasksRecyclerView)
    }
}