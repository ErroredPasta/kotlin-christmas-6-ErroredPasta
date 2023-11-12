package christmas.ui.view

import camp.nextstep.edu.missionutils.Console
import christmas.domain.model.Menu
import java.time.DateTimeException
import java.time.LocalDate

class ConsoleInputView : InputView {
    override fun getDate(): LocalDate {
        println(InputView.ASK_VISIT_DATE_MESSAGE)

        return runCatching {
            LocalDate.of(InputView.YEAR, InputView.MONTH, readInt())
        }.onFailure { error ->
            if (error is DateTimeException) throw IllegalArgumentException(InputView.INVALID_DATE_MESSAGE)
        }.getOrThrow()
    }

    override fun getMenusAndAmounts(): List<Pair<Menu, Int>> {
        println(InputView.ENTER_MENUS_AND_AMOUNTS_MESSAGE)

        return runCatching {
            Console.readLine().split(InputView.MENU_DELIMITER).map {
                val (menuName, amount) = it.split(InputView.MENU_AMOUNT_DIVIDER)
                Menu.getMenuByMenuName(menuName = menuName) to amount.toInt()
            }
        }.onFailure { error ->
            if (error is IndexOutOfBoundsException) throw IllegalArgumentException(InputView.INVALID_ORDER_MESSAGE)
            if (error is NumberFormatException) throw IllegalArgumentException(InputView.INVALID_ORDER_MESSAGE)
        }.getOrThrow()
    }

    private fun readInt(): Int = requireNotNull(Console.readLine().toIntOrNull()) { InputView.INVALID_DATE_MESSAGE }
}