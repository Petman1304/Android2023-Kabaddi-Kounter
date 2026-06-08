package com.example.kabaddikounter

import androidx.room.TypeConverter
import com.example.kabaddikounter.Status

class Converters {
    @TypeConverter
    fun fromStatus(status: Status): String{
        return status.name
    }

    @TypeConverter
    fun toStatus(value: String): Status {
        return Status.valueOf(value)
    }
}