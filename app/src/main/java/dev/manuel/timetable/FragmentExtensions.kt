package dev.manuel.timetable

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar

fun Fragment.getNavigationResult(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<dev.manuel.timetable.room.entities.Class>(key)

fun Fragment.setNavigationResult(result: dev.manuel.timetable.room.entities.Class, key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}


fun Fragment.setUpWithNavController(toolbar: MaterialToolbar) {
    val navController = findNavController()

    val activity = activity as MainActivity

    val appbarConfiguration = AppBarConfiguration(
        topLevelDestinationIds = setOf(R.id.homeFragment)
    )
    toolbar.setupWithNavController(navController, appbarConfiguration)

    activity.setSupportActionBar(toolbar)
}

fun Fragment.setNavigateUp(toolbar: MaterialToolbar) {
    toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
}