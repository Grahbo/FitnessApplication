package com.bignerdranch.android.FitnessApplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.FitnessApplication.data.WorkOutEntity
import com.bignerdranch.android.FitnessApplication.databinding.MainFragmentBinding

class MainFragment : Fragment(),
    WorkOutListAdapter.ListItemListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding
    private lateinit var adapter: WorkOutListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity)
            .supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(true)

        requireActivity().title = getString(R.string.app_name)

        binding = MainFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        with(binding.recyclerView) {
            setHasFixedSize(true)
            val divider = DividerItemDecoration(
                context, LinearLayoutManager(context).orientation
            )
            addItemDecoration(divider)
        }

        viewModel.workoutsList?.observe(viewLifecycleOwner, Observer {
            Log.i("noteLogging", it.toString())
            adapter = WorkOutListAdapter(it, this@MainFragment)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(activity)

            val selectedworkouts =
                savedInstanceState?.getParcelableArrayList<WorkOutEntity>(SELECTED_FITNESS_KEY)
            adapter.selectedworkouts.addAll(selectedworkouts ?: emptyList())

        })

        binding.floatingActionButton.setOnClickListener {
            editWorkOut(NEW_WORKOUT_ID)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuId =
            if (this::adapter.isInitialized && adapter.selectedworkouts.isNotEmpty()) {
                R.menu.menu_main_selected_items
            } else {
                R.menu.menu_main
            }
        inflater.inflate(menuId, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> deleteSelectedworkouts()
            R.id.action_delete_all_data -> deleteAllworkouts()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteAllworkouts(): Boolean {
        viewModel.deleteAllworkouts()
        return true
    }

    private fun deleteSelectedworkouts(): Boolean {
        viewModel.deleteworkouts(adapter.selectedworkouts)
        Handler(Looper.getMainLooper()).postDelayed({
            adapter.selectedworkouts.clear()
            requireActivity().invalidateOptionsMenu()
        }, 100)
        return true
    }

    override fun editWorkOut(workOutId: Int) {
        Log.i(TAG, "onItemClick: received note id $workOutId")
        val action = MainFragmentDirections.actionEditNote(workOutId)
        findNavController().navigate(action)
    }

    override fun onItemSelectionChanged() {
        requireActivity().invalidateOptionsMenu()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (this::adapter.isInitialized) {
            outState.putParcelableArrayList(
                SELECTED_FITNESS_KEY,
                adapter.selectedworkouts
            )
        }
        super.onSaveInstanceState(outState)
    }
}