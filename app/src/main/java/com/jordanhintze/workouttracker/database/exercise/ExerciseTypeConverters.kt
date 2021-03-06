package com.jordanhintze.workouttracker.database.exercise

import androidx.room.TypeConverter
import java.util.*

class ExerciseTypeConverters {

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }
}