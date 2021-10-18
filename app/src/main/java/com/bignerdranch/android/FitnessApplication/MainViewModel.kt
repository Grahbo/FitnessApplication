package com.bignerdranch.android.FitnessApplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.FitnessApplication.data.AppDB
import com.bignerdranch.android.FitnessApplication.data.WorkOutEntity
import com.bignerdranch.android.FitnessApplication.data.SampleDataProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val database = AppDB.getInstance(app)

    val notesList = database?.noteDao()?.getAll()

    fun addSampleData (){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val sampleNotes = SampleDataProvider.getNotes()
                database?.noteDao()?.insertAll(sampleNotes)
            }
        }
    }

    fun deleteNotes(selectedWorkOuts: List<WorkOutEntity>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                database?.noteDao()?.deleteWorkOut(selectedWorkOuts)
            }
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                database?.noteDao()?.deleteAllWorkOuts()
            }
        }
    }

}