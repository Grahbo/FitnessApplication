package com.bignerdranch.android.FitnessApplication.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bignerdranch.android.FitnessApplication.NEW_WORKOUT_ID
import kotlinx.android.parcel.Parcelize
import java.util.*
import java.time.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Parcelize
//@Entity(tableName = "workouts")
@Entity
data class WorkOutEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var date: String,
    var workout: String,
    var location: String,
    var workoutsolo: Int,
    var starttime: String,
    var endtime: String
) : Parcelable {
    constructor() : this(NEW_WORKOUT_ID, "", "", "", 0, "", "")

    constructor(
        date: String,
        text: String,
        location: String,
        workoutsolo: Int,
        starttime: String,
        endtime: String
    )
            : this(NEW_WORKOUT_ID, date, text, location, workoutsolo, starttime, endtime)
}


