package com.bignerdranch.android.FitnessApplication.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WorkOutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(workOut: WorkOutEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(workOuts: List<WorkOutEntity>)

    @Query("SELECT * FROM notes ORDER BY date ASC")
    fun getAll(): LiveData<List<WorkOutEntity>>

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getWorkOutById(id: Int): WorkOutEntity?

    @Query("SELECT COUNT(*) from notes")
    fun getCount(): Int

    @Delete
    fun deleteWorkOut(selectedWorkOuts: List<WorkOutEntity>): Int

    @Query("DELETE from notes")
    fun deleteAllWorkOuts(): Int

    @Delete
    fun deleteWorkOut(it: WorkOutEntity)
}