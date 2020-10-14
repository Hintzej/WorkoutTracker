package com.jordanhintze.workouttracker.database.exercise

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jordanhintze.workouttracker.model.Exercise

@Database(entities = [ Exercise::class ], version = 1)
@TypeConverters(ExerciseTypeConverters::class)
abstract class ExerciseDatabase : RoomDatabase() {

    abstract fun exerciseDao(): ExerciseDao
}