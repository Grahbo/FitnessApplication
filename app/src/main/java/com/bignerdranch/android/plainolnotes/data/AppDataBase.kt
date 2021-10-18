package com.bignerdranch.android.plainolnotes.data

import android.content.Context
import androidx.room.*
import java.security.AccessControlContext

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false )
@TypeConverters(DateConvertor::class)
abstract class AppDataBase: RoomDatabase() {
    
    abstract fun  noteDao():NoteDao?

    companion object{
        private var INSTANCE: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase? {
            if(INSTANCE == null){
                synchronized(AppDataBase::class){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java,
                        "plainolnotes.db"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}