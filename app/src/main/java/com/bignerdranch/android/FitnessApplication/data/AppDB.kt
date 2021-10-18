package com.bignerdranch.android.FitnessApplication.data

import android.content.Context
import androidx.room.*

@Database(entities = [WorkOutEntity::class], version = 1, exportSchema = false )
@TypeConverters(DateConvertor::class)
abstract class AppDB: RoomDatabase() {
    
    abstract fun  workOutDao(): WorkOutDao?

    companion object{
        private var INSTANCE: AppDB? = null

        fun getInstance(context: Context): AppDB? {
            if(INSTANCE == null){
                synchronized(AppDB::class){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDB::class.java,
                        "fitnessapplication.db"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}