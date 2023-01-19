package dev.manuel.timetable.ui.screens.notes

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.manuel.timetable.R
import dev.manuel.timetable.databinding.FragmentEditNoteBinding
import dev.manuel.timetable.getNavigationResult
import dev.manuel.timetable.ui.UIEvent
import dev.manuel.timetable.ui.screens.classes.SelectClassFragment
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditNoteFragment : Fragment() {

    private lateinit var binding: FragmentEditNoteBinding

    private val viewModel: EditNoteVM by viewModels()

    private val destinationChangeListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            if (destination.id != R.id.editNoteFragment) {
                Toast.makeText(requireContext(), "Toast", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.edit_note_screen_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.timetables_item -> {
                    findNavController().navigate(R.id.action_homeFragment_to_timetablesFragment)
                    true
                }
                else -> false
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findNavController().addOnDestinationChangedListener(destinationChangeListener)
        binding = FragmentEditNoteBinding.bind(view)
        getNavigationResult(SelectClassFragment.SELECTED_CLASS_ID)?.observe(viewLifecycleOwner) { result ->
            viewModel.changeClass(result)
        }
        binding.fragmentEditNoteContainer.setContent {
            Mdc3Theme {
                EditNoteScreen(
                    viewModel = viewModel,
                    onSelectClass = {
                        findNavController().navigate(R.id.action_editNoteFragment_to_selectClassFragment)
                    },
                    navController = findNavController()
                )
            }
        }

        lifecycleScope.launch {
            viewModel.uiEvent.collect {
                when (it) {
                    UIEvent.HideContextualMenu -> {}
                    UIEvent.NavigateBack -> {
                        findNavController().navigateUp()
                    }
                    UIEvent.ShowContextualMenu -> {}
                    is UIEvent.ShowMessage -> {
                        Snackbar.make(
                            requireView(),
                            resources.getString(it.message),
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().removeMenuProvider(menuProvider)
    }


}