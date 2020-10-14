package com.jordanhintze.workouttracker.ui.workoutlist


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jordanhintze.workouttracker.model.Workout
import com.jordanhintze.workouttracker.Repositories
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

private const val TAG = "workoutlistviewmodel"

class WorkoutListViewModel : ViewModel() {

    private val workoutRepository =
        Repositories.get()
    val workoutsLiveData = workoutRepository.getWorkouts()

    private val sendChannel = Channel<Event>()
    val channel : ReceiveChannel<Event> = sendChannel

    private fun sendEvent(event: Event) = viewModelScope.launch {
        sendChannel.offer(event)
    }

    fun addWorkout(workout: Workout) {
        workoutRepository.addWorkout(workout)
    }

    fun onWorkoutClick(workout: Workout) {
        sendEvent(
            Event.NavigateToWorkout(
                workout
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        sendChannel.close()
    }

    sealed class Event {
        data class NavigateToWorkout(val workout: Workout) : Event()
    }
}