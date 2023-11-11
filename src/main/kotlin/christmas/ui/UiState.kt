package christmas.ui

sealed interface UiState {
    data object Initialized : UiState
}