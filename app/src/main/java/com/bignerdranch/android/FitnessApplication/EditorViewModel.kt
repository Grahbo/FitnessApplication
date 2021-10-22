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
    val currentWorkOut = MutableLiveData<WorkOutEntity>()

    fun getWorkOutById(workOutId: Int){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val note =
                    if (workOutId != NEW_WORKOUT_ID){
                        database?.workOutDao()?.getWorkOutById(workOutId)
                    }else{
                        WorkOutEntity()
                    }
                currentWorkOut.postValue(note!!)
            }
        }
    }

    fun updateNote() {
        currentWorkOut.value?.let{
            it.text = it.text.trim()
            //it.location = it.location.trim()
            if(it.id == NEW_WORKOUT_ID && it.text.isEmpty()){
                return
            }

            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    if(it.text.isEmpty()){
                        database?.workOutDao()?.deleteWorkOut(it)
                    }else{
                        database?.workOutDao()?.insertWorkout(it)
                    }
                }
            }
        }
    }
}