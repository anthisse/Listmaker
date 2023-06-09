package com.ant.listmaker.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ant.listmaker.TaskList
import com.ant.listmaker.databinding.ListSelectionViewHolderBinding

class ListSelectionRecyclerViewAdapter(private val lists : MutableList<TaskList>, val clickListener:
ListSelectionRecyclerViewClickListener) : RecyclerView.Adapter<ListSelectionViewHolder>() {

    interface ListSelectionRecyclerViewClickListener {
        fun listItemClicked(list: TaskList)
    }

    // Create a View from the Layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSelectionViewHolder {
        val binding = ListSelectionViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListSelectionViewHolder(binding)
    }

    // Populate TextViews with appropriate positions and strings
    override fun onBindViewHolder(holder: ListSelectionViewHolder, position: Int) {
        holder.binding.itemNumber.text = (position + 1).toString()
        holder.binding.itemString.text = lists[position].name
        holder.itemView.setOnClickListener {
            clickListener.listItemClicked(lists[position])
        }
    }

    // Get the number of items in a list
    override fun getItemCount(): Int {
        return lists.size
    }

    // Notify RecyclerView that the list changed
    fun listsUpdated() {
        notifyItemInserted(lists.size-1)
    }
}
