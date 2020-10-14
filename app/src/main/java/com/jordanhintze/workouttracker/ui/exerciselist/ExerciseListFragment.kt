package com.jordanhintze.workouttracker.ui.exerciselist

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager

import com.jordanhintze.workouttracker.R
import com.jordanhintze.workouttracker.databinding.ExerciseListFragmentBinding
import com.jordanhintze.workouttracker.model.Exercise
import java.util.*

private const val ARG_WORKOUT_ID = "workout_id"
private const val TAG = "ExerciseListFragment"

class ExerciseListFragment : Fragment() {

    interface Callbacks {
        fun onExerciseSelected(exerciseId: UUID)
    }

    private var callbacks: Callbacks? = null
    private lateinit var binding: ExerciseListFragmentBinding
    lateinit var workoutId: UUID

    private val exerciseListViewModel : ExerciseListViewModel by lazy {
        ViewModelProvider(this).get(ExerciseListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.exercise_list_fragment, container, false)

        workoutId = arguments?.getSerializable(ARG_WORKOUT_ID) as UUID
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exerciseListViewModel.loadWorkout(workoutId)
        setupViewModelObservers()
        setUpBindings()
    }

    private fun setupViewModelObservers() {
        exerciseListViewModel.exercisesLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { exercises ->
                Log.i(TAG, "Got ${exercises.size} exercises")
                binding.exerciseRecyclerView.apply{
                    layoutManager = LinearLayoutManager(context)
                    adapter =
                        ExerciseAdapter(
                            exercises,
                            exerciseListViewModel,
                            layoutInflater
                        )
                }
            }
        )

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            for (event in exerciseListViewModel.channel) {
                when (event) {
                    is ExerciseListViewModel.Event.NavigateToExercise -> navigateToExercise(event.exercise)
                }
            }
        }
    }

    private fun setUpBindings() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun navigateToExercise(exercise: Exercise) {
        callbacks?.onExerciseSelected(exercise.id)
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.exercise_list_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_exercise -> {
                val exercise = Exercise(workoutId)
                exerciseListViewModel.addExercise(exercise)
                callbacks?.onExerciseSelected(exercise.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun newInstance(workoutId: UUID): ExerciseListFragment {
            val args = Bundle().apply {
                putSerializable(ARG_WORKOUT_ID, workoutId)
            }
            return ExerciseListFragment().apply {
                arguments = args
            }
        }
    }
}
