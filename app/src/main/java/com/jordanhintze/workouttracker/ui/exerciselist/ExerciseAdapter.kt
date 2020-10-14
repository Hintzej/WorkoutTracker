package com.jordanhintze.workouttracker.ui.exerciselist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jordanhintze.workouttracker.model.Exercise
import com.jordanhintze.workouttracker.R
import com.jordanhintze.workouttracker.databinding.ListItemExerciseBinding

class ExerciseAdapter(var exercises: List<Exercise>,
                      exerciseListViewModel: ExerciseListViewModel,
                      layoutInflater: LayoutInflater)
    : RecyclerView.Adapter<ExerciseAdapter.ExerciseHolder>(){

    private val viewModel = exerciseListViewModel
    private val inflater = layoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseHolder {
        val binding = DataBindingUtil.inflate<ListItemExerciseBinding>(
            inflater,
            R.layout.list_item_exercise,
            parent,
            false
        )
        return ExerciseHolder(
            binding,
            viewModel
        )
    }

    override fun getItemCount() = exercises.size

    override fun onBindViewHolder(holder: ExerciseHolder, position: Int) {
        val exercise = exercises[position]
        holder.bind(exercise)
    }

    class ExerciseHolder(private val binding: ListItemExerciseBinding,
                         exerciseListViewModel: ExerciseListViewModel
    )
        : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.exerciseListViewModel = exerciseListViewModel
        }

        fun bind(exercise: Exercise) {
            binding.exercise = exercise
        }
    }
}