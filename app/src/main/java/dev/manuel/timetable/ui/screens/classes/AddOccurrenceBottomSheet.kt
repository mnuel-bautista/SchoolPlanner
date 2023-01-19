package dev.manuel.timetable.ui.screens.classes

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import dev.manuel.timetable.databinding.AddOccurrenceBottomSheetBinding
import dev.manuel.timetable.format
import dev.manuel.timetable.room.entities.Occurrence
import java.time.DayOfWeek
import java.time.LocalTime

class AddOccurrenceBottomSheet : BottomSheetDialogFragment() {

    private var _binding: AddOccurrenceBottomSheetBinding? = null

    private val binding get() = _binding!!

    private var occurrence: Occurrence = Occurrence()

    var onAddOccurrence: ((Occurrence) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddOccurrenceBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        with(binding) {
            txtDayOfWeek.text = occurrence.dayOfWeek.format()
            txtStarts.text = occurrence.starts.format()
            txtEnds.text = occurrence.ends.format()
            btnCancel.setOnClickListener { dismiss() }
            btnAdd.setOnClickListener {
                onAddOccurrence?.invoke(occurrence)
                dismiss()
            }
        }

        binding.btnDayOfWeek.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Day of week")
                .setSingleChoiceItems(
                    arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"),
                    occurrence.dayOfWeek.ordinal,
                ) { dialog, dayOfWeek ->
                    occurrence = occurrence.copy(dayOfWeek = DayOfWeek.of(dayOfWeek + 1))
                    binding.txtDayOfWeek.text = occurrence.dayOfWeek.format()
                    dialog.dismiss()
                }.show()
        }

        binding.btnStarts.setOnClickListener {
            val picker = MaterialTimePicker.Builder()
                .setHour(occurrence.starts.hour)
                .setMinute(occurrence.starts.minute)
                .build()
            picker.addOnPositiveButtonClickListener {
                occurrence = occurrence.copy(starts = LocalTime.of(picker.hour, picker.minute))
                binding.txtStarts.text = occurrence.starts.format()
            }
            picker.show(parentFragmentManager, "Material Picker - Start time")
        }

        binding.btnEnds.setOnClickListener {
            val picker = MaterialTimePicker.Builder()
                .setHour(occurrence.ends.hour)
                .setMinute(occurrence.ends.minute)
                .build()
            picker.addOnPositiveButtonClickListener {
                occurrence = occurrence.copy(ends = LocalTime.of(picker.hour, picker.minute))
                binding.txtEnds.text = occurrence.ends.format()
            }
            picker.show(parentFragmentManager, "Material Picker - End time")
        }
    }


}