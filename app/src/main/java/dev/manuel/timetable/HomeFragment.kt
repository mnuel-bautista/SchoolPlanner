package dev.manuel.timetable

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import dev.manuel.timetable.databinding.FragmentHomeBinding
import dev.manuel.timetable.ui.adapters.DayOfWeekFragment
import dev.manuel.timetable.ui.adapters.MPagerAdapter
import dev.manuel.timetable.ui.screens.TimetableScreen
import dev.manuel.timetable.ui.screens.TimetableVM
import dev.manuel.timetable.ui.theme.TimetableTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: TimetableVM by viewModels()

    private lateinit var navController: NavController

    private lateinit var binding: FragmentHomeBinding

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.main_menu, menu)
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

    override fun onStart() {
        super.onStart()
        requireActivity().addMenuProvider(menuProvider)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        val pager = binding.pager


        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
/*                viewLifecycleOwner.lifecycleScope.launch {
                    pagerState.scrollToPage(tab?.position ?: 0)
                }*/
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        navController = findNavController()

        val monday = DayOfWeekFragment()
        val tuesday = DayOfWeekFragment()
        val wednesday = DayOfWeekFragment()
        val thursday = DayOfWeekFragment()
        val friday = DayOfWeekFragment()

        val adapter = MPagerAdapter(
            this, listOf(
                monday, tuesday, wednesday, thursday, friday
            )
        )
        pager.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.courses.collect {
                monday.classes = it.filter { dow -> dow.occurrence.dayOfWeek.ordinal == 0 }
                tuesday.classes = it.filter { dow -> dow.occurrence.dayOfWeek.ordinal == 1 }
                wednesday.classes = it.filter { dow -> dow.occurrence.dayOfWeek.ordinal == 2 }
                thursday.classes = it.filter { dow -> dow.occurrence.dayOfWeek.ordinal == 3 }
                friday.classes = it.filter { dow -> dow.occurrence.dayOfWeek.ordinal == 4 }
            }
        }

        setToolbarTitle()

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = when (position) {
                0 -> resources.getString(R.string.monday)
                1 -> resources.getString(R.string.tuesday)
                2 -> resources.getString(R.string.wednesday)
                3 -> resources.getString(R.string.thursday)
                4 -> resources.getString(R.string.friday)
                else -> ""
            }
        }.attach()


    }


    private fun setToolbarTitle() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.timetable.collect { timetableName ->
                (requireActivity() as MainActivity).supportActionBar?.title = timetableName
            }
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().removeMenuProvider(menuProvider)
    }

}