package dev.manuel.timetable

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import dagger.hilt.android.AndroidEntryPoint
import dev.manuel.timetable.databinding.FragmentTimetablesBinding
import dev.manuel.timetable.ui.screens.timetable.ContextMenuFragment
import dev.manuel.timetable.ui.screens.timetable.EditTimetableFragmentBottomSheet
import dev.manuel.timetable.ui.screens.timetable.TimetablesScreen
import dev.manuel.timetable.ui.screens.timetable.TimetablesVM

@AndroidEntryPoint
class TimetablesFragment : Fragment() {

    private val viewModel: TimetablesVM by viewModels()

    private lateinit var binding: FragmentTimetablesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timetables, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val composeView = view.findViewById<ComposeView>(R.id.fragmentTimetables)
        binding = FragmentTimetablesBinding.bind(view)
        composeView.setContent {
            Mdc3Theme {
                TimetablesScreen(
                    viewModel = viewModel,
                    onShowContextMenu = {
                        val contextMenu = ContextMenuFragment(
                            it,
                            onEdit = {
                                val editDialog = EditTimetableFragmentBottomSheet(it,
                                    onSave = { new -> viewModel.saveChanges(new) }
                                )
                                editDialog.show(
                                    parentFragmentManager,
                                    "Edit Dialog - TimetablesFragment"
                                )
                            },
                            onSelect = { tm -> viewModel.selectTimetable(tm) },
                            onDelete = { tm -> viewModel.deleteTimetable(tm) }
                        )
                        contextMenu.show(parentFragmentManager, "Context Menu - TimetablesFragment")
                    },
                    onShowDialogForTimetable = {
                        val editDialog = EditTimetableFragmentBottomSheet(onSave = { viewModel.saveChanges(it)})
                        editDialog.show(parentFragmentManager, "Edit Dialog - TimetablesFragment")
                    }
                )
            }
            setUpWithNavController()
        }
    }

    private fun setUpWithNavController() {
        val navController = findNavController()
        val toolbar = binding.toolbar

        val activity = activity as MainActivity

        val appbarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(R.id.homeFragment)
        )
        toolbar.setupWithNavController(navController, appbarConfiguration)

        activity.setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

}