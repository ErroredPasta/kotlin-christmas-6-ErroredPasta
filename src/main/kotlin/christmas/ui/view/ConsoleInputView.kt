package christmas.ui.view

import camp.nextstep.edu.missionutils.Console

class ConsoleInputView : InputView {
    override fun getDayOfMonth(): Int {
        println(InputView.ASK_VISIT_DATE_MESSAGE)
        return readInt()
    }

    override fun getMenusAndAmounts(): List<String> {
        println(InputView.ENTER_MENUS_AND_AMOUNTS_MESSAGE)
        return Console.readLine().split(InputView.MENU_DELIMITER)
    }

    private fun readInt(): Int = requireNotNull(Console.readLine().toIntOrNull()) { InputView.INVALID_DATE_MESSAGE }
}