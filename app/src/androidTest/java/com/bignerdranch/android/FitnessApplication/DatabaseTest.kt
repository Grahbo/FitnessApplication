package com.bignerdranch.android.FitnessApplication

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bignerdranch.android.FitnessApplication.data.AppDB
import com.bignerdranch.android.FitnessApplication.data.WorkOutDao
import com.bignerdranch.android.FitnessApplication.data.WorkOutEntity
import com.bignerdranch.android.FitnessApplication.data.SampleDataProvider
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var dao: WorkOutDao
    private lateinit var database: AppDB

    @Before
    fun createDb(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(appContext, AppDB::class.java)
            .allowMainThreadQueries()
            .build()

        dao = database.workOutDao()!!
    }

    @Test
    fun createworkouts() {
        dao.insertAll(SampleDataProvider.getworkouts())
        val count = dao.getCount()
        assertEquals(count, SampleDataProvider.getworkouts().size)
    }

    @Test
    fun insertNote(){
        val note = WorkOutEntity()
        note.text = "some text"
        dao.insertNote(note)
        val savedNote = dao.getWorkOutById(1)

        assertEquals(savedNote?.id ?: 0, 1)
    }

    @After
    fun closeDb(){
        database.close()
    }
}