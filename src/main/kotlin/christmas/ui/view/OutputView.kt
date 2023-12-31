package christmas.ui.view

import christmas.domain.model.Badge
import christmas.domain.model.Discount
import christmas.domain.model.Menu

interface OutputView {
    fun displayMessage(message: String)
    fun displayErrorMessage(message: String?)
    fun displayMenusAndAmounts(menusAndAmounts: List<Pair<Menu, Int>>)
    fun displayDiscountNotAppliedTotalPrice(totalPrice: Int)
    fun displayShouldGiveaway(shouldGiveaway: Boolean)
    fun displayDiscounts(discounts: List<Discount>)
    fun displayTotalDiscountAmount(totalDiscountAmount: Int)
    fun displayDiscountAppliedTotalPrice(totalPrice: Int)
    fun displayBadge(badge: Badge)

    companion object {
        const val WELCOME_MESSAGE = "안녕하세요! 우테코 식당 12월 이벤트 플래너입니다."
        const val ERROR_MESSAGE_PREFIX = "[ERROR]"
        const val DEFAULT_ERROR_MESSAGE = "에러가 발생했습니다. 다시 시도해 주세요."
        const val MENU_AND_AMOUNT_FORMAT = "%s %d개"
        const val PREVIEW_BENEFITS_MESSAGE = "12월 26일에 우테코 식당에서 받을 이벤트 혜택 미리 보기!\n"
        const val ORDERED_MENUS_TITLE = "<주문 메뉴>"
        const val DISCOUNT_NOT_APPLIED_TOTAL_PRICE_TITLE = "<할인 전 총주문 금액>"
        const val KRW_FORMAT = "%,d원"
        const val GIVEAWAY_TITLE = "<증정 메뉴>"
        const val NOTHING = "없음"
        const val BENEFITS_TITLE = "<혜택 내역>"
        const val CHRISTMAS_DISCOUNT_NAME = "크리스마스 디데이 할인"
        const val WEEKDAY_DISCOUNT_NAME = "평일 할인"
        const val WEEKEND_DISCOUNT_NAME = "주말 할인"
        const val STAR_DAY_DISCOUNT_NAME = "특별 할인"
        const val GIVEAWAY_DISCOUNT_NAME = "증정 이벤트"
        const val DISCOUNT_FORMAT = "%s: %,d원"
        const val TOTAL_DISCOUNT_TITLE = "<총혜택 금액>"
        const val DISCOUNT_APPLIED_TITLE = "<할인 후 예상 결제 금액>"
        const val BADGE_TITLE = "<12월 이벤트 배지>"
        const val STAR_BADGE_NAME = "별"
        const val TREE_BADGE_NAME = "트리"
        const val SANTA_BADGE_NAME = "산타"
    }
}