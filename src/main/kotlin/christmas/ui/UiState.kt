package christmas.ui

import christmas.domain.model.Discount
import christmas.domain.model.Menu

sealed interface UiState {
    data object Initialized : UiState
    data object GetDateDone : UiState
    data class GetMenusAndAmountsDone(val menusAndAmounts: List<Pair<Menu, Int>>) : UiState
    data class DisplayMenusAndAmountsDone(val totalPrice: Int) : UiState
    data class DisplayDiscountNotAppliedTotalPriceDone(val shouldGiveaway: Boolean) : UiState
    data class DisplayShouldGiveawayDone(val discounts: List<Discount>) : UiState
    data class Error(val error: Throwable) : UiState
}