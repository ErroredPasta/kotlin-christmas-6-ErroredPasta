package christmas.domain.logic

import christmas.domain.constant.MONTH
import christmas.domain.constant.YEAR
import christmas.domain.logic.DiscountCalculator.calculatedTotalDiscountAmount
import christmas.domain.model.Discount
import christmas.domain.model.Menu
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDate
import java.util.stream.Stream

class DiscountCalculatorTest {

    @ParameterizedTest
    @MethodSource("provideMenusAndAmountsForToCalculateDiscounts")
    @DisplayName("메뉴와 날짜가 결정되면 어떤 할인을 받을지 계산")
    fun calculateDiscounts_requiredParametersGiven_calculateDiscountsCorrectly(
        menusAndAmounts: List<Pair<Menu, Int>>,
        dayOfMonth: Int,
        expected: List<Discount>
    ) {
        // given
        val date = LocalDate.of(YEAR, MONTH, dayOfMonth)

        // when
        val result = DiscountCalculator.calculateDiscounts(
            menusAndAmounts = menusAndAmounts,
            date = date,
        )

        // then
        assertThat(result.size).isEqualTo(expected.size)
        expected.forEach { assertThat(result).contains(it) }
    }

    @ParameterizedTest
    @MethodSource("provideDiscountsForTotalDiscountAmount")
    @DisplayName("혜택 목록이 주어지면 총 혜택 금액을 계산")
    fun calculatedTotalDiscountAmount_discountsGiven_calculateTotalDiscountAmountCorrectly(
        discounts: List<Discount>,
        expected: Int
    ) {
        // when
        val result = discounts.calculatedTotalDiscountAmount()

        // then
        assertThat(result).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        private fun provideMenusAndAmountsForToCalculateDiscounts(): Stream<Arguments> = Stream.of(
            Arguments.of(listOf(Menu.TAPAS to 1, Menu.ZERO_COKE to 1), 26, emptyList<Discount>()),
            Arguments.of(
                listOf(Menu.SEAFOOD_PASTA to 2, Menu.RED_WINE to 1, Menu.CHOCOLATE_CAKE to 1),
                25,
                listOf(
                    Discount.Christmas(discountAmount = -3_400),
                    Discount.Weekday(discountAmount = -2_023),
                    Discount.StarDay,
                    Discount.Giveaway
                )
            ),
            Arguments.of(
                listOf(
                    Menu.T_BONE_STEAK to 1,
                    Menu.BBQ_RIB to 1,
                    Menu.CHOCOLATE_CAKE to 2,
                    Menu.ZERO_COKE to 1
                ),
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
        private fun provideDiscountsForTotalDiscountAmount(): Stream<Arguments> = Stream.of(
            Arguments.of(emptyList<Discount>(), 0),
            Arguments.of(
                listOf(
                    Discount.Christmas(discountAmount = -1_200),
                    Discount.Weekday(discountAmount = -4_046),
                    Discount.StarDay,
                    Discount.Giveaway
                ),
                -31_246
            ),
            Arguments.of(
                listOf(
                    Discount.Christmas(discountAmount = -3_400),
                    Discount.Weekday(discountAmount = -2_023),
                    Discount.StarDay,
                    Discount.Giveaway
                ),
                -31_423
            )
        )
    }
}