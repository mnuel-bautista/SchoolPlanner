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
import dev.manuel.timetable.ui.screens.classes.SelectClassFragment.Companion.SELECTED_CLASS_ID
import dev.manuel.timetable.ui.screens.tasks.TaskListScreen
import dev.manuel.timetable.ui.screens.tasks.TaskListVM

@AndroidEntryPoint
class TasksFragment : Fragment() {

    private val viewModel: TaskListVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val composeView = view.findViewById<ComposeView>(R.id.fragmentTasks)
        val navController = findNavController()
        getNavigationResult(SELECTED_CLASS_ID)?.observe(viewLifecycleOwner) { result ->
            viewModel.editableTask.setClass(result)
        }
        composeView.setContent {
            Mdc3Theme {
                TaskListScreen(
                    viewModel = viewModel,
                    onNavigateToSelectSubject = {
                        navController.navigate(R.id.action_tasksFragment_to_selectClassFragment)
                    },
                    navigateUp = navController::navigateUp
                )
            }
        }
    }

}