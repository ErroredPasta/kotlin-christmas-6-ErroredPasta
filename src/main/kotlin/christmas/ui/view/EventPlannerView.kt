package christmas.ui.view

import christmas.domain.util.runCatchingUntilSuccess
import christmas.ui.EventPlannerViewModel
import christmas.ui.UiState

class EventPlannerView(
    private val inputView: InputView,
    private val outputView: OutputView,
    private val viewModel: EventPlannerViewModel
) {
    fun start() {
        viewModel.setCallback { uiState ->
            when (uiState) {
                UiState.Initialized -> onStart()
                UiState.GetDateDone -> onGetDateDone()
                UiState.GetMenusAndAmountsDone -> Unit // TODO implement next action
                is UiState.Error -> handleError(errorState = uiState)
            }
        }

        onStart()
    }

    private fun onStart() {
        outputView.displayMessage(OutputView.WELCOME_MESSAGE)

        runCatchingUntilSuccess(
            block = { inputView.getDayOfMonth() },
            onSuccess = { date -> viewModel.setDate(dayOfMonth = date) },
            onFailure = { error -> outputView.displayErrorMessage(error.message) }
        )
    }

    private fun onGetDateDone() = runCatchingUntilSuccess(
        block = { inputView.getMenusAndAmounts() },
        onSuccess = { menusAndAmounts -> viewModel.setMenusAndAmounts(menusAndAmounts = menusAndAmounts) },
        onFailure = { error -> outputView.displayErrorMessage(error.message) }
    )

    private fun handleError(errorState: UiState.Error) {
        outputView.displayErrorMessage(errorState.error.message)
        viewModel.rewindToPreviousState()
    }
}