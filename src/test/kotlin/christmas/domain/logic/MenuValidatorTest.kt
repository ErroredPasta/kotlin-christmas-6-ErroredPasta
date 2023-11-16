package christmas.domain.logic

import christmas.domain.model.Menu
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class MenuValidatorTest {
    @Test
    @DisplayName("올바른 메뉴와 개수가 주어질 경우 예외가 발생하지 않음")
    fun validate_validMenusAndAmounts_nothingHappens() {
        // given
        val validMenusAndAmounts = listOf(
            Menu.SEAFOOD_PASTA to 2,
            Menu.RED_WINE to 1,
            Menu.CHOCOLATE_CAKE to 1
        )

        // when, then
        assertDoesNotThrow {
            MenuValidator.validate(menusAndAmounts = validMenusAndAmounts)
        }
    }

    @Test
    @DisplayName("중복 메뉴가 있을 경우 예외 발생")
    fun validate_duplicatedMenuExists_throwIllegalArgumentException() {
        // given
        val duplicatedMenus = listOf(
            Menu.SEAFOOD_PASTA to 2,
            Menu.RED_WINE to 1,
            Menu.CHOCOLATE_CAKE to 1,
            Menu.SEAFOOD_PASTA to 2
        )

        assertThatIllegalArgumentException()
            .isThrownBy { MenuValidator.validate(menusAndAmounts = duplicatedMenus) } // when
            .withMessageContaining(MenuValidator.DUPLICATED_MENU_MESSAGE) // then
    }

    @Test
    @DisplayName("올바르지 않은 개수가 주어질 경우 예외 발생")
    fun validate_amountIsSmallerThanMinAmount_throwIllegalArgumentException() {
        // given
        val menusAndInvalidAmount = listOf(
            Menu.SEAFOOD_PASTA to -2, // invalid amount
            Menu.RED_WINE to 1,
            Menu.CHOCOLATE_CAKE to 1,
        )

        assertThatIllegalArgumentException()
            .isThrownBy { MenuValidator.validate(menusAndAmounts = menusAndInvalidAmount) } // when
            .withMessageContaining(MenuValidator.INVALID_AMOUNT_MESSAGE) // then
    }

    @Test
    @DisplayName("한번에 20개 이상의 주문을 할 경우 예외 발생")
    fun validate_sumOfAmountsIsGreaterThanMaxSumOfAmounts_throwIllegalArgumentException() {
        // given
        val tooManyAmounts = listOf(
            Menu.SEAFOOD_PASTA to 5,
            Menu.RED_WINE to 5,
            Menu.CHOCOLATE_CAKE to 5,
            Menu.T_BONE_STEAK to 6
        )

        assertThatIllegalArgumentException()
            .isThrownBy { MenuValidator.validate(menusAndAmounts = tooManyAmounts) } // when
            .withMessageContaining(MenuValidator.TOO_MANY_AMOUNTS_MESSAGE) // then
    }

    @Test
    @DisplayName("음료만 주문을 할 경우 예외 발생")
    fun validate_orderOnlyDrinks_throwIllegalArgumentException() {
        // given
        val onlyDrinks = listOf(
            Menu.ZERO_COKE to 1,
            Menu.RED_WINE to 1,
            Menu.CHAMPAGNE to 1,
        )

        assertThatIllegalArgumentException()
            .isThrownBy { MenuValidator.validate(menusAndAmounts = onlyDrinks) } // when
            .withMessageContaining(MenuValidator.ONLY_DRINKS_ORDER_MESSAGE) // then
    }
}