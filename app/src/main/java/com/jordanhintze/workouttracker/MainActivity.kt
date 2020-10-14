package com.jordanhintze.workouttracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jordanhintze.workouttracker.ui.exercise.ExerciseFragment
import com.jordanhintze.workouttracker.ui.exerciselist.ExerciseListFragment
import com.jordanhintze.workouttracker.ui.workout.WorkoutFragment
import com.jordanhintze.workouttracker.ui.workoutlist.WorkoutListFragment
import java.util.*

class MainActivity : AppCompatActivity(), WorkoutListFragment.Callbacks, WorkoutFragment.Callbacks,
                    ExerciseListFragment.Callbacks{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = WorkoutListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onWorkoutSelected(workoutId: UUID) {
        val fragment = WorkoutFragment.newInstance(workoutId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun exerciseListClicked(workoutId: UUID) {
        val fragment = ExerciseListFragment.newInstance(workoutId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onExerciseSelected(exerciseId: UUID) {
        val fragment = ExerciseFragment.newInstance(exerciseId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
