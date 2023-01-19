package dev.manuel.timetable.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.SharedFlow

sealed class UIEvent {
    object NavigateBack: UIEvent()
    object ShowContextualMenu: UIEvent()
    object HideContextualMenu: UIEvent()
    class ShowMessage(@StringRes val message: Int): UIEvent()
}

@Composable
fun SharedFlow<UIEvent>.safeCollector(
    onNavigateUp: () -> Unit = {},
    onShowMessage: (message: Int) -> Unit = {},
    onShowContextualMenu: () -> Unit = {},
    onHideContextualMenu: () -> Unit = {},
) = apply {
    LaunchedEffect(key1 = this) {
        collect {
            when (it) {
                is UIEvent.NavigateBack -> onNavigateUp()
                is UIEvent.ShowMessage -> onShowMessage(it.message)
                is UIEvent.ShowContextualMenu -> onShowContextualMenu()
                is UIEvent.HideContextualMenu -> onHideContextualMenu()
            }
        }
    }
}