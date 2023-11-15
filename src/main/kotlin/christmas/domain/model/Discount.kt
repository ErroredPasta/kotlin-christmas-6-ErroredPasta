package christmas.domain.model

sealed class Discount(open val discountAmount: Int) {
    data class Christmas(override val discountAmount: Int) : Discount(discountAmount)
    data class Weekday(override val discountAmount: Int) : Discount(discountAmount)
    data class Weekend(override val discountAmount: Int) : Discount(discountAmount)
    data object StarDay : Discount(STAR_DAY_DISCOUNT_AMOUNT)
    data object Giveaway : Discount(Menu.CHAMPAGNE.price)

    companion object {
        const val STAR_DAY_DISCOUNT_AMOUNT = 1_000
        const val DEFAULT_CHRISTMAS_DISCOUNT_AMOUNT = 1_000
        const val CHRISTMAS_DISCOUNT_AMOUNT_STEP = 100
        const val WEEKDAY_DISCOUNT_AMOUNT = 2_023
        const val WEEKEND_DISCOUNT_AMOUNT = 2_023
    }
}