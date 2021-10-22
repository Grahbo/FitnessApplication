package com.bignerdranch.android.FitnessApplication

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.DatePicker
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.FitnessApplication.databinding.EditorFragmentBinding
import kotlinx.android.synthetic.main.editor_fragment.*
import java.text.SimpleDateFormat
import java.util.*

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
        binding.datetextView.setText("")

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    saveAndReturn()
                }
        })

        //bind data from DB
        viewModel.currentWorkOut.observe(viewLifecycleOwner, Observer {
            val savedWorkoutString = savedInstanceState?.getString(FITNESS_TEXT_KEY)
            val savedLocationString = savedInstanceState?.getString(SELECTED_LOCATION_KEY)
            val savedInt = savedInstanceState?.getInt(SELECTED_FITNESS_CHECK)
            val savedDate = savedInstanceState?.getString(SELECTED_DATE)
            binding.editorworkout.setText(savedWorkoutString?: it.workout)
            binding.editorlocation.setText(savedLocationString?: it.location)
            binding.radioGroup.check(savedInt?: it.workoutsolo)

            if(it.date != "") {
                binding.datetextView.setText(savedDate ?: it.date)
            }

        })
        viewModel.getWorkOutById(args.workOutId)



        //date picker properties
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)

        //Set the date to today
        if (binding.datetextView.text == "Workout Date" || binding.datetextView.text == "") {
            binding.datetextView.setText("" + day + "/" + month + "/" + year)
        }

        //Date button listener
        binding.dateButton.setOnClickListener{
            val dpDiag = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener{view, mYear, mMonth, mDay ->

                datetextView.setText("" + mDay + "/" + mMonth + "/" + mYear)
            }, year, month, day)
            dpDiag.show()
        }

        //Start time button listener
        binding.startTimeButton.setOnClickListener{
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                start_time_text.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(requireContext(), timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        //End time button listener
        binding.endTimeButton.setOnClickListener{
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                end_time_text.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(requireContext(), timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }
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
        viewModel.currentWorkOut.value?.workout = binding.editorworkout.text.toString()
        viewModel.currentWorkOut.value?.location = binding.editorlocation.text.toString()
        viewModel.currentWorkOut.value?.workoutsolo = binding.radioGroup.checkedRadioButtonId
        viewModel.currentWorkOut.value?.date = binding.datetextView.text.toString()
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
            outState.putString(SELECTED_DATE, datetextView.text.toString())
        }
        super.onSaveInstanceState(outState)
    }

}