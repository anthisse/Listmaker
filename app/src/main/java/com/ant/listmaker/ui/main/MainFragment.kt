package com.ant.listmaker.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ant.listmaker.TaskList
import com.ant.listmaker.databinding.FragmentMainBinding
import kotlin.properties.Delegates
import org.intellij.lang.annotations.JdkConstants.ListSelectionMode

class MainFragment : Fragment(),
    ListSelectionRecyclerViewAdapter.ListSelectionRecyclerViewClickListener {

    lateinit var clickListener: MainFragmentInteractionListener
    interface MainFragmentInteractionListener {
        fun listItemTapped(list: TaskList)
    }

    private lateinit var binding: FragmentMainBinding

    private lateinit var viewModel : MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.listsRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }

    // Override onActivityCreated to retrieve a ViewModel
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(),
            MainViewModelFactory(PreferenceManager.getDefaultSharedPreferences(requireActivity())))
            .get(MainViewModel::class.java)

        val recyclerViewAdapter = ListSelectionRecyclerViewAdapter(viewModel.lists, this)

        binding.listsRecyclerview.adapter = recyclerViewAdapter

        viewModel.onListAdded = {
            recyclerViewAdapter.listsUpdated()
        }
    }

    // Inform the Fragment which list item was tapped
    override fun listItemClicked(list: TaskList) {
        clickListener.listItemTapped(list)
    }
    companion object {
        fun newInstance() = MainFragment()
    }
}
