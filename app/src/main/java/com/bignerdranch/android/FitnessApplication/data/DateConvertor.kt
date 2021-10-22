package com.bignerdranch.android.FitnessApplication.data

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class DateConvertor {
    @TypeConverter
    fun fromTimeStamp(value: Long): Date{
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {

//        val df = SimpleDateFormat("yyyy.MM.dd HH:mm")
//        return df.parse(date).time

        return date.time
    }
}