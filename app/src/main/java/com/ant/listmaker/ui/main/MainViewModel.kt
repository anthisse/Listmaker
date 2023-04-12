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

    val lists: MutableList<TaskList> by lazy {
        retrieveLists()
    }

    private fun retrieveLists() : MutableList<TaskList> {
        val sharedPreferencesContents = sharedPreferences.all
        val taskLists = ArrayList<TaskList>()

        for (taskList in sharedPreferencesContents) {
            val itemsHashSet = ArrayList(taskList.value as HashSet<String>)
            val list = TaskList(taskList.key, itemsHashSet)
            taskLists.add(list)
        }
        return taskLists
    }

    fun saveList(list : TaskList) {
        sharedPreferences.edit().putStringSet(list.name, list.tasks.toHashSet()).apply()
        lists.add(list)
        onListAdded.invoke()
    }

    // Write a list to SharedPreferences
    fun updateList(list: TaskList) {
        sharedPreferences.edit().putStringSet(list.name,
        list.tasks.toHashSet()).apply()
        lists.add(list)
    }

    // Clear li
    fun refreshLists() {
        lists.clear()
        lists.addAll(retrieveLists())
    }
}