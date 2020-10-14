package com.jordanhintze.workouttracker.database.exercise

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jordanhintze.workouttracker.model.Exercise
import java.util.*

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercise WHERE workoutId=(:workoutId)")
    fun getExercises(workoutId: UUID): LiveData<List<Exercise>>

    @Query("SELECT * FROM exercise WHERE id=(:id)")
    fun getExercise(id: UUID): LiveData<Exercise?>

    @Update
    fun updateExercise(exercise: Exercise)

    @Insert
    fun addExercise(exercise: Exercise)
}