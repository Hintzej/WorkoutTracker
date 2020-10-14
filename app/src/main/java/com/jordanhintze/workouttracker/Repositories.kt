package com.jordanhintze.workouttracker

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.jordanhintze.workouttracker.database.exercise.ExerciseDatabase
import com.jordanhintze.workouttracker.database.workout.WorkoutDatabase
import com.jordanhintze.workouttracker.model.Exercise
import com.jordanhintze.workouttracker.model.Workout
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private const val TAG = "Repositories"

private const val WORKOUT_DATABASE_NAME = "workout-database"
private const val EXERCISE_DATABASE_NAME = "exercise-database"

class Repositories private constructor(context: Context) {

    private val workoutDatabase : WorkoutDatabase = Room.databaseBuilder(
        context.applicationContext,
        WorkoutDatabase::class.java,
        WORKOUT_DATABASE_NAME
    ).build()

    private val exerciseDatabase : ExerciseDatabase = Room.databaseBuilder(
        context.applicationContext,
        ExerciseDatabase::class.java,
        EXERCISE_DATABASE_NAME
    ).build()

    private val workoutDao = workoutDatabase.workoutDao()
    private val workoutExecutor = Executors.newSingleThreadExecutor()

    private val exerciseDao = exerciseDatabase.exerciseDao()
    private val exerciseExecutor = Executors.newSingleThreadExecutor()

    fun getWorkouts(): LiveData<List<Workout>> = workoutDao.getWorkouts()

    fun getWorkout(id: UUID): LiveData<Workout?> = workoutDao.getWorkout(id)

    fun updateWorkout(workout: Workout) {
        workoutExecutor.execute {
            workoutDao.updateWorkout(workout)
        }
    }

    fun addWorkout(workout: Workout) {
        workoutExecutor.execute {
            workoutDao.addWorkout(workout)
        }
    }

    fun getExercises(workoutId: UUID): LiveData<List<Exercise>> = exerciseDao.getExercises(workoutId)

    fun getExercise(id: UUID): LiveData<Exercise?> = exerciseDao.getExercise(id)

    fun updateExercise(exercise: Exercise) {
        exerciseExecutor.execute {
            exerciseDao.updateExercise(exercise)
        }
    }

    fun addExercise(exercise: Exercise) {
        exerciseExecutor.execute {
            exerciseDao.addExercise(exercise)
        }
    }

    companion object {
        private var INSTANCE: Repositories? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = Repositories(context)
            }
        }

        fun get(): Repositories {
            return INSTANCE ?:
            throw IllegalStateException("Repositories must be initialized")
        }
    }
}