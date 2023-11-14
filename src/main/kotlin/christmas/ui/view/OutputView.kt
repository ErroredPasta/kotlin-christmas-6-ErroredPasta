package christmas.ui.view

import christmas.domain.model.Menu

interface OutputView {
    fun displayMessage(message: String)
    fun displayErrorMessage(message: String?)
    fun displayMenusAndAmounts(menusAndAmounts: List<Pair<Menu, Int>>)

    companion object {
        const val WELCOME_MESSAGE = "안녕하세요! 우테코 식당 12월 이벤트 플래너입니다."
        const val ERROR_MESSAGE_PREFIX = "[ERROR]"
        const val DEFAULT_ERROR_MESSAGE = "에러가 발생했습니다. 다시 시도해 주세요."
        const val MENU_AND_AMOUNT_FORMAT = "%s %d개"
        const val PREVIEW_BENEFITS_MESSAGE = "12월 26일에 우테코 식당에서 받을 이벤트 혜택 미리 보기!\n"
        const val ORDERED_MENUS_TITLE = "<주문 메뉴>"
    }
}