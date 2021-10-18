package com.bignerdranch.android.plainolnotes.data

import androidx.room.TypeConverter
import java.util.*

class DateConvertor {
    @TypeConverter
    fun fromTimeStamp(value: Long): Date{
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }
}