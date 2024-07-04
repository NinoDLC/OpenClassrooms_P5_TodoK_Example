package fr.delcey.todok.uixml.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.delcey.todok.uicompose.databinding.TaskEmptyStateItemBinding
import fr.delcey.todok.uicompose.databinding.TaskHeaderItemBinding
import fr.delcey.todok.uicompose.databinding.TaskItemBinding

class TaskAdapter : ListAdapter<TasksViewStateItem, TaskAdapter.TaskViewHolder>(TaskDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder = when (TasksViewStateItem.Type.values()[viewType]) {
        TasksViewStateItem.Type.EMPTY_STATE -> TaskViewHolder.EmptyState.create(parent)
        TasksViewStateItem.Type.HEADER -> TaskViewHolder.Header.create(parent)
        TasksViewStateItem.Type.TASK -> TaskViewHolder.Task.create(parent)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        when (holder) {
            is TaskViewHolder.EmptyState -> Unit
            is TaskViewHolder.Header -> holder.bind(item = getItem(position) as TasksViewStateItem.Header)
            is TaskViewHolder.Task -> holder.bind(item = getItem(position) as TasksViewStateItem.Task)
        }
    }

    override fun getItemViewType(position: Int): Int = getItem(position).type.ordinal

    sealed class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        class EmptyState(private val binding: TaskEmptyStateItemBinding) : TaskViewHolder(binding.root) {
            companion object {
                fun create(parent: ViewGroup) = EmptyState(
                    binding = TaskEmptyStateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
        }

        class Header(private val binding: TaskHeaderItemBinding) : TaskViewHolder(binding.root) {
            companion object {
                fun create(parent: ViewGroup) = Header(
                    binding = TaskHeaderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }

            fun bind(item: TasksViewStateItem.Header) {
                binding.taskHeaderItemTextViewTitle.text = item.title
            }
        }

        class Task(private val binding: TaskItemBinding) : TaskViewHolder(binding.root) {
            companion object {
                fun create(parent: ViewGroup) = Task(
                    binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }

            fun bind(item: TasksViewStateItem.Task) {
                binding.root.tag = item.taskId
                binding.taskItemImageViewColor.setColorFilter(item.projectColor)
                binding.taskItemTextViewDescription.text = item.description
            }
        }
    }

    object TaskDiffCallback : DiffUtil.ItemCallback<TasksViewStateItem>() {
        override fun areItemsTheSame(oldItem: TasksViewStateItem, newItem: TasksViewStateItem): Boolean = when {
            oldItem is TasksViewStateItem.Header && newItem is TasksViewStateItem.Header -> oldItem.title == newItem.title
            oldItem is TasksViewStateItem.Task && newItem is TasksViewStateItem.Task -> oldItem.taskId == newItem.taskId
            oldItem is TasksViewStateItem.EmptyState && newItem is TasksViewStateItem.EmptyState -> true
            else -> false
        }

        override fun areContentsTheSame(oldItem: TasksViewStateItem, newItem: TasksViewStateItem): Boolean = oldItem == newItem
    }
}
