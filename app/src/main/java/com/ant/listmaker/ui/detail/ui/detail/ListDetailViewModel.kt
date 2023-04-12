package com.ant.listmaker.ui.detail.ui.detail

import androidx.lifecycle.ViewModel
import com.ant.listmaker.TaskList

class ListDetailViewModel : ViewModel() {
    // Inform the fragment that a new task is available
    lateinit var onTaskAdded: (() -> Unit)
    lateinit var list: TaskList

    // Add tasks to the list and invoke the lambda
    fun addTask(task: String) {
        list.tasks.add(task)
        onTaskAdded.invoke()
    }
}