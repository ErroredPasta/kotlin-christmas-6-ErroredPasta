package christmas.domain.logic

import christmas.domain.model.Menu
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class CalculateTotalPriceKtTest {

    @ParameterizedTest
    @MethodSource("provideMenusAndAmounts")
    @DisplayName("메뉴가 주어질 경우 총 금액을 계산")
    fun calculateTotalPrice_calculateTotalPriceCorrectly(menusAndAmounts: List<Pair<Menu, Int>>, expected: Int) {
        // when
        val result = menusAndAmounts.calculateTotalPrice()

        // then
        assertThat(result).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        private fun provideMenusAndAmounts(): Stream<Arguments> = Stream.of(
            Arguments.of(listOf(Menu.BUTTON_MUSHROOM_SOUP to 2), 12_000),
            Arguments.of(listOf(Menu.TAPAS to 1, Menu.ZERO_COKE to 1), 8_500),
            Arguments.of(
                listOf(
                    Menu.T_BONE_STEAK to 1,
                    Menu.BBQ_RIB to 1,
                    Menu.CHOCOLATE_CAKE to 2,
                    Menu.ZERO_COKE to 1
                ), 142_000
            ),
        )
    }
}