package christmas.domain.logic

import christmas.domain.model.Menu

fun List<Pair<Menu, Int>>.calculateTotalPrice(): Int = sumOf { (menu, amount) ->
    menu.price * amount
}