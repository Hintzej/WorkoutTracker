package com.jordanhintze.workouttracker.ui.exercise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.jordanhintze.workouttracker.Repositories
import com.jordanhintze.workouttracker.model.Exercise
import java.util.*

class ExerciseViewModel : ViewModel() {

    private val exerciseRepository = Repositories.get()
    private val exerciseIdLiveData = MutableLiveData<UUID>()

    var exerciseLiveData: LiveData<Exercise?> =
        Transformations.switchMap(exerciseIdLiveData) { exerciseId ->
            exerciseRepository.getExercise(exerciseId)
        }

    fun loadExercise(exerciseId: UUID) {
        exerciseIdLiveData.value = exerciseId
    }

    fun saveExercise(exercise: Exercise) {
        exerciseRepository.updateExercise(exercise)
    }


}
