package christmas.ui

import christmas.domain.exception.MenuNotExistException
import christmas.domain.logic.DiscountCalculator
import christmas.domain.logic.MenuValidator
import christmas.domain.model.Discount
import christmas.domain.model.Menu
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import java.util.stream.Stream

typealias AtomicThrowable = AtomicReference<Throwable>

class EventPlannerViewModelTest {
    private lateinit var viewModel: EventPlannerViewModel

    @BeforeEach
    fun setUp() {
        viewModel = EventPlannerViewModel(menuValidator = MenuValidator, discountCalculator = DiscountCalculator)
    }

    @Test
    @DisplayName("callback 등록시 uiState 변경시 callback이 호출됨")
    fun setCallback_uiStateChanges_invokeCallback() {
        // given
        val isCallbackInvoked = AtomicBoolean(false)

        // when
        viewModel.setCallback { isCallbackInvoked.set(true) }

        // 성공적으로 setDate가 호출되면 uiState가 변경됨
        viewModel.setDate(dayOfMonth = VALID_DAY_OF_MONTH)

        // then
        assertThat(isCallbackInvoked.get()).isTrue()
    }

    @Test
    @DisplayName("rewindToPreviousState 호출시 성공적으로 이번 uiState로 되돌림")
    fun rewindToPreviousState_successfullyRewindToPreviousUiState() {
        // given
        // 성공적으로 setDate가 호출되면 uiState가 변경됨
        val isRewound = AtomicBoolean(false)
        val isGetDateDone = AtomicBoolean(false)

        viewModel.setCallback { uiState ->
            when (uiState) {
                UiState.Initialized -> isRewound.set(true)
                UiState.GetDateDone -> isGetDateDone.set(true)
                else -> Unit
            }
        }

        viewModel.setDate(dayOfMonth = VALID_DAY_OF_MONTH)

        // when
        viewModel.rewindToPreviousState()

        // then
        assertThat(isRewound.get()).isTrue()
        assertThat(isGetDateDone.get()).isTrue()
    }

