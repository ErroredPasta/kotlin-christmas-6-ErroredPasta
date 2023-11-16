package christmas.domain.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class BadgeTest {
    @ParameterizedTest
    @MethodSource("provideDiscountAmountForBadge")
    @DisplayName("총 혜택 금액이 주어질 경우 제공할 배지를 올바르게 return")
    fun getBadgeByTotalDiscountAmount_totalDiscountAmountGiven_returnCorrectBadge(
        totalDiscountAmount: Int,
        expected: Badge
    ) {
        // when
        val result = Badge.getBadgeByTotalDiscountAmount(totalDiscountAmount = totalDiscountAmount)

        // then
        assertThat(result).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        private fun provideDiscountAmountForBadge(): Stream<Arguments> = Stream.of(
            Arguments.of(1, Badge.NOTHING),
            Arguments.of(0, Badge.NOTHING),
            Arguments.of(-1, Badge.NOTHING),
            Arguments.of(-4_999, Badge.NOTHING),
            Arguments.of(-5_000, Badge.STAR),
            Arguments.of(-5_001, Badge.STAR),
            Arguments.of(-9_999, Badge.STAR),
            Arguments.of(-10_000, Badge.TREE),
            Arguments.of(-10_001, Badge.TREE),
            Arguments.of(-19_999, Badge.TREE),
            Arguments.of(-20_000, Badge.SANTA),
            Arguments.of(-20_001, Badge.SANTA)
        )
    }
}