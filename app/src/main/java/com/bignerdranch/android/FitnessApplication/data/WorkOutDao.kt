package com.bignerdranch.android.FitnessApplication.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WorkOutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(workOut: WorkOutEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(workOuts: List<WorkOutEntity>)

    @Query("SELECT * FROM WorkOutEntity ORDER BY date ASC")
    fun getAll(): LiveData<List<WorkOutEntity>>

    @Query("SELECT * FROM WorkOutEntity WHERE id = :id")
    fun getWorkOutById(id: Int): WorkOutEntity?

    @Query("SELECT COUNT(*) from WorkOutEntity")
    fun getCount(): Int

    @Delete
    fun deleteWorkOut(selectedWorkOuts: List<WorkOutEntity>): Int

    @Query("DELETE from WorkOutEntity")
    fun deleteAllWorkOuts(): Int

    @Delete
    fun deleteWorkOut(workOut: WorkOutEntity)
}