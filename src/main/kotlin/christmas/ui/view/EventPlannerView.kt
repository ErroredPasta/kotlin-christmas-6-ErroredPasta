package christmas.ui.view

import christmas.domain.util.runCatchingUntilSuccess
import christmas.ui.EventPlannerViewModel
import christmas.ui.UiState

class EventPlannerView(
    private val inputView: InputView,
    private val outputView: OutputView
) {
    private val viewModel = EventPlannerViewModel()

    fun onStart() {
        subscribeUiState()

        outputView.displayMessage(OutputView.WELCOME_MESSAGE)

        runCatchingUntilSuccess(
            block = { inputView.getDate() },
            onSuccess = { date -> viewModel.setDate(date = date) },
            onFailure = { error -> outputView.displayErrorMessage(error.message) }
        )
    }

    fun onGetDateDone() = runCatchingUntilSuccess(
        block = { inputView.getMenusAndAmounts() },
        onSuccess = { menusAndAmounts -> viewModel.setMenusAndAmounts(menusAndAmounts = menusAndAmounts) },
        onFailure = { error -> outputView.displayErrorMessage(error.message) }
    )

    private fun subscribeUiState() {
        viewModel.setCallback { uiState ->
            when (uiState) {
                UiState.Initialized -> Unit
                UiState.GetDateDone -> onGetDateDone()
                UiState.GetMenusAndAmountsDone -> Unit // TODO implement next action
                is UiState.Error -> handleError(errorState = uiState)
            }
        }
    }

    private fun handleError(errorState: UiState.Error) {
        outputView.displayErrorMessage(errorState.error.message)
        viewModel.rewindToPreviousState()
    }
}