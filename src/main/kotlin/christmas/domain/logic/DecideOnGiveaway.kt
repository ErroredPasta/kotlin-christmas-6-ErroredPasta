package christmas.domain.logic

import christmas.domain.model.Menu

private const val GIVEAWAY_PRICE_THRESHOLD = 120_000
val giveawayMenu = Menu.CHAMPAGNE to 1


fun decideOnGiveaway(totalPrice: Int): Boolean = totalPrice >= GIVEAWAY_PRICE_THRESHOLD
