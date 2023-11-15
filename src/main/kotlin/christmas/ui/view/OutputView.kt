package christmas.ui.view

import christmas.domain.model.Discount
import christmas.domain.model.Menu

interface OutputView {
    fun displayMessage(message: String)
    fun displayErrorMessage(message: String?)
    fun displayMenusAndAmounts(menusAndAmounts: List<Pair<Menu, Int>>)
    fun displayDiscountNotAppliedTotalPrice(totalPrice: Int)
    fun displayShouldGiveaway(shouldGiveaway: Boolean)
    fun displayDiscounts(discounts: List<Discount>)

    companion object {
        const val WELCOME_MESSAGE = "안녕하세요! 우테코 식당 12월 이벤트 플래너입니다."
        const val ERROR_MESSAGE_PREFIX = "[ERROR]"
        const val DEFAULT_ERROR_MESSAGE = "에러가 발생했습니다. 다시 시도해 주세요."
        const val MENU_AND_AMOUNT_FORMAT = "%s %d개"
        const val PREVIEW_BENEFITS_MESSAGE = "12월 26일에 우테코 식당에서 받을 이벤트 혜택 미리 보기!\n"
        const val ORDERED_MENUS_TITLE = "<주문 메뉴>"
        const val DISCOUNT_NOT_APPLIED_TOTAL_PRICE_TITLE = "<할인 전 총주문 금액>"
        const val PRICE_FORMAT = "%,d원"
        const val GIVEAWAY_TITLE = "<증정 메뉴>"
        val giveawayMenu = Menu.CHAMPAGNE to 1
        const val NOTHING = "없음"
        const val BENEFITS_TITLE = "<혜택 내역>"
        const val CHRISTMAS_DISCOUNT_NAME = "크리스마스 디데이 할인"
        const val WEEKDAY_DISCOUNT_NAME = "평일 할인"
        const val WEEKEND_DISCOUNT_NAME = "주말 할인"
        const val STAR_DAY_DISCOUNT_NAME = "특별 할인"
        const val GIVEAWAY_DISCOUNT_NAME = "증정 이벤트"
        const val DISCOUNT_FORMAT = "%s: -%,d원"
    }
}