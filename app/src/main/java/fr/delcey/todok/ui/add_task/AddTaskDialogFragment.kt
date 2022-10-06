package fr.delcey.todok.ui.add_task

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import fr.delcey.todok.R
import fr.delcey.todok.databinding.AddTaskDialogFragmentBinding
import fr.delcey.todok.ui.utils.viewBinding

@AndroidEntryPoint
class AddTaskDialogFragment : DialogFragment(R.layout.add_task_dialog_fragment) {

    companion object {
        fun newInstance() = AddTaskDialogFragment()
    }

    private val binding by viewBinding { AddTaskDialogFragmentBinding.bind(it) }
    private val viewModel by viewModels<AddTaskViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AddTaskProjectSpinnerAdapter()
        binding.createTaskAutoCompleteTextViewProjects.setAdapter(adapter)
        binding.createTaskAutoCompleteTextViewProjects.setOnItemClickListener { _, _, position, _ ->
            adapter.getItem(position)?.let {
                viewModel.onProjectSelected(it.projectId)
            }
        }
        binding.createTaskTextInputEditTextDescription.doAfterTextChanged {
            viewModel.onTaskDescriptionChanged(it?.toString())
        }
        binding.createTaskButtonCancel.setOnClickListener { dismiss() }
        binding.createTaskButtonOk.setOnClickListener { viewModel.onOkButtonClicked() }

        viewModel.viewStateLiveData.observe(this) { addTaskViewState ->
            adapter.setData(addTaskViewState.items)
            binding.createTaskButtonOk.visibility = if (addTaskViewState.isOkButtonVisible) View.VISIBLE else View.INVISIBLE
            binding.createTaskProgressBar.visibility = if (addTaskViewState.isProgressBarVisible) View.VISIBLE else View.INVISIBLE
        }
        viewModel.singleLiveEvent.observe(this) { addTaskEvent: AddTaskEvent ->
            when (addTaskEvent) {
                is AddTaskEvent.Dismiss -> dismiss()
                is AddTaskEvent.Toast -> Toast.makeText(
                    requireContext(),
                    addTaskEvent.text.toCharSequence(requireContext()),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}