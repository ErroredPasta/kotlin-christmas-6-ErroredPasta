package christmas.domain.logic

private const val GIVEAWAY_PRICE_THRESHOLD = 120_000

fun decideOnGiveaway(totalPrice: Int): Boolean = totalPrice >= GIVEAWAY_PRICE_THRESHOLD
