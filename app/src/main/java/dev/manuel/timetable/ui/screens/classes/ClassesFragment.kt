package dev.manuel.timetable.ui.screens.classes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Class
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import dagger.hilt.android.AndroidEntryPoint
import dev.manuel.timetable.R
import dev.manuel.timetable.databinding.FragmentClassesBinding

@AndroidEntryPoint
class ClassesFragment : Fragment() {

    private lateinit var binding: FragmentClassesBinding

    private val viewModel: ClassesVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_classes, container, false)
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentClassesBinding.bind(view)
        val navController = findNavController()
        binding.classesFragmentContainer.setContent {
            val classes by viewModel.classes.collectAsState()
            Mdc3Theme {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(classes) {
                        Surface(
                            onClick = {
                                navController.navigate(
                                    R.id.action_classesFragment_to_classDetailsFragment,
                                    bundleOf("classId" to it.id)
                                )
                            }
                        ) {
                            ListItem(
                                headlineText = { Text(text = it.subject) },
                                leadingContent = {
                                    Icon(
                                        imageVector = Icons.Outlined.Class, contentDescription = ""
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
    }

}