package christmas.domain.logic

import christmas.domain.model.Menu


object MenuValidator {
    const val DUPLICATED_MENU_MESSAGE = "유효하지 않은 주문입니다. 다시 입력해 주세요."
    const val MIN_AMOUNT = 1
    const val INVALID_AMOUNT_MESSAGE = "유효하지 않은 주문입니다. 다시 입력해 주세요."
    const val MAX_SUM_OF_AMOUNTS = 20
    const val TOO_MANY_AMOUNTS_MESSAGE = "한번에 최대 ${MAX_SUM_OF_AMOUNTS}개까지 주문이 가능합니다. 다시 입력해 주세요."
    const val ONLY_DRINKS_ORDER_MESSAGE = "음료만 주문은 불가능합니다. 다시 입력해 주세요."

    fun validate(menusAndAmounts: List<Pair<Menu, Int>>) {
        require(!menusAndAmounts.hasDuplicatedMenus()) { DUPLICATED_MENU_MESSAGE }
        require(menusAndAmounts.all { it.second >= MIN_AMOUNT }) { INVALID_AMOUNT_MESSAGE }

        val sumOfAmounts = menusAndAmounts.sumOf { it.second }
        require(sumOfAmounts <= MAX_SUM_OF_AMOUNTS) { TOO_MANY_AMOUNTS_MESSAGE }

        require(!menusAndAmounts.hasOnlyDrinks()) { ONLY_DRINKS_ORDER_MESSAGE }
    }

    private fun List<Pair<Menu, Int>>.hasDuplicatedMenus(): Boolean {
        val menus = this.map { it.first }
        return menus.toSet().size != menus.size
    }

    private fun List<Pair<Menu, Int>>.hasOnlyDrinks(): Boolean {
        val menus = this.map { it.first }
        return menus.all { it.type == Menu.Type.DRINK }
    }
}