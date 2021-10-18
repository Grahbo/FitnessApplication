package com.bignerdranch.android.FitnessApplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.FitnessApplication.data.AppDataBase
import com.bignerdranch.android.FitnessApplication.data.NoteEntity
import com.bignerdranch.android.FitnessApplication.data.SampleDataProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val database = AppDataBase.getInstance(app)

    val notesList = database?.noteDao()?.getAll()

    fun addSampleData (){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val sampleNotes = SampleDataProvider.getNotes()
                database?.noteDao()?.insertAll(sampleNotes)
            }
        }
    }

    fun deleteNotes(selectedNotes: List<NoteEntity>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                database?.noteDao()?.deleteNotes(selectedNotes)
            }
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                database?.noteDao()?.deleteAllNotes()
            }
        }
    }

}