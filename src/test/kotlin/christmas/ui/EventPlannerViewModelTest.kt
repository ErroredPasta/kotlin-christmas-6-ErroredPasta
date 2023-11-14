package christmas.ui

import christmas.domain.exception.MenuNotExistException
import christmas.domain.logic.MenuValidator
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
import java.util.concurrent.atomic.AtomicReference
import java.util.stream.Stream

typealias AtomicThrowable = AtomicReference<Throwable>

class EventPlannerViewModelTest {
    private lateinit var viewModel: EventPlannerViewModel

    @BeforeEach
    fun setUp() {
        viewModel = EventPlannerViewModel(menuValidator = MenuValidator)
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

    companion object {
        const val VALID_DAY_OF_MONTH = 1

        @JvmStatic
        private fun provideInvalidFormatInputsForMenusAndAmounts(): Stream<Arguments> = Stream.of(
            Arguments.of(listOf("해산물파스타:2", "레드와인-1", "초코케이크-1")),
            Arguments.of(listOf("해산물파스타 - 2", "레드와인-1", "초코케이크-1")),
            Arguments.of(listOf("해산물파스타 2", "레드와인-1", "초코케이크-1")),
        )
    }
}