package com.ant.listmaker.ui.main

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.ant.listmaker.TaskList

class MainViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {

    // Inform the fragment that a new task is available if something is added
    lateinit var onListAdded: (() -> Unit)
    lateinit var onTaskAdded: (() -> Unit)
    lateinit var list: TaskList

    // Add tasks to the list and invoke the lambda
    fun addTask(task: String) {
        list.tasks.add(task)
        onTaskAdded.invoke()
    }

    // Until lists is called, it's empty
    val lists: MutableList<TaskList> by lazy {
        retrieveLists()
    }

    // Get saved TaskLists from SharedPreferences
    private fun retrieveLists() : MutableList<TaskList> {
        val sharedPreferencesContents = sharedPreferences.all
        val taskLists = ArrayList<TaskList>()

        // Loop through sharedPreferencesContents and recreate the TaskList objects
        for (taskList in sharedPreferencesContents) {
            val itemsHashSet = ArrayList(taskList.value as HashSet<String>)
            val list = TaskList(taskList.key, itemsHashSet)
            taskLists.add(list)
        }
        // Return the TaskLists
        return taskLists
    }

    // Save a TaskList to SharedPreferences
    fun saveList(list : TaskList) {
        // Convert to a HashSet to ensure unique values in the list
        sharedPreferences.edit().putStringSet(list.name, list.tasks.toHashSet()).apply()
        lists.add(list)

        // Alert fragment that the list was updated
        onListAdded.invoke()
    }

    // Write a list to SharedPreferences
    fun updateList(list: TaskList) {
        sharedPreferences.edit().putStringSet(list.name,
        list.tasks.toHashSet()).apply()
        lists.add(list)
    }

    // Clear a list and refill it from SharedPreferences
    fun refreshLists() {
        lists.clear()
        lists.addAll(retrieveLists())
    }
}