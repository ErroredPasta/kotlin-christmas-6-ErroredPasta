package christmas.ui.view

import christmas.domain.logic.giveawayMenu
import christmas.domain.model.Badge
import christmas.domain.model.Discount
import christmas.domain.model.Menu

class ConsoleOutputView : OutputView {
    override fun displayMessage(message: String) {
        println(message)
    }

    override fun displayErrorMessage(message: String?) {
        println("${OutputView.ERROR_MESSAGE_PREFIX} ${message ?: OutputView.DEFAULT_ERROR_MESSAGE}")
    }

    override fun displayMenusAndAmounts(menusAndAmounts: List<Pair<Menu, Int>>) {
        println(OutputView.ORDERED_MENUS_TITLE)
        menusAndAmounts.forEach { menuAndAmount ->
            val (menu, amount) = menuAndAmount
            println(OutputView.MENU_AND_AMOUNT_FORMAT.format(menu.menuName, amount))
        }
    }

    override fun displayDiscountNotAppliedTotalPrice(totalPrice: Int) {
        println(OutputView.DISCOUNT_NOT_APPLIED_TOTAL_PRICE_TITLE)
        println(OutputView.KRW_FORMAT.format(totalPrice))
    }

    override fun displayShouldGiveaway(shouldGiveaway: Boolean) {
        println(OutputView.GIVEAWAY_TITLE)

        val giveawayMenuMessage = if (shouldGiveaway) {
            val (menu, amount) = giveawayMenu
            OutputView.MENU_AND_AMOUNT_FORMAT.format(menu.menuName, amount)
        } else {
            OutputView.NOTHING
        }

        println(giveawayMenuMessage)
    }

    override fun displayDiscounts(discounts: List<Discount>) {
        println(OutputView.BENEFITS_TITLE)

        if (discounts.isEmpty()) {
            println(OutputView.NOTHING)
            return
        }

        discounts.forEach { discount ->
            println(OutputView.DISCOUNT_FORMAT.format(discount.name, discount.discountAmount))
        }
    }

    private val Discount.name: String
        get() = when (this) {
            is Discount.Christmas -> OutputView.CHRISTMAS_DISCOUNT_NAME
            is Discount.Weekday -> OutputView.WEEKDAY_DISCOUNT_NAME
            is Discount.Weekend -> OutputView.WEEKEND_DISCOUNT_NAME
            is Discount.StarDay -> OutputView.STAR_DAY_DISCOUNT_NAME
            is Discount.Giveaway -> OutputView.GIVEAWAY_DISCOUNT_NAME
        }

    override fun displayTotalDiscountAmount(totalDiscountAmount: Int) {
        println(OutputView.TOTAL_DISCOUNT_TITLE)
        println(OutputView.KRW_FORMAT.format(totalDiscountAmount))
    }

    override fun displayDiscountAppliedTotalPrice(totalPrice: Int) {
        println(OutputView.DISCOUNT_APPLIED_TITLE)
        println(OutputView.KRW_FORMAT.format(totalPrice))
    }

    override fun displayBadge(badge: Badge) {
        println(OutputView.BADGE_TITLE)
        println(badge.badgeName)
    }

    private val Badge.badgeName: String
        get() = when (this) {
            Badge.NOTHING -> OutputView.NOTHING
            Badge.STAR -> OutputView.STAR_BADGE_NAME
            Badge.TREE -> OutputView.TREE_BADGE_NAME
            Badge.SANTA -> OutputView.SANTA_BADGE_NAME
        }
}