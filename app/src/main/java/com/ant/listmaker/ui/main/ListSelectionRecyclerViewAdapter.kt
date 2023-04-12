package com.ant.listmaker.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ant.listmaker.TaskList
import com.ant.listmaker.databinding.ListSelectionViewHolderBinding

class ListSelectionRecyclerViewAdapter(private val lists : MutableList<TaskList>) :
    RecyclerView.Adapter<ListSelectionViewHolder>() {

    // Hold some strings in an array
    val listTitles = arrayOf("Shopping List", "Chores", "Android Tutorials")

    // Inflate a layout, create a ListSelectionViewHolder, and return it
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSelectionViewHolder {
        val binding = ListSelectionViewHolderBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)

        return ListSelectionViewHolder(binding)
    }

    // Get the number of items RecyclerView has. Returns an Int.
    override fun getItemCount(): Int {
        return lists.size
    }

    // Bind the items in the array to the ViewHolder
    override fun onBindViewHolder(holder: ListSelectionViewHolder, position: Int) {

        holder.binding.itemNumber.text = (position + 1).toString()
        holder.binding.itemString.text = lists[position].name
    }

    // Notify that a new list needs to be displayed
    fun listsUpdated() {
        notifyItemInserted(lists.size - 1)
    }
}