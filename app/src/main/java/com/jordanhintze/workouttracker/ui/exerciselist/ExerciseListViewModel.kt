package com.jordanhintze.workouttracker.ui.exerciselist

import androidx.lifecycle.*
import com.jordanhintze.workouttracker.model.Exercise
import com.jordanhintze.workouttracker.Repositories
import com.jordanhintze.workouttracker.model.Workout
import com.jordanhintze.workouttracker.ui.workoutlist.WorkoutListViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import java.util.*

class ExerciseListViewModel() : ViewModel() {

    private val workoutRepository =
        Repositories.get()
    private val workoutIdLiveData = MutableLiveData<UUID>()

    var workoutLiveData: LiveData<Workout?> =
        Transformations.switchMap(workoutIdLiveData) { workoutId ->
            workoutRepository.getWorkout(workoutId)
        }

    private val exerciseRepository = Repositories.get()
    lateinit var exercisesLiveData : LiveData<List<Exercise>>

    private val sendChannel = Channel<Event>()
    val channel : ReceiveChannel<Event> = sendChannel

    private fun sendEvent(event: Event) = viewModelScope.launch {
        sendChannel.offer(event)
    }

    fun loadWorkout(workoutId:UUID) {
        workoutIdLiveData.value = workoutId
        exercisesLiveData = exerciseRepository.getExercises(workoutId)
    }

    fun addExercise(exercise: Exercise) {
        exerciseRepository.addExercise(exercise)
    }

    fun onExerciseClick(exercise: Exercise) {
        sendEvent(
            Event.NavigateToExercise(exercise)
        )
    }



    sealed class Event {
        data class NavigateToExercise(val exercise: Exercise) : Event()
    }
}
