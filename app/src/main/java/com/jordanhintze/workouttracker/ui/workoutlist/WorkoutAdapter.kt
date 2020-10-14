package com.jordanhintze.workouttracker.ui.workoutlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jordanhintze.workouttracker.R
import com.jordanhintze.workouttracker.model.Workout
import com.jordanhintze.workouttracker.databinding.ListItemWorkoutBinding

class WorkoutAdapter(var workouts: List<Workout>,
                     workoutListViewModel: WorkoutListViewModel,
                     layoutInflater: LayoutInflater)
    : RecyclerView.Adapter<WorkoutAdapter.WorkoutHolder>() {

    private val viewModel = workoutListViewModel
    private val inflater = layoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutHolder {
        val binding = DataBindingUtil.inflate<ListItemWorkoutBinding>(
            inflater,
            R.layout.list_item_workout,
            parent,
            false
        )
        return WorkoutHolder(
            binding,
            viewModel
        )
    }

    override fun getItemCount() = workouts.size

    override fun onBindViewHolder(holder: WorkoutHolder, position: Int) {
        val workout = workouts[position]
        holder.bind(workout)
    }


    class WorkoutHolder(private val binding: ListItemWorkoutBinding,
                        workoutListViewModel: WorkoutListViewModel
    )
        : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.workoutListViewModel = workoutListViewModel
        }

        fun bind(workout: Workout) {
            binding.workout = workout
        }
    }
}