    @Test
    @DisplayName("올바른 날짜 입력시 uiState가 GetDateDone으로 변경")
    fun setDate_validDayOfMonth_uiStateChangesToGetDateDone() {
        // given
        val isGetDateDone = AtomicBoolean(false)
        val hasErrorOccurred = AtomicBoolean(false)

        viewModel.setCallback { uiState ->
            when (uiState) {
                UiState.GetDateDone -> isGetDateDone.set(true)
                is UiState.Error -> hasErrorOccurred.set(true)
                else -> Unit
            }
        }

        // when
        viewModel.setDate(dayOfMonth = VALID_DAY_OF_MONTH)

        // then
        assertThat(isGetDateDone.get()).isTrue()
        assertThat(hasErrorOccurred.get()).isFalse()
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 0, 32])
    @DisplayName("올바르지 않은 범위의 날짜를 입력시 uiState에 에러사항이 반영됨")
    fun setDate_invalidDayOfMonth_uiStateChangesToError(invalidDayOfMonth: Int) {
        // given
        val error = AtomicThrowable(null)
        viewModel.setCallback { uiState ->
            if (uiState is UiState.Error) error.set(uiState.error)
        }

        // when
        viewModel.setDate(dayOfMonth = invalidDayOfMonth)

        // then
        assertThat(error.get()).isInstanceOf(IllegalArgumentException::class.java)
        assertThat(error.get().message).contains(EventPlannerViewModel.INVALID_DATE_MESSAGE)
    }

    @Test
    @DisplayName("알맞은 주문과 개수 입력 시 uiState가 GetMenusAndAmountsDone으로 변경되고 주문한 메뉴가 반영됨")
    fun setMenusAndAmounts_validMenusAndAmounts_uiStateChangesToGetMenusAndAmountsDone() {
        // given
        val validMenusAndAmounts = listOf("해산물파스타-2", "레드와인-1", "초코케이크-1")

        val menusAndAmounts = AtomicReference<List<Pair<Menu, Int>>>(null)
        val hasErrorOccurred = AtomicBoolean(false)
        viewModel.setCallback { uiState ->
            when (uiState) {
                is UiState.GetMenusAndAmountsDone -> menusAndAmounts.set(uiState.menusAndAmounts)
                is UiState.Error -> hasErrorOccurred.set(true)
                else -> Unit
            }
        }

        // when
        viewModel.setMenusAndAmounts(menusAndAmounts = validMenusAndAmounts)

        // then
        val expected = listOf(Menu.SEAFOOD_PASTA to 2, Menu.RED_WINE to 1, Menu.CHOCOLATE_CAKE to 1)
        assertThat(menusAndAmounts.get()).isEqualTo(expected)
        assertThat(hasErrorOccurred.get()).isFalse()
    }

    @Test
    @DisplayName("없는 메뉴 입력시 uiState에 에러사항이 반영됨")
    fun setMenusAndAmounts_menuNotExists_uiStateChangesToError() {
        // given
        val menuNotExistInput = listOf("크림파스타-2", "레드와인-1", "초코케이크-1")

        val error = AtomicThrowable(null)
        viewModel.setCallback { uiState ->
            if (uiState is UiState.Error) error.set(uiState.error)
        }

        // when
        viewModel.setMenusAndAmounts(menusAndAmounts = menuNotExistInput)

        // then
        assertThat(error.get()).isInstanceOf(MenuNotExistException::class.java)
        assertThat(error.get().message).contains(Menu.MENU_NOT_EXIST_MESSAGE)
    }

    @Test
    @DisplayName("올바르지 않은 개수 입력시 uiState에 에러사항이 반영됨")
    fun setMenusAndAmounts_invalidAmount_uiStateChangesToError() {
        // given
        val invalidAmountInput = listOf("해산물파스타-0", "레드와인-1", "초코케이크-1")

        val error = AtomicThrowable(null)
        viewModel.setCallback { uiState ->
            if (uiState is UiState.Error) error.set(uiState.error)
        }

        // when
        viewModel.setMenusAndAmounts(menusAndAmounts = invalidAmountInput)

        // then
        assertThat(error.get()).isInstanceOf(IllegalArgumentException::class.java)
        assertThat(error.get().message).contains(MenuValidator.INVALID_AMOUNT_MESSAGE)
    }

    @ParameterizedTest
    @MethodSource("provideInvalidFormatInputsForMenusAndAmounts")
    @DisplayName("올바르지 않은 메뉴 형식을 입력할 경우 uiState에 에러사항이 반영됨")
    fun setMenusAndAmounts_invalidFormatForMenusAndAmounts_uiStateChangesToError(invalidFormatInput: List<String>) {
        // given
        val error = AtomicThrowable(null)
        viewModel.setCallback { uiState ->
            if (uiState is UiState.Error) error.set(uiState.error)
        }

        // when
        viewModel.setMenusAndAmounts(menusAndAmounts = invalidFormatInput)

        // then
        assertThat(error.get()).isInstanceOf(IllegalArgumentException::class.java)
        assertThat(error.get().message).contains(EventPlannerViewModel.INVALID_ORDER_MESSAGE)
    }

    @Test
    @DisplayName("중복된 메뉴가 있을 경우 uiState에 에러사항이 반영됨")
    fun setMenusAndAmounts_invalidFormatForMenusAndAmounts_uiStateChangesToError() {
        // given
        val duplicatedMenuInput = listOf("해산물파스타-2", "레드와인-1", "초코케이크-1", "해산물파스타-2")

        val error = AtomicThrowable(null)
        viewModel.setCallback { uiState ->
            if (uiState is UiState.Error) error.set(uiState.error)
        }

        // when
        viewModel.setMenusAndAmounts(menusAndAmounts = duplicatedMenuInput)

        // then
        assertThat(error.get()).isInstanceOf(IllegalArgumentException::class.java)
        assertThat(error.get().message).contains(MenuValidator.DUPLICATED_MENU_MESSAGE)
    }

    @Test
    @DisplayName("음료만 시킬 경우 uiState에 에러사항이 반영됨")
    fun setMenusAndAmounts_orderOnlyDrinks_uiStateChangesToError() {
        // given
        val onlyDrinks = listOf("레드와인-1", "제로콜라-1", "샴페인-2")

        val error = AtomicThrowable(null)
        viewModel.setCallback { uiState ->
            if (uiState is UiState.Error) error.set(uiState.error)
        }

        // when
        viewModel.setMenusAndAmounts(menusAndAmounts = onlyDrinks)

        // then
        assertThat(error.get()).isInstanceOf(IllegalArgumentException::class.java)
        assertThat(error.get().message).contains(MenuValidator.ONLY_DRINKS_ORDER_MESSAGE)
    }

    @Test
    @DisplayName("한번에 20개 이상 메뉴를 주문하는 경우 uiState에 에러사항이 반영됨")
    fun setMenusAndAmounts_sumOfAmountIsGreaterThanMaxSumOfAmounts_uiStateChangesToError() {
        // given
        val tooManyAmounts = listOf("레드와인-5", "제로콜라-5", "샴페인-5", "티본스테이크-6")

        val error = AtomicThrowable(null)
        viewModel.setCallback { uiState ->
            if (uiState is UiState.Error) error.set(uiState.error)
        }

        // when
        viewModel.setMenusAndAmounts(menusAndAmounts = tooManyAmounts)

        // then
        assertThat(error.get()).isInstanceOf(IllegalArgumentException::class.java)
        assertThat(error.get().message).contains(MenuValidator.TOO_MANY_AMOUNTS_MESSAGE)
    }

    @ParameterizedTest
    @MethodSource("provideMenusAndAmounts")
    @DisplayName("주문한 메뉴를 보여준 뒤 총 금액을 계산하여 uiState에 반영")
    fun calculateTotalPrice_menusAndAmountsSet_uiStateIncludesTotalPrice(
        menusAndAmounts: List<String>,
        expected: Int
    ) {
        // given
        viewModel.setMenusAndAmounts(menusAndAmounts = menusAndAmounts)

        val totalPrice = AtomicInteger()
        viewModel.setCallback { uiState ->
            if (uiState is UiState.TotalPriceCalculated) totalPrice.set(uiState.totalPrice)
        }

        // when
        viewModel.calculateTotalPrice()

        // then
        assertThat(totalPrice.get()).isEqualTo(expected)
    }

    @ParameterizedTest
    @MethodSource("provideMenusAndAmountsForGiveaway")
    @DisplayName("총 금액에 따라 증정 메뉴를 증정할지 uiState에 반영")
    fun decideOnGiveaway_menusAndAmountsSet_uiStateIncludesShouldGiveaway(
        menusAndAmounts: List<String>,
        expected: Boolean
    ) {
        // given
        viewModel.setMenusAndAmounts(menusAndAmounts = menusAndAmounts)
        viewModel.calculateTotalPrice()

        val totalPrice = AtomicBoolean(false)
        viewModel.setCallback { uiState ->
            if (uiState is UiState.GiveawayDecided) totalPrice.set(uiState.shouldGiveaway)
        }

        // when
        viewModel.decideOnGiveaway()

        // then
        assertThat(totalPrice.get()).isEqualTo(expected)
    }

    @ParameterizedTest
    @MethodSource("provideValidParametersForDiscounts")
    @DisplayName("날짜, 주문 메뉴에 따라 어떤 혜택을 제공할지 uiState에 반영")
    fun calculateDiscounts_validParametersGiven_uiStateIncludesDiscounts(
        menusAndAmounts: List<String>,
        dayOfMonth: Int,
        expected: List<Discount>
    ) {
        // given
        viewModel.setDate(dayOfMonth = dayOfMonth)
        viewModel.setMenusAndAmounts(menusAndAmounts = menusAndAmounts)

        val discounts = AtomicReference<List<Discount>>()
        viewModel.setCallback { uiState ->
            if (uiState is UiState.DiscountsCalculated) discounts.set(uiState.discounts)
        }

        // when
        viewModel.calculateDiscounts()

        // then
        assertThat(discounts.get().size).isEqualTo(expected.size)
        expected.forEach { assertThat(discounts.get()).contains(it) }
    }

    @ParameterizedTest
    @MethodSource("provideValidParametersForTotalDiscountAmount")
    @DisplayName("혜택이 정해지고 총 혜택 금액 계산시 uiState에 총 혜택 금액 반영")
    fun calculateTotalDiscountAmount_discountsGiven_uiStateIncludesTotalDiscountAmount(
        menusAndAmounts: List<String>,
        dayOfMonth: Int,
        expected: Int
    ) {
        // given
        viewModel.setDate(dayOfMonth = dayOfMonth)
        viewModel.setMenusAndAmounts(menusAndAmounts = menusAndAmounts)
        viewModel.calculateDiscounts()

        val totalDiscountAmount = AtomicInteger()
        viewModel.setCallback { uiState ->
            if (uiState is UiState.TotalDiscountAmountCalculated) totalDiscountAmount.set(uiState.totalDiscountAmount)
        }

        // when
        viewModel.calculateTotalDiscountAmount()

        // then
        assertThat(totalDiscountAmount.get()).isEqualTo(expected)
    }

    @ParameterizedTest
    @MethodSource("provideValidParametersToApplyDiscounts")
    @DisplayName("총 혜택 금액 계산 후 총 주문 금액에 적용시 uiState에 혜택이 반영된 주문 금액이 반영")
    fun applyDiscounts_menusAnddiscountsGiven_uiStateIncludesDiscountAppliedTotalPrice(
        menusAndAmounts: List<String>,
        dayOfMonth: Int,
        expected: Int
    ) {
        // given
        viewModel.setDate(dayOfMonth = dayOfMonth)
        viewModel.setMenusAndAmounts(menusAndAmounts = menusAndAmounts)
        viewModel.calculateTotalPrice()
        viewModel.calculateDiscounts()
        viewModel.calculateTotalDiscountAmount()

        val discountAppliedTotalPrice = AtomicInteger()
        viewModel.setCallback { uiState ->
            if (uiState is UiState.DiscountApplied) discountAppliedTotalPrice.set(uiState.totalPrice)
        }

        // when
        viewModel.applyDiscounts()

        // then
        assertThat(discountAppliedTotalPrice.get()).isEqualTo(expected)
    }

    companion object {
        const val VALID_DAY_OF_MONTH = 1

        @JvmStatic
        private fun provideInvalidFormatInputsForMenusAndAmounts(): Stream<Arguments> = Stream.of(
            Arguments.of(listOf("해산물파스타:2", "레드와인-1", "초코케이크-1")),
            Arguments.of(listOf("해산물파스타 - 2", "레드와인-1", "초코케이크-1")),
            Arguments.of(listOf("해산물파스타 2", "레드와인-1", "초코케이크-1")),
        )

        @JvmStatic
        private fun provideMenusAndAmounts(): Stream<Arguments> = Stream.of(
            Arguments.of(listOf("해산물파스타-2", "레드와인-1", "초코케이크-1"), 145_000),
            Arguments.of(listOf("타파스-1", "제로콜라-1"), 8_500),
            Arguments.of(listOf("티본스테이크-1", "바비큐립-1", "초코케이크-2", "제로콜라-1"), 142_000),
        )

        @JvmStatic
        private fun provideMenusAndAmountsForGiveaway(): Stream<Arguments> = Stream.of(
            Arguments.of(listOf("해산물파스타-2", "레드와인-1", "초코케이크-1"), true),
            Arguments.of(listOf("타파스-1", "제로콜라-1"), false),
            Arguments.of(listOf("티본스테이크-1", "바비큐립-1", "초코케이크-2", "제로콜라-1"), true),
        )

        @JvmStatic
        private fun provideValidParametersForDiscounts(): Stream<Arguments> = Stream.of(
            Arguments.of(
                listOf("해산물파스타-2", "레드와인-1", "초코케이크-1"),
                25,
                listOf(
                    Discount.Christmas(discountAmount = -3_400),
                    Discount.Weekday(discountAmount = -2_023),
                    Discount.StarDay,
                    Discount.Giveaway
                )
            ),
            Arguments.of(listOf("타파스-1", "제로콜라-1"), 26, emptyList<Discount>()),
            Arguments.of(
                listOf("티본스테이크-1", "바비큐립-1", "초코케이크-2", "제로콜라-1"),
                3,
                listOf(
                    Discount.Christmas(discountAmount = -1_200),
                    Discount.Weekday(discountAmount = -4_046),
                    Discount.StarDay,
                    Discount.Giveaway
                )
            ),
        )

        @JvmStatic
        private fun provideValidParametersForTotalDiscountAmount(): Stream<Arguments> = Stream.of(
            Arguments.of(listOf("해산물파스타-2", "레드와인-1", "초코케이크-1"), 25, -31_423),
            Arguments.of(listOf("타파스-1", "제로콜라-1"), 26, 0),
            Arguments.of(listOf("티본스테이크-1", "바비큐립-1", "초코케이크-2", "제로콜라-1"), 3, -31_246),
        )

        @JvmStatic
        private fun provideValidParametersToApplyDiscounts(): Stream<Arguments> = Stream.of(
            Arguments.of(listOf("해산물파스타-2", "레드와인-1", "초코케이크-1"), 25, 113_577),
            Arguments.of(listOf("타파스-1", "제로콜라-1"), 26, 8_500),
            Arguments.of(listOf("티본스테이크-1", "바비큐립-1", "초코케이크-2", "제로콜라-1"), 3, 110_754),
        )
    }
}