package com.bignerdranch.android.FitnessApplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bignerdranch.android.plainolnotes.R

//my Main Activity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}