package dev.manuel.timetable

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import dagger.hilt.android.AndroidEntryPoint
import dev.manuel.timetable.ui.screens.classes.ClassDetailsScreen
import dev.manuel.timetable.ui.screens.classes.ClassDetailsVM
import dev.manuel.timetable.ui.theme.TimetableTheme

@AndroidEntryPoint
class ClassDetailsFragment : Fragment() {

    private val viewModel: ClassDetailsVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val composeView = view.findViewById<ComposeView>(R.id.fragmentClassDetails)
        val navController = findNavController()
        composeView.setContent {
           Mdc3Theme {
               ClassDetailsScreen(
                   viewModel = viewModel,
                   onNavigateToEditClass = {
                       navController.navigate(R.id.action_classDetailsFragment_to_newClassFragment)
                   },
                   onNavigateUp = { navController.navigateUp() }
               )
           }
        }
    }

}