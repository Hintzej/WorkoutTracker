package com.jordanhintze.workouttracker.ui.workout

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.jordanhintze.workouttracker.R
import com.jordanhintze.workouttracker.model.Workout
import com.jordanhintze.workouttracker.databinding.FragmentWorkoutBinding
import java.util.*

private const val ARG_WORKOUT_ID = "workout_id"
private const val TAG = "workout_fragment"

class WorkoutFragment : Fragment() {

    interface Callbacks {
        fun exerciseListClicked(workoutId: UUID)
    }

    private lateinit var titleField: EditText
    private var callbacks: Callbacks? = null
    private lateinit var binding: FragmentWorkoutBinding

    private val workoutViewModel: WorkoutViewModel by lazy {
        ViewModelProvider(this).get(WorkoutViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_workout, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModelObservers()
        setupBindings()
    }

    private fun setupViewModelObservers() {
        workoutViewModel.workoutLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { workout ->
                workout?.let {
                    binding.workout = workout
                    updateUI()
                }
            }
        )

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            for (event in workoutViewModel.channel) {
                when (event) {
                    is WorkoutViewModel.Event.NavigateToExerciseList -> navigateToExerciseList()
                }
            }
        }

    }

    private fun setupBindings() {
        titleField = binding.workoutName

        binding.workout = Workout()
        binding.workoutViewModel = workoutViewModel
        val workoutId: UUID = arguments?.getSerializable(ARG_WORKOUT_ID) as UUID
        workoutViewModel.loadWorkout(workoutId)



        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun navigateToExerciseList() {
        Log.d(TAG, "Navigate button clicked")
        binding.workout?.let { workout ->
            callbacks?.exerciseListClicked(workout.id)
        }
    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // don't need this
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // don't need this
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.workout?.let { workout ->
                    workout.title = s.toString()
                }
            }
        }
        titleField.addTextChangedListener(titleWatcher)
    }

    override fun onStop() {
        super.onStop()
        binding.workout?.let { workout ->
            workoutViewModel.saveWorkout(workout)
        }
    }

    private fun updateUI() {
        binding.workout?.let { workout ->
            titleField.setText(workout.title)
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object {

        fun newInstance(workoutId: UUID): WorkoutFragment {
            val args = Bundle().apply {
                putSerializable(ARG_WORKOUT_ID, workoutId)
            }
            return WorkoutFragment().apply {
                arguments = args
            }
        }
    }
}