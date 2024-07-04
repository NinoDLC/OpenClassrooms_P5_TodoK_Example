package fr.delcey.todok.uixml.add_task

import android.database.DataSetObservable
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ListAdapter
import fr.delcey.todok.uixml.databinding.AddTaskProjectSpinnerItemBinding

class AddTaskProjectSpinnerAdapter : ListAdapter, Filterable {

    private val dataSetObservable = DataSetObservable()
    private var items = emptyList<AddTaskViewStateItem>()

    override fun registerDataSetObserver(observer: DataSetObserver) {
        dataSetObservable.registerObserver(observer)
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver) {
        dataSetObservable.unregisterObserver(observer)
    }

    override fun areAllItemsEnabled(): Boolean = true

    override fun isEnabled(position: Int): Boolean = true

    override fun getItemViewType(position: Int): Int = 0

    override fun getViewTypeCount(): Int = 1

    override fun isEmpty(): Boolean = count == 0

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): AddTaskViewStateItem? = items.getOrNull(position)

    override fun getItemId(position: Int): Long = getItem(position)?.projectId ?: -1

    override fun hasStableIds(): Boolean = true

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = if (convertView != null) {
            AddTaskProjectSpinnerItemBinding.bind(convertView)
        } else {
            AddTaskProjectSpinnerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        }
        getItem(position)?.let {
            binding.addTaskProjectItemImageViewColor.setColorFilter(it.projectColor)
            binding.addTaskProjectItemTextViewName.text = it.projectName
        }
        return binding.root
    }

    override fun getFilter() = object : Filter() {
        override fun performFiltering(constraint: CharSequence) = FilterResults()
        override fun publishResults(constraint: CharSequence, results: FilterResults) {}
        override fun convertResultToString(resultValue: Any): CharSequence {
            return (resultValue as AddTaskViewStateItem).projectName
        }
    }

    fun setData(items: List<AddTaskViewStateItem>) {
        this.items = items
        dataSetObservable.notifyChanged()
    }
}