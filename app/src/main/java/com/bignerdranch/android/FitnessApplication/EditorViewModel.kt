package com.bignerdranch.android.FitnessApplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.FitnessApplication.data.AppDB
import com.bignerdranch.android.FitnessApplication.data.WorkOutEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditorViewModel(app: Application) : AndroidViewModel(app) {
    private val database = AppDB.getInstance(app)
    val currentNote = MutableLiveData<WorkOutEntity>()

    fun getNoteById(noteId: Int){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val note =
                    if (noteId != NEW_WORKOUT_ID){
                        database?.noteDao()?.getWorkOutById(noteId)
                    }else{
                        WorkOutEntity()
                    }
                currentNote.postValue(note!!)
            }
        }
    }

    fun updateNote() {
        currentNote.value?.let{
            it.text = it.text.trim()
            if(it.id == NEW_WORKOUT_ID && it.text.isEmpty()){
                return
            }

            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    if(it.text.isEmpty()){
                        database?.noteDao()?.deleteWorkOut(it)
                    }else{
                        database?.noteDao()?.insertNote(it)
                    }
                }
            }
        }
    }
}