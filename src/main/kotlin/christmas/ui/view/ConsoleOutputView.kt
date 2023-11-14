package christmas.ui.view

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
        println(OutputView.PRICE_FORMAT.format(totalPrice))
    }
}