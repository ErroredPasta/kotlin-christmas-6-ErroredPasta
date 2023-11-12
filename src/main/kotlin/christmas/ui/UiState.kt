package christmas.ui

sealed interface UiState {
    data object Initialized : UiState
    data object GetDateDone : UiState
    data object GetMenusAndAmountsDone : UiState
    data class Error(val error: Throwable) : UiState
}