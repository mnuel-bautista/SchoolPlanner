package dev.manuel.timetable.ui.screens.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.manuel.timetable.R
import dev.manuel.timetable.room.entities.Timetable
import dev.manuel.timetable.ui.components.NewTimetableDialog

class EditTimetableFragmentBottomSheet(
    private val timetable: Timetable = Timetable(0, ""),
    private val onSave: (Timetable) -> Unit = {}
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_timetable_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val composeView = view.findViewById<ComposeView>(R.id.fragmentBottomSheetEditTimetable)
        composeView.setContent {
            Mdc3Theme {
                NewTimetableDialog(
                    timetable = timetable.name,
                    onAccept = { dismiss(); onSave(timetable.copy(name = it)) },
                    onCancel = { dismiss() },
                    onDismiss = { dismiss() })
            }
        }
    }
}