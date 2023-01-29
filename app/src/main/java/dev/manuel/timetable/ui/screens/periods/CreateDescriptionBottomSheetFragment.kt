package dev.manuel.timetable.ui.screens.periods

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.manuel.timetable.R
import dev.manuel.timetable.databinding.FragmentCreateDescriptionBottomSheetBinding

class CreateDescriptionBottomSheetFragment(
    private val onAddDescription: (String) -> Unit,
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCreateDescriptionBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_description_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateDescriptionBottomSheetBinding.bind(view)

        binding.cancelButton.setOnClickListener { dismiss() }
        binding.saveButton.setOnClickListener {
            val description = binding.periodDescriptionTextField.editText?.text.toString()
            if(description.isBlank()) {
                binding.periodDescriptionTextField.error = resources.getString(R.string.fill_in_fields)
                return@setOnClickListener
            }
            onAddDescription(description)
            dismiss()
        }
    }

}