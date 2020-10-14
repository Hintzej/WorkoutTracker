package com.jordanhintze.workouttracker.ui.exercise

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import com.jordanhintze.workouttracker.R
import com.jordanhintze.workouttracker.databinding.ExerciseFragmentBinding
import com.jordanhintze.workouttracker.model.Exercise
import java.util.*

private const val ARG_EXERCISE_ID = "exercise_id"

class ExerciseFragment : Fragment() {

    private lateinit var titleField: EditText
    private lateinit var repField: EditText
    private lateinit var binding: ExerciseFragmentBinding

    private val exerciseViewModel: ExerciseViewModel by lazy {
        ViewModelProvider(this).get(ExerciseViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.exercise_fragment, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModelObservers()
        setupBindings()
    }

    private fun setupViewModelObservers() {
        exerciseViewModel.exerciseLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { exercise ->
                exercise?.let {
                    binding.exercise = exercise
                    updateUI()
                }
            }
        )
    }

    private fun setupBindings() {
        titleField = binding.exerciseName
        repField = binding.repNumber

        val exerciseId: UUID = arguments?.getSerializable(ARG_EXERCISE_ID) as UUID
        binding.exercise = Exercise(exerciseId)
        binding.exerciseViewModel = exerciseViewModel
        exerciseViewModel.loadExercise(exerciseId)

        binding.lifecycleOwner = viewLifecycleOwner
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
                binding.exercise?.let { exercise ->
                    exercise.name = s.toString()
                }
            }
        }
        titleField.addTextChangedListener(titleWatcher)


        val repWatcher = object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                // don't need this
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // don't need this
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.exercise?.let { exercise ->
                    exercise.reps = s.toString()
                }
            }
        }
        repField.addTextChangedListener(repWatcher)
    }

    override fun onStop() {
        super.onStop()
        binding.exercise?.let { exercise ->
            exerciseViewModel.saveExercise(exercise)
        }
    }

    private fun updateUI() {
        binding.exercise?.let { exercise ->
            titleField.setText(exercise.name)
        }
    }

    companion object {
        fun newInstance(exerciseId: UUID): ExerciseFragment {
            val args = Bundle().apply {
                putSerializable(ARG_EXERCISE_ID, exerciseId)
            }
            return ExerciseFragment().apply {
                arguments = args
            }
        }
    }
}
