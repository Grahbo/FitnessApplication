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
    var date: Date,
    var text: String,
    var location: String,
    var workoutsolo: Boolean,
    var workoutcompany: Boolean
) : Parcelable {
    constructor() : this(NEW_WORKOUT_ID, Date(), "", "", false, false)
    constructor(date: Date, text: String, location: String, workoutsolo: Boolean, workoutcompany: Boolean)
                : this(NEW_WORKOUT_ID, date, text, location, workoutsolo, workoutcompany)
}
