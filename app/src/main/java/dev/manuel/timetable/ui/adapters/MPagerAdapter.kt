package dev.manuel.timetable.ui.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.manuel.timetable.R
import dev.manuel.timetable.room.entities.OccurrenceWithClass

class MPagerAdapter(
    fragment: Fragment,
    var children: List<Fragment>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return children[position]
    }
}

class DayOfWeekFragment: Fragment() {

    var classes: List<OccurrenceWithClass> = emptyList()
        set(value) {
            adapter?.submitList(value)
            field = value
        }

    private var adapter: ClassAdapter? = null

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.day_of_week_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recycler = view.findViewById<RecyclerView>(R.id.classesRecycler)
        navController = findNavController()
        adapter = ClassAdapter {
            navController.navigate(
                R.id.action_homeFragment_to_classDetailsFragment,
                bundleOf("classId" to it.mClass.id)
            )
        }
        recycler.adapter = adapter
        adapter?.submitList(classes)
        recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}