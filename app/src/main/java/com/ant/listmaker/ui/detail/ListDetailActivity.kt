package com.ant.listmaker.ui.detail

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.ant.listmaker.MainActivity
import com.ant.listmaker.R
import com.ant.listmaker.databinding.ListDetailActivityBinding
import com.ant.listmaker.ui.detail.ui.detail.ListDetailFragment
import com.ant.listmaker.ui.detail.ui.detail.ListDetailViewModel
import com.ant.listmaker.ui.main.MainViewModel
import com.ant.listmaker.ui.main.MainViewModelFactory

class ListDetailActivity : AppCompatActivity() {

    lateinit var binding: ListDetailActivityBinding
    lateinit var viewModel: MainViewModel
    lateinit var fragment: ListDetailFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout with binding
        binding = ListDetailActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Set a listener for the task button. When it's clicked, show the task dialog
        binding.addTaskButton.setOnClickListener {
            showCreateTaskDialog()
        }
        // Get the ViewModel for this activity
        viewModel = ViewModelProvider(this, MainViewModelFactory(
            PreferenceManager.getDefaultSharedPreferences(this)))
            .get(MainViewModel::class.java)
        viewModel.list = intent.getParcelableExtra(MainActivity.INTENT_LIST_KEY)!!

        title = viewModel.list.name

        fragment = ListDetailFragment.newInstance()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.detail_container, fragment)
                .commitNow()
        }
    }

    private fun showCreateTaskDialog() {
        // Create an EditText object to get input from user
        val taskEditText = EditText(this)
        taskEditText.inputType = InputType.TYPE_CLASS_TEXT

        // Set up some attributes of AlertDialog
        AlertDialog.Builder(this).setTitle(R.string.task_to_add)
            .setView(taskEditText)
            .setPositiveButton(R.string.add_task) { dialog, _ ->
                val task = taskEditText.text.toString() // Get text and create a task
                viewModel.addTask(task) // Add task to the ViewModel
                dialog.dismiss()// Dismiss the dialog
            }
            // Create and show the AlertDialog
            .create()
            .show()
    }

    // When the back button is pressed, bundle the list and put it in an Intent
    override fun onBackPressed() {
        val bundle = Bundle()
        bundle.putParcelable(MainActivity.INTENT_LIST_KEY, viewModel.list)

        val intent = Intent()
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }
}