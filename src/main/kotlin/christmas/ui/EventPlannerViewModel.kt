package christmas.ui

import christmas.domain.constant.MONTH
import christmas.domain.constant.YEAR
import christmas.domain.logic.DiscountCalculator
import christmas.domain.logic.DiscountCalculator.calculatedTotalDiscountAmount
import christmas.domain.logic.MenuValidator
import christmas.domain.logic.calculateTotalPrice
import christmas.domain.logic.decideOnGiveaway
import christmas.domain.model.Badge
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
    private var totalDiscountAmount = TOTAL_DISCOUNT_AMOUNT_NOT_INITIALIZED

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

    fun calculateTotalPrice() {
        totalPrice = menusAndAmounts.calculateTotalPrice()
        this.uiState = UiState.TotalPriceCalculated(totalPrice = totalPrice)
    }

    fun decideOnGiveaway() {
        this.uiState =
            UiState.GiveawayDecided(shouldGiveaway = decideOnGiveaway(totalPrice = totalPrice))
    }

    fun calculateDiscounts() {
        discounts = discountCalculator.calculateDiscounts(
            date = date,
            menusAndAmounts = menusAndAmounts
        )

        this.uiState = UiState.DiscountsCalculated(discounts = discounts)
    }

    fun calculateTotalDiscountAmount() {
        totalDiscountAmount = discounts.calculatedTotalDiscountAmount()
        this.uiState = UiState.TotalDiscountAmountCalculated(totalDiscountAmount = totalDiscountAmount)
    }

    fun applyDiscounts() {
        // totalDiscountAmount는 음수이므로 서로 더해야한다.
        this.uiState = UiState.DiscountApplied(totalPrice = totalPrice + totalDiscountAmount)
    }

    fun decideBadge() {
        this.uiState =
            UiState.BadgeDecided(Badge.getBadgeByTotalDiscountAmount(totalDiscountAmount = totalDiscountAmount))
    }

    companion object {
        const val INVALID_DATE_MESSAGE = "유효하지 않은 날짜입니다. 다시 입력해 주세요."
        const val MENU_AMOUNT_DIVIDER = '-'
        const val REQUIRED_MENU_AMOUNT_DIVIDER_COUNT = 1
        const val INVALID_ORDER_MESSAGE = "유효하지 않은 주문입니다. 다시 입력해 주세요."
        const val TOTAL_PRICE_NOT_INITIALIZED = -1
        const val TOTAL_DISCOUNT_AMOUNT_NOT_INITIALIZED = -1
    }
}