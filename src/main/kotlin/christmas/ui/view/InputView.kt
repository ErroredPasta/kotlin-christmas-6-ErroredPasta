package christmas.ui.view

interface InputView {
    fun getDayOfMonth(): Int
    fun getMenusAndAmounts(): List<String>

    companion object {
        const val ASK_VISIT_DATE_MESSAGE = "12월 중 식당 예상 방문 날짜는 언제인가요? (숫자만 입력해 주세요!)"
        const val INVALID_DATE_MESSAGE = "유효하지 않은 날짜입니다. 다시 입력해 주세요."
        const val ENTER_MENUS_AND_AMOUNTS_MESSAGE = "주문하실 메뉴를 메뉴와 개수를 알려 주세요. (e.g. 해산물파스타-2,레드와인-1,초코케이크-1)"
        const val MENU_DELIMITER = ','
    }
}