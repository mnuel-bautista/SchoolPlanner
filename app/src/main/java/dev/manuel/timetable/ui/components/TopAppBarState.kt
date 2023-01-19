package dev.manuel.timetable.ui.components

enum class TopAppBarState {
    /**
     * The app bar shows the default items.
     * */
    Default,

    /***
     * When one item is selected.
     */
    Context,

    /**
     * When more than one item is selected.
     * */
    ContextMore
}