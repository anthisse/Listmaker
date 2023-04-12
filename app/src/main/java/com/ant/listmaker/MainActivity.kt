package com.ant.listmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.ant.listmaker.databinding.ActivityMainBinding
import com.ant.listmaker.databinding.FragmentMainBinding
//import com.ant.listmaker.models.TaskList // TODO this is broken
import com.ant.listmaker.ui.main.MainFragment
import com.ant.listmaker.ui.main.MainViewModel
import com.ant.listmaker.ui.main.MainViewModelFactory

class MainActivity : AppCompatActivity() {

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
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
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
        builder.setPositiveButton(positiveButtonTitle) { dialog, _ ->dialog.dismiss()
        viewModel.saveList(TaskList(listTitleEditText.text.toString()))}

        // Show the dialog
        builder.create().show()
    }
}