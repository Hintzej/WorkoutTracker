package com.jordanhintze.workouttracker.ui.workout

import android.util.Log
import androidx.lifecycle.*
import com.jordanhintze.workouttracker.model.Workout
import com.jordanhintze.workouttracker.Repositories
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "workoutviewmodel"

class WorkoutViewModel() : ViewModel() {

    private val workoutRepository = Repositories.get()
    private val workoutIdLiveData = MutableLiveData<UUID>()

    var workoutLiveData: LiveData<Workout?> =
        Transformations.switchMap(workoutIdLiveData) { workoutId ->
            workoutRepository.getWorkout(workoutId)
        }

    private val sendChannel = Channel<Event>()
    val channel : ReceiveChannel<Event> = sendChannel

    private fun sendEvent(event: Event) = viewModelScope.launch {
        sendChannel.offer(event)
    }

    fun loadWorkout(workoutId: UUID) {
        workoutIdLiveData.value = workoutId
    }

    fun saveWorkout(workout: Workout) {
        workoutRepository.updateWorkout(workout)
    }

    fun onExercisesButtonClicked() {
        Log.d(TAG, "Button Click started")
        sendEvent(Event.NavigateToExerciseList())
    }

    override fun onCleared() {
        super.onCleared()
        sendChannel.close()
    }

    sealed class Event {
        class NavigateToExerciseList() : Event()
    }
}