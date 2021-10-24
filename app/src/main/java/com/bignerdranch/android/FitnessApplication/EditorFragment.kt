package com.bignerdranch.android.FitnessApplication

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
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
            val savedStart = savedInstanceState?.getString(STARTTIME)
            val savedEnd = savedInstanceState?.getString(ENDTIME)
            binding.editorworkout.setText(savedWorkoutString?: it.workout)
            binding.editorlocation.setText(savedLocationString?: it.location)
            binding.radioGroup.check(savedInt?: it.workoutsolo)
            //binding.startTimeText.setText(savedStart?: it.starttime)
            binding.endTimeText.setText(savedEnd?: it.endtime)

            if(it.date != "") {
                binding.datetextView.setText(savedDate ?: it.date)
            }
            if(it.starttime != "") {
                binding.startTimeText.setText(savedStart ?: it.starttime)
            }

        })
        viewModel.getWorkOutById(args.workOutId)



        //date picker properties
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)
//        val hour = cal.get(Calendar.HOUR_OF_DAY)
//        val minute = cal.get(Calendar.MINUTE)

        //Set the date to today
        if (binding.datetextView.text == "Workout Date" || binding.datetextView.text == "") {
            binding.datetextView.setText("" + day + "/" + month + "/" + year)
        }

        UpdateTime()
        //Set the start time to today
//        if (binding.startTimeText.text == "Exercise Start Time" || binding.startTimeText.text == "") {
//            binding.startTimeText.setText("" + day + "/" + month)
//        }


        //Date button listener
        binding.dateButton.setOnClickListener{
            val dpDiag = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener{view, mYear, mMonth, mDay ->
                datetextView.setText("" + mDay + "/" + mMonth + "/" + mYear)
            }, year, month, day)
            dpDiag.show()
        }

        //Start time button listener
        //The following method updates the start time text
        binding.startTimeButton.setOnClickListener{
            UpdateTime()
        }

        //End time button listener
        //The following method updates the end time text
        binding.endTimeButton.setOnClickListener{
//            val cal = Calendar.getInstance()
//            cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY))
//            cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE))
            end_time_text.text = SimpleDateFormat("HH:mm:ss").format(calendarItem().time)
        }
        return binding.root
    }

    private fun UpdateTime(){
//        val cal = Calendar.getInstance()
//        cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY))
//        cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE))
        binding.startTimeText.text = SimpleDateFormat("HH:mm:ss").format(calendarItem().time)
    }

    private fun calendarItem (): Calendar {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY))
        cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE))
        return cal
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
        viewModel.currentWorkOut.value?.starttime = binding.startTimeText.text.toString()
        viewModel.currentWorkOut.value?.endtime = binding.endTimeText.text.toString()
        viewModel.updateWorkout()


        Toast.makeText(context,"WorkOut Updated",Toast.LENGTH_SHORT).show()

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