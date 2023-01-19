package dev.manuel.timetable.ui.screens.classes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import dagger.hilt.android.AndroidEntryPoint
import dev.manuel.timetable.R
import dev.manuel.timetable.databinding.FragmentSelectClassBinding
import dev.manuel.timetable.setNavigationResult

@AndroidEntryPoint
class SelectClassFragment : Fragment() {

    private lateinit var binding: FragmentSelectClassBinding

    private val viewModel: SelectClassVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_class, container, false)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSelectClassBinding.bind(view)

        binding.fragmentSelectClassContainer.setContent {
            val classes by viewModel.classes.collectAsState()
            Mdc3Theme {
                LazyColumn {
                    items(classes) {
                        Card(modifier = Modifier.clickable {
                            setNavigationResult(it, SELECTED_CLASS_ID)
                            findNavController().popBackStack()
                        }) {
                            ListItem(
                                headlineText = { Text(text = it.subject) }
                            )
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val SELECTED_CLASS_ID = "dev.mnuel.timetable.selected_class_id"
    }

}