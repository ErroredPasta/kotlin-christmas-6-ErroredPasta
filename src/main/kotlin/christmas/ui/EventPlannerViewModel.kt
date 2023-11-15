package christmas.ui

import christmas.domain.logic.DiscountCalculator
import christmas.domain.logic.MenuValidator
import christmas.domain.logic.calculateTotalPrice
import christmas.domain.model.Discount
import christmas.domain.model.Menu
import christmas.domain.util.onFailureOtherThanNoSuchElementException
import java.time.LocalDate
import kotlin.properties.Delegates

typealias Callback = (UiState) -> Unit

class EventPlannerViewModel(
    private val menuValidator: MenuValidator,
    private val discountCalculator: DiscountCalculator
) {
    private lateinit var date: LocalDate
    private lateinit var menusAndAmounts: List<Pair<Menu, Int>>
    private var totalPrice = TOTAL_PRICE_NOT_INITIALIZED
    private lateinit var discounts: List<Discount>

    private var callback: Callback? = null

    private var previousState: UiState = UiState.Initialized
        set(value) {
            if (value is UiState.Error) return // Error state로 되돌일 일은 없으므로 저장하지 않는다
            field = value
        }

    private var uiState: UiState by Delegates.observable(UiState.Initialized) { _, oldValue, newValue ->
        previousState = oldValue
        callback?.invoke(newValue)
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    fun rewindToPreviousState() {
        uiState = previousState
    }

    fun setDate(dayOfMonth: Int) {
        runCatching {
            LocalDate.of(YEAR, MONTH, dayOfMonth)
        }.onSuccess {
            date = it
            uiState = UiState.GetDateDone
        }.onFailureOtherThanNoSuchElementException {
            uiState = UiState.Error(error = IllegalArgumentException(INVALID_DATE_MESSAGE))
        }
    }

    fun setMenusAndAmounts(menusAndAmounts: List<String>) {
        runCatching {
            menusAndAmounts.checkFormats()
            convertToMenusAndAmounts(input = menusAndAmounts).also { menuValidator.validate(menusAndAmounts = it) }
        }.onSuccess {
            this.menusAndAmounts = it
            uiState = UiState.GetMenusAndAmountsDone(menusAndAmounts = it)
        }.onFailureOtherThanNoSuchElementException { error ->
            uiState = when (error) {
                is NumberFormatException -> UiState.Error(error = IllegalArgumentException(INVALID_ORDER_MESSAGE))
                is IndexOutOfBoundsException -> UiState.Error(error = IllegalArgumentException(INVALID_ORDER_MESSAGE))
                else -> UiState.Error(error = error)
            }
        }
    }

    private fun List<String>.checkFormats() = forEach { menuAndAmount ->
        require(!menuAndAmount.any { it.isWhitespace() }) { INVALID_ORDER_MESSAGE }
        require(menuAndAmount.count { it == MENU_AMOUNT_DIVIDER } == REQUIRED_MENU_AMOUNT_DIVIDER_COUNT) {
            INVALID_ORDER_MESSAGE
        }
    }

    private fun convertToMenusAndAmounts(input: List<String>): List<Pair<Menu, Int>> = input.map {
        val (menuName, amount) = it.split(MENU_AMOUNT_DIVIDER)
        Menu.getMenuByMenuName(menuName = menuName) to amount.toInt()
    }

    fun displayMenusAndAmountsDone() {
        totalPrice = menusAndAmounts.calculateTotalPrice()
        this.uiState = UiState.DisplayMenusAndAmountsDone(totalPrice = totalPrice)
    }

    fun displayDiscountNotAppliedTotalPriceDone() {
        this.uiState = UiState.DisplayDiscountNotAppliedTotalPriceDone(shouldGiveaway = totalPrice >= GIVEAWAY_PRICE)
    }

    fun displayShouldGiveawayDone() {
        discounts = discountCalculator.calculateDiscounts(
            date = date,
            shouldGiveaway = totalPrice >= GIVEAWAY_PRICE,
            menusAndAmounts = menusAndAmounts
        )

        this.uiState = UiState.DisplayShouldGiveawayDone(discounts = discounts)
    }

    companion object {
        const val YEAR = 2023
        const val MONTH = 12
        const val INVALID_DATE_MESSAGE = "유효하지 않은 날짜입니다. 다시 입력해 주세요."
        const val MENU_AMOUNT_DIVIDER = '-'
        const val REQUIRED_MENU_AMOUNT_DIVIDER_COUNT = 1
        const val INVALID_ORDER_MESSAGE = "유효하지 않은 주문입니다. 다시 입력해 주세요."
        const val TOTAL_PRICE_NOT_INITIALIZED = -1
        const val GIVEAWAY_PRICE = 120_000 // TODO: rename this
    }
}