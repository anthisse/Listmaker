package com.ant.listmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.ant.listmaker.databinding.ActivityMainBinding
import com.ant.listmaker.databinding.FragmentMainBinding
import com.ant.listmaker.ui.detail.ListDetailActivity
import com.ant.listmaker.ui.main.MainFragment
import com.ant.listmaker.ui.main.MainViewModel
import com.ant.listmaker.ui.main.MainViewModelFactory

class MainActivity : AppCompatActivity(), MainFragment.MainFragmentInteractionListener {

    // Store binding
    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this,
        MainViewModelFactory(PreferenceManager.getDefaultSharedPreferences(this)))
            .get((MainViewModel::class.java))
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (savedInstanceState == null) {
            val mainFragment = MainFragment.newInstance(this)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, mainFragment)
                .commitNow()
        }

        // Listen for a click
        binding.fabButton.setOnClickListener {
            // Show dialog
            showCreateListDialog()
        }
    }

    // Show some dialog
    private fun showCreateListDialog() {
        // Get some strings
        val dialogTitle = getString(R.string.name_of_list)
        val positiveButtonTitle = getString(R.string.create_list)

        // Create a Builder and an EditText view
        val builder = AlertDialog.Builder(this)
        val listTitleEditText = EditText(this)
        listTitleEditText.inputType = InputType.TYPE_CLASS_TEXT

        // Set the view's title and text
        builder.setTitle(dialogTitle)
        builder.setView(listTitleEditText)

        // Add a positive button
        builder.setPositiveButton(positiveButtonTitle) { dialog, _ -> dialog.dismiss()

        val taskList = TaskList(listTitleEditText.text.toString())
            viewModel.saveList(taskList)
            showListDetail(taskList)
        }

        // Show the dialog
        builder.create().show()
    }

    private fun showListDetail(list : TaskList) {
        // Create an intent and pass in the current activity and its class
        val listDetailIntent = Intent(this, ListDetailActivity::class.java)

        // Add an Extra and send it a list and a key
        listDetailIntent.putExtra(INTENT_LIST_KEY, list)

        // Start a new activity
        startActivity(listDetailIntent)
    }

    companion object {
        const val INTENT_LIST_KEY = "list"
    }

    override fun listItemTapped(list: TaskList) {
        showListDetail(list)
    }
}