package dev.manuel.timetable.ui.screens.periods

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.manuel.timetable.R
import dev.manuel.timetable.databinding.FragmentNewGradeBottomSheetBinding
import dev.manuel.timetable.room.entities.Grade

class NewGradeBottomSheetFragment(
    private val onAddGrade: (Grade) -> Unit = {},
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNewGradeBottomSheetBinding

    private val grade: Grade = Grade()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_grade_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewGradeBottomSheetBinding.bind(view)


        setUpKeyboard()
        binding.cancelGradeButton.setOnClickListener { dismiss() }
        binding.saveGradeButton.setOnClickListener { addGrade() }
    }


    private fun setUpKeyboard() {
        binding.taskTextfield.editText?.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_NEXT -> {
                    binding.gradeTextfield.editText?.requestFocus()
                    true
                }
                else -> false
            }
        }

        binding.gradeTextfield.editText?.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_NEXT -> {
                    binding.weightTextfield.requestFocus()
                    true
                }
                else -> false
            }
        }

        binding.weightTextfield.editText?.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    addGrade()
                    true
                }
                else -> false
            }
        }
    }

    private fun addGrade() {
        val task = binding.taskTextfield.editText?.text.toString()
        val gr = binding.gradeTextfield.editText?.text.toString().toIntOrNull()
        val weight = binding.weightTextfield.editText?.text.toString().toIntOrNull()

        if (task.isBlank()) {
            binding.taskTextfield.error = "Task must not be empty"
            return
        }

        if (gr == null) {
            binding.taskTextfield.error = "Grade must not be empty"
            return
        }

        if (weight == null) {
            binding.taskTextfield.error = "Weight must not be empty"
            return
        }

        val grade = Grade(0, gr, task, weight)

        onAddGrade(grade)
        dismiss()
    }

}