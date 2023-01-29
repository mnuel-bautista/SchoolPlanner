package dev.manuel.timetable

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import dev.manuel.timetable.databinding.FragmentClassDetailsBinding
import dev.manuel.timetable.ui.screens.classes.ClassDetailsScreen
import dev.manuel.timetable.ui.screens.classes.ClassDetailsVM
import dev.manuel.timetable.ui.screens.periods.NewGradeBottomSheetFragment
import dev.manuel.timetable.ui.screens.periods.NewPeriodBottomSheetFragment
import dev.manuel.timetable.ui.screens.periods.NewPeriodFragmentDirections
import dev.manuel.timetable.ui.theme.TimetableTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClassDetailsFragment : Fragment() {

    private val viewModel: ClassDetailsVM by viewModels()

    private val args: ClassDetailsFragmentArgs by navArgs()

    private lateinit var binding: FragmentClassDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClassDetailsBinding.bind(view)
        val composeView = view.findViewById<ComposeView>(R.id.fragmentClassDetails)
        val navController = findNavController()

        composeView.setContent {
            Mdc3Theme {
                ClassDetailsScreen(
                    viewModel = viewModel,
                    onNavigateToEditClass = {
                        navController.navigate(R.id.action_classDetailsFragment_to_newClassFragment)
                    },
                    onCreatePeriod = {

                        val bottomSheet = NewPeriodBottomSheetFragment(
                            onAddPeriod = { period ->
                                lifecycleScope.launch {
                                    val periodId = viewModel.createPeriod(period)
                                    findNavController().navigate(
                                        R.id.action_classDetailsFragment_to_newPeriodFragment,
                                        bundleOf("classId" to args.classId, "periodId" to periodId)
                                    )
                                }
                            }
                        )
                        bottomSheet.show(parentFragmentManager, "NewGradeBottomSheetFragment")
                    },
                    onNavigateToPeriod = {
                        findNavController().navigate(
                            R.id.action_classDetailsFragment_to_newPeriodFragment,
                            bundleOf(
                                "periodId" to it.id.toLong(),
                                "classId" to it.classId
                            )
                        )
                    },
                    onNavigateUp = { navController.navigateUp() },
                )
            }
        }


        setUpWithNavController(binding.toolbar)
        setNavigateUp(binding.toolbar)
        lifecycleScope.launch {
            viewModel.state.collect {
                binding.toolbar.title = it.classWithOccurrences.mClass.subject
                binding.toolbar.subtitle = "Hello, this is a subtitle"
            }
        }
    }

}