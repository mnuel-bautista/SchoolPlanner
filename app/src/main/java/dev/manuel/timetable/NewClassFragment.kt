package dev.manuel.timetable

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.manuel.timetable.ui.screens.classes.AddOccurrenceBottomSheet
import dev.manuel.timetable.ui.screens.classes.EditClassScreen
import dev.manuel.timetable.ui.screens.classes.EditClassVM
import dev.manuel.timetable.ui.theme.TimetableTheme

@AndroidEntryPoint
class NewClassFragment : Fragment() {

    private val viewModel: EditClassVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_class, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val composeView = view.findViewById<ComposeView>(R.id.fragmentNewClass)
        val navController = findNavController()

        composeView.setContent {
            TimetableTheme {
                EditClassScreen(
                    viewModel = viewModel,
                    onNavigateUp = { navController.navigateUp() },
                    onAddOccurrence = {
                        val bottomSheet = AddOccurrenceBottomSheet()
                        bottomSheet.onAddOccurrence =
                            { occurrence -> viewModel.addOccurrence(occurrence) }
                        bottomSheet.show(parentFragmentManager, "ADD OCCURRENCE BOTTOM SHEET")
                    },
                )
            }
        }
    }

}