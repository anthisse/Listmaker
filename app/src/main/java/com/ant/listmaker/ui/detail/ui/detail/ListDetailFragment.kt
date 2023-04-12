package com.ant.listmaker.ui.detail.ui.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ant.listmaker.MainActivity
import com.ant.listmaker.R
import com.ant.listmaker.TaskList
import com.ant.listmaker.databinding.ListDetailFragmentBinding
import com.ant.listmaker.ui.main.MainViewModel
import com.ant.listmaker.ui.main.MainViewModelFactory

class ListDetailFragment : Fragment() {

    lateinit var binding: ListDetailFragmentBinding

    companion object {
        fun newInstance() = ListDetailFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory(
            PreferenceManager.getDefaultSharedPreferences(requireActivity()))
        ).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = ListDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Notify adapter that list of tasks has updated, redraw RecyclerView
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Get viewModel data
        viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory(
            PreferenceManager.getDefaultSharedPreferences(requireActivity())))
            .get(MainViewModel::class.java)

        // Get a bundle and unpack it with getParcelable() and INTENT_LIST_KEY
        val list: TaskList? = arguments?.getParcelable(MainActivity.INTENT_LIST_KEY)

        // Ensure the list isn't null
        if (list != null) {
            // Assign list contents to the ViewModel
            viewModel.list = list
            requireActivity().title = list.name
        }

        // Notify the adapter that the task list has updated and redraw the RecyclerView
        val recyclerAdapter = ListItemsRecyclerViewAdapter(viewModel.list)
        binding.listItemsRecyclerview.adapter = recyclerAdapter
        binding.listItemsRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        viewModel.onTaskAdded = {
            recyclerAdapter.notifyDataSetChanged()
        }
    }
}