package christmas.ui

import christmas.domain.model.Discount
import christmas.domain.model.Menu

sealed interface UiState {
    data object Initialized : UiState
    data object GetDateDone : UiState
    data class GetMenusAndAmountsDone(val menusAndAmounts: List<Pair<Menu, Int>>) : UiState
    data class TotalPriceCalculated(val totalPrice: Int) : UiState
    data class GiveawayDecided(val shouldGiveaway: Boolean) : UiState
    data class DiscountsCalculated(val discounts: List<Discount>) : UiState
    data class TotalDiscountAmountCalculated(val totalDiscountAmount: Int) : UiState
    data class Error(val error: Throwable) : UiState
}