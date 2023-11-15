package christmas.domain.model

import christmas.domain.exception.MenuNotExistException

enum class Menu(val menuName: String, val price: Int, val type: Type) {
    BUTTON_MUSHROOM_SOUP(menuName = "양송이수프", price = 6_000, type = Type.APPETIZER),
    TAPAS(menuName = "타파스", price = 5_500, type = Type.APPETIZER),
    CAESAR_SALAD(menuName = "시저샐러드", price = 8_000, type = Type.APPETIZER),

    T_BONE_STEAK(menuName = "티본스테이크", price = 55_000, type = Type.MAIN),
    BBQ_RIB(menuName = "바비큐립", price = 54_000, type = Type.MAIN),
    SEAFOOD_PASTA(menuName = "해산물파스타", price = 35_000, type = Type.MAIN),
    CHRISTMAS_PASTA(menuName = "크리스마스파스타", price = 25_000, type = Type.MAIN),

    CHOCOLATE_CAKE(menuName = "초코케이크", price = 15_000, type = Type.DESSERT),
    ICE_CREAM(menuName = "아이스크림", price = 5_000, type = Type.DESSERT),

    ZERO_COKE(menuName = "제로콜라", price = 3_000, type = Type.DRINK),
    RED_WINE(menuName = "레드와인", price = 60_000, type = Type.DRINK),
    CHAMPAGNE(menuName = "샴페인", price = 25_000, type = Type.DRINK);

    enum class Type {
        APPETIZER, MAIN, DESSERT, DRINK
    }

    companion object {
        const val MENU_NOT_EXIST_MESSAGE = "존재하지 않는 메뉴가 있습니다. 다시 입력해 주세요."

        fun getMenuByMenuName(menuName: String): Menu =
            entries.find { it.menuName == menuName } ?: throw MenuNotExistException(MENU_NOT_EXIST_MESSAGE)
    }
}