package christmas.domain.logic

import christmas.domain.model.Menu

fun List<Pair<Menu, Int>>.hasDuplicatedMenus(): Boolean {
    val menus = this.map { it.first }
    return menus.toSet().size != menus.size
}