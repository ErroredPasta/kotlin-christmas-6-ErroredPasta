package christmas.ui.view

import christmas.domain.util.runCatchingUntilSuccess
import christmas.ui.EventPlannerViewModel

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

    private fun subscribeUiState() {
        viewModel.setCallback { }
    }
}