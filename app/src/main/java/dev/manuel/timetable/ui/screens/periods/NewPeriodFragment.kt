package dev.manuel.timetable.ui.screens.periods

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.manuel.timetable.R
import dev.manuel.timetable.databinding.FragmentNewPeriodBinding
import dev.manuel.timetable.setNavigateUp
import dev.manuel.timetable.setUpWithNavController
import dev.manuel.timetable.ui.UIEvent
import dev.manuel.timetable.ui.adapters.GradeAdapter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewPeriodFragment : Fragment() {

    private lateinit var binding: FragmentNewPeriodBinding

    private val viewModel: NewPeriodVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_period, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewPeriodBinding.bind(view)


        val adapter = GradeAdapter(emptyList())
        binding.gradesRecyclerView.adapter = adapter
        binding.gradesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        lifecycleScope.launch {
            viewModel.state.collect {
                with(binding) {

                    toolbar.title = it.period.period

                    if(it.period.description.isNotBlank()) {
                        periodDescriptionText.text = it.period.description
                        periodDescriptionText.visibility = View.VISIBLE
                        addDescriptionButton.visibility = View.INVISIBLE
                    } else {
                        periodDescriptionText.visibility = View.INVISIBLE
                        addDescriptionButton.visibility = View.VISIBLE
                    }

                    val finalGrade = it.grades.fold(0) { acc, grade ->
                        acc + (grade.grade * (grade.weight.toFloat() / 100)).toInt()
                    }

                    finalGradeBar.progress = finalGrade
                    finalGradeText.text = finalGrade.toString()
                }

                adapter.submitList(it.grades)
            }
        }


        binding.addGradeButton.setOnClickListener {
            val bottomSheet = NewGradeBottomSheetFragment(
                onAddGrade = { viewModel.addGrade(it) }
            )

            bottomSheet.show(parentFragmentManager, "NewGradeBottomSheetFragment")
        }

        binding.addDescriptionButton.setOnClickListener {
            val bottomSheet = CreateDescriptionBottomSheetFragment(
                onAddDescription = { viewModel.addDescription(it) }
            )

            bottomSheet.show(parentFragmentManager, "NewPeriodDescriptionFragment")
        }


        lifecycleScope.launch {
            viewModel.uiEvent.collect {
                when (it) {
                    UIEvent.NavigateBack -> {
                        findNavController().navigateUp()
                    }
                    is UIEvent.ShowMessage -> {
                        Snackbar.make(
                            requireView(),
                            resources.getString(it.message),
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }
                    else -> {}
                }
            }
        }

        setUpWithNavController(binding.toolbar)
        setNavigateUp(binding.toolbar)
    }

}