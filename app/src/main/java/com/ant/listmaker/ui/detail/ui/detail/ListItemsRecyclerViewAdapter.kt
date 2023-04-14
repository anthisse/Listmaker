package com.ant.listmaker.ui.detail.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ant.listmaker.TaskList
import com.ant.listmaker.databinding.ListItemViewHolderBinding

class ListItemsRecyclerViewAdapter(var list: TaskList):
    RecyclerView.Adapter<ListItemViewHolder>() {

    // Use the binding generated for the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding = ListItemViewHolderBinding.inflate(LayoutInflater.from(parent.context),
        parent, false)
        return ListItemViewHolder(binding)
    }

    // Bind a TextView to a task from the list
    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.binding.textViewTask.text = list.tasks[position]
    }

    // Return the number of tasks in a list
    override fun getItemCount(): Int {
        return list.tasks.size
    }
}