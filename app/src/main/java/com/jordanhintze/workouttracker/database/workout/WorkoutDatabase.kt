package com.jordanhintze.workouttracker.database.workout

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jordanhintze.workouttracker.model.Workout

@Database(entities = [Workout::class], version = 1)
@TypeConverters(WorkoutTypeConverters::class)
abstract class WorkoutDatabase : RoomDatabase() {

    abstract fun workoutDao(): WorkoutDao
}