package com.bignerdranch.android.FitnessApplication

import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.RadioGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.FitnessApplication.databinding.EditorFragmentBinding
import kotlinx.android.synthetic.main.editor_fragment.*

class EditorFragment : Fragment() {


    private lateinit var viewModel: EditorViewModel
    private val args: EditorFragmentArgs by navArgs()
    private lateinit var binding: EditorFragmentBinding

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity).supportActionBar?.let {
            it.setHomeButtonEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_check)
        }
        setHasOptionsMenu(true)


        requireActivity().title =
            if(args.workOutId == NEW_WORKOUT_ID) {
                getString(R.string.new_workout)
            }else{
                getString(R.string.edit_workout)
            }

        viewModel = ViewModelProvider(this).get(EditorViewModel::class.java)

        binding = EditorFragmentBinding.inflate(inflater, container, false)
        binding.editorworkout.setText("")
        binding.editorlocation.setText("")
        binding.radioGroup.check(0)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    saveAndReturn()
                }
        })

        //start observing the elements
        viewModel.currentWorkOut.observe(viewLifecycleOwner, Observer {
            val savedWorkoutString = savedInstanceState?.getString(FITNESS_TEXT_KEY)
            val savedLocationString = savedInstanceState?.getString(SELECTED_LOCATION_KEY)
            val savedInt = savedInstanceState?.getInt(SELECTED_FITNESS_CHECK)
            binding.editorworkout.setText(savedWorkoutString?: it.text)
            binding.editorlocation.setText(savedLocationString?: it.location)
            binding.radioGroup.check(savedInt?: it.workoutsolo)

        })
        viewModel.getWorkOutById(args.workOutId)

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> saveAndReturn()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveAndReturn(): Boolean {

        //hide keyboard after edit and fragment has closed
        val imm = requireActivity()
            .getSystemService(Activity.INPUT_METHOD_SERVICE)as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)

        //load the database information
        viewModel.currentWorkOut.value?.text = binding.editorworkout.text.toString()
        viewModel.currentWorkOut.value?.location = binding.editorlocation.text.toString()
        viewModel.currentWorkOut.value?.workoutsolo = binding.radioGroup.checkedRadioButtonId
        viewModel.updateNote()

        findNavController().navigateUp()
        return true
    }

    //the following method deals with rotating the device
    override fun onSaveInstanceState(outState: Bundle) {
        with(binding.editorworkout){
            outState.putString(FITNESS_TEXT_KEY, editorworkout.text.toString())
            outState.putString(SELECTED_LOCATION_KEY, editorlocation.text.toString())
            //outState.putInt(CURSOR_POSITION_KEY, selectionStart)
            outState.putInt(SELECTED_FITNESS_CHECK, radioGroup.checkedRadioButtonId)
        }
        super.onSaveInstanceState(outState)
    }

}