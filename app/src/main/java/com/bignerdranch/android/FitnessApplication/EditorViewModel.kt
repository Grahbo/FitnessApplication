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

    fun getWorkOutById(workOutId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val note =
                    if (workOutId != NEW_WORKOUT_ID) {
                        database?.workOutDao()?.getWorkOutById(workOutId)
                    } else {
                        WorkOutEntity()
                    }
                currentWorkOut.postValue(note!!)
            }
        }
    }

    fun updateWorkout() {
        currentWorkOut.value?.let {
            it.workout = it.workout.trim()
            //it.location = it.location.trim()
            if (it.id == NEW_WORKOUT_ID && it.workout.isEmpty()) {
                return
            }

            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    if (it.workout.isEmpty()) {
                        database?.workOutDao()?.deleteWorkOut(it)
                    } else {
                        database?.workOutDao()?.insertWorkout(it)
                    }
                }
            }
        }
    }
}