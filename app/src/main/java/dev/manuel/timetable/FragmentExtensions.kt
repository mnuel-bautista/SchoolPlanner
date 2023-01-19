package dev.manuel.timetable

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.getNavigationResult(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<dev.manuel.timetable.room.entities.Class>(key)

fun Fragment.setNavigationResult(result: dev.manuel.timetable.room.entities.Class, key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}