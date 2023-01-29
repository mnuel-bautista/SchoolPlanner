package dev.manuel.timetable.ui.screens.periods

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.manuel.timetable.R
import dev.manuel.timetable.databinding.FragmentNewPeriodBottomSheetBinding

class NewPeriodBottomSheetFragment(
    private val onAddPeriod: (String) -> Unit,
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNewPeriodBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_period_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewPeriodBottomSheetBinding.bind(view)
        binding.cancelPeriodButton.setOnClickListener { dismiss() }
        binding.savePeriodButton.setOnClickListener {
            val period = binding.periodTextfield.editText?.text.toString()
            if(period.isBlank()) {
                binding.periodTextfield.error = resources.getString(R.string.fill_in_fields)
                return@setOnClickListener
            }
            onAddPeriod(binding.periodTextfield.editText?.text.toString())
            dismiss()
        }
    }

}