package christmas.ui.view

import christmas.domain.model.Menu
import java.time.LocalDate

interface InputView {
    fun getDate(): LocalDate
    fun getMenusAndAmounts(): List<Pair<Menu, Int>>

    companion object {
        const val YEAR = 2023
        const val MONTH = 12

        const val ASK_VISIT_DATE_MESSAGE = "12월 중 식당 예상 방문 날짜는 언제인가요? (숫자만 입력해 주세요!)"
        const val INVALID_DATE_MESSAGE = "유효하지 않은 날짜입니다. 다시 입력해 주세요."
        const val ENTER_MENUS_AND_AMOUNTS_MESSAGE = "주문하실 메뉴를 메뉴와 개수를 알려 주세요. (e.g. 해산물파스타-2,레드와인-1,초코케이크-1)"
        const val MENU_DELIMITER = ','
        const val MENU_AMOUNT_DIVIDER = '-'
        const val INVALID_ORDER_MESSAGE = "유효하지 않은 주문입니다. 다시 입력해 주세요."
    }
}