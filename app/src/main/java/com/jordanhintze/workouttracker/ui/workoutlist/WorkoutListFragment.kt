package com.jordanhintze.workouttracker.ui.workoutlist

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jordanhintze.workouttracker.R
import com.jordanhintze.workouttracker.model.Workout
import com.jordanhintze.workouttracker.databinding.FragmentWorkoutListBinding
import java.util.*

private const val TAG = "WorkoutListFragment"

class WorkoutListFragment : Fragment() {

    interface Callbacks {
        fun onWorkoutSelected(workoutId: UUID)
    }

    private var callbacks: Callbacks? = null
    private lateinit var binding: FragmentWorkoutListBinding

    private val workoutListViewModel : WorkoutListViewModel by lazy {
        ViewModelProvider(this).get(WorkoutListViewModel::class.java)
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_workout_list, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModelObservers()
        setUpBindings()
    }

    private fun setupViewModelObservers() {
        workoutListViewModel.workoutsLiveData.observe(
            viewLifecycleOwner,
            Observer { workouts ->
                workouts?.let {
                    Log.i(TAG, "Got ${workouts.size} workouts")
                    binding.workoutRecyclerView.apply{
                        layoutManager = LinearLayoutManager(context)
                        adapter =
                            WorkoutAdapter(
                                workouts,
                                workoutListViewModel,
                                layoutInflater
                            )
                    }
                }
            }
        )

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            for (event in workoutListViewModel.channel) {
                when (event) {
                    is WorkoutListViewModel.Event.NavigateToWorkout -> navigateToWorkout(event.workout)
                }
            }
        }
    }

    private fun setUpBindings() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun navigateToWorkout(workout: Workout) {
        callbacks?.onWorkoutSelected(workout.id)
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_workout_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_workout -> {
                    val workout = Workout()
                    workoutListViewModel.addWorkout(workout)
                    callbacks?.onWorkoutSelected(workout.id)
                    true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun newInstance(): WorkoutListFragment {
            return WorkoutListFragment()
        }
    }
}