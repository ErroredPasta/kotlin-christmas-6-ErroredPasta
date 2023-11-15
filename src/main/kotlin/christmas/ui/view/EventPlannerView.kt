package christmas.ui.view

import christmas.domain.model.Discount
import christmas.domain.model.Menu
import christmas.domain.util.runCatchingUntilSuccess
import christmas.ui.EventPlannerViewModel
import christmas.ui.UiState

class EventPlannerView(
    private val inputView: InputView,
    private val outputView: OutputView,
    private val viewModel: EventPlannerViewModel
) {
    fun start() {
        viewModel.setCallback { handleUiState(uiState = it) }
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

    private fun onGetMenusAndAmountsDone(menusAndAmounts: List<Pair<Menu, Int>>) {
        outputView.displayMessage(message = OutputView.PREVIEW_BENEFITS_MESSAGE)
        outputView.displayMenusAndAmounts(menusAndAmounts = menusAndAmounts)
        viewModel.calculateTotalPrice()
    }

    private fun onTotalPriceCalculated(totalPrice: Int) {
        outputView.displayMessage(message = "") // new line
        outputView.displayDiscountNotAppliedTotalPrice(totalPrice = totalPrice)
        viewModel.decideOnGiveaway()
    }

    private fun onGiveawayDecided(shouldGiveaway: Boolean) {
        outputView.displayMessage(message = "") // new line
        outputView.displayShouldGiveaway(shouldGiveaway = shouldGiveaway)
        viewModel.calculateDiscounts()
    }

    private fun onDiscountsCalculated(discounts: List<Discount>) {
        outputView.displayMessage(message = "") // new line
        outputView.displayDiscounts(discounts = discounts)
    }

    private fun handleUiState(uiState: UiState): Unit = when (uiState) {
        UiState.Initialized -> onStart()
        UiState.GetDateDone -> onGetDateDone()
        is UiState.GetMenusAndAmountsDone -> onGetMenusAndAmountsDone(menusAndAmounts = uiState.menusAndAmounts)
        is UiState.TotalPriceCalculated -> onTotalPriceCalculated(totalPrice = uiState.totalPrice)
        is UiState.GiveawayDecided -> onGiveawayDecided(shouldGiveaway = uiState.shouldGiveaway)
        is UiState.DiscountsCalculated -> onDiscountsCalculated(discounts = uiState.discounts)
        is UiState.Error -> handleError(errorState = uiState)
    }

    private fun handleError(errorState: UiState.Error) {
        outputView.displayErrorMessage(errorState.error.message)
        viewModel.rewindToPreviousState()
    }
}