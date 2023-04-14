package com.ant.listmaker.ui.main

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Constructor
class MainViewModelFactory(private val sharedPreferences: SharedPreferences) : ViewModelProvider.Factory {

    // Override create() method. Return an instance of MainViewModel
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(sharedPreferences) as T
    }
}
