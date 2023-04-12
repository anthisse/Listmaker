package com.ant.listmaker

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.ant.listmaker.databinding.ActivityMainBinding
import com.ant.listmaker.ui.detail.ListDetailActivity
import com.ant.listmaker.ui.detail.ui.detail.ListDetailFragment
import com.ant.listmaker.ui.main.MainFragment
import com.ant.listmaker.ui.main.MainViewModel
import com.ant.listmaker.ui.main.MainViewModelFactory

class MainActivity : AppCompatActivity(), MainFragment.MainFragmentInteractionListener {

    // Store binding
    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(PreferenceManager.getDefaultSharedPreferences(this))
        )
            .get((MainViewModel::class.java))
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (savedInstanceState == null) {
            val mainFragment = MainFragment.newInstance()
            mainFragment.clickListener = this

            val fragmentContainerViewId: Int = if (binding.mainFragmentContainer == null) {
                R.id.detail_container
            } else {
                R.id.main_fragment_container
            }

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(fragmentContainerViewId, mainFragment)
            }

            // Listen for a click
            binding.fabButton.setOnClickListener {
                // Show dialog
                showCreateListDialog()
            }
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

        val taskList: TaskList = TaskList(listTitleEditText.text.toString())
            this.viewModel.saveList(taskList)
            showListDetail(taskList)
        }

        // Show the dialog
        builder.create().show()
    }

    private fun showListDetail(list : TaskList) {
        // If the FragmentContainerView is null, use the smaller layout
        if (binding.mainFragmentContainer == null) {
            // Create an intent and pass in the current activity and its class
            val listDetailIntent = Intent(this, ListDetailActivity::class.java)

            // Add an Extra and send it a list and a key
            listDetailIntent.putExtra(INTENT_LIST_KEY, list)

            // Start a new activity
            startActivityForResult(
                listDetailIntent,
                LIST_DETAIL_REQUEST_CODE
            )
        } else {
            // Otherwise, bundle the data
            val bundle = bundleOf(INTENT_LIST_KEY to list)
            supportFragmentManager.commit {

                binding.fabButton.setOnClickListener {
                    showCreateListDialog() // This is suspicious. Should be showCreateTaskDialog()?
                }
                setReorderingAllowed(true)

                // Create a new fragment
                replace(R.id.list_detail_fragment_container,
                ListDetailFragment::class.java, bundle, null)
            }
        }
    }

    private fun ShowCreateTaskDialog() {

        // Create an EditText to get user input
        val taskEditText = EditText(this)
        taskEditText.inputType = InputType.TYPE_CLASS_TEXT

        // Create and show the dialog
        AlertDialog.Builder(this)
            .setTitle(R.string.task_to_add)
            .setView(taskEditText)
            .setPositiveButton(R.string.add_task) { dialog, _ ->
                val task = taskEditText.text.toString()
                viewModel.addTask(task)
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun listItemTapped(list: TaskList) {
        showListDetail(list)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LIST_DETAIL_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let { viewModel.updateList(data.getParcelableExtra(INTENT_LIST_KEY)!!)
            viewModel.refreshLists()}
        }
    }

    // Reset the FAB to create lists when back is pressed
    override fun onBackPressed() {
        // Get a Fragment
        val listDetailFragment = supportFragmentManager.findFragmentById(
            R.id.list_detail_fragment_container
        )
        // If we didn't get a Fragment, close the Activity
        if (listDetailFragment == null) {
            super.onBackPressed()
        } else {
            // Otherwise, reset the Activity to its original state
            title = resources.getString(R.string.app_name) // Set the app title

            // Remove listDetailFragment
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                remove(listDetailFragment)
            }

            // Reset the FAB to create a list when tapped
            binding.fabButton.setOnClickListener {
                showCreateListDialog()
            }
        }
    }
    // Companion object
    companion object {
        const val INTENT_LIST_KEY = "list"
        const val LIST_DETAIL_REQUEST_CODE = 123
    }

}