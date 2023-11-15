package christmas.domain.logic

import christmas.domain.model.Discount
import christmas.domain.model.Menu
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit

object DiscountCalculator {
    private const val DISCOUNT_PRICE_THRESHOLD = 10_000
    const val YEAR = 2023
    const val MONTH = 12
    private const val CHRISTMAS_DAY = 25
    private val christmas = LocalDate.of(YEAR, MONTH, CHRISTMAS_DAY)
    private val beginningOfDecember = LocalDate.of(YEAR, MONTH, 1)

    fun calculateDiscounts(
        menusAndAmounts: List<Pair<Menu, Int>>,
        date: LocalDate,
    ): List<Discount> {
        val totalPrice = menusAndAmounts.calculateTotalPrice()
        if (totalPrice < DISCOUNT_PRICE_THRESHOLD) return emptyList()

        val discounts = mutableListOf<Discount>()

        if (date <= christmas) discounts.addChristmasDiscount(date = date)
        if (date.isWeekend()) discounts.addWeekendDiscount(menusAndAmounts = menusAndAmounts)
        if (!date.isWeekend()) discounts.addWeekdayDiscount(menusAndAmounts = menusAndAmounts)
        if (date.isStarDay()) discounts.add(Discount.StarDay)
        if (decideOnGiveaway(totalPrice = totalPrice)) discounts.add(Discount.Giveaway)

        return discounts
    }

    fun List<Discount>.calculatedTotalDiscountAmount(): Int = this.sumOf { it.discountAmount }

    private fun MutableList<Discount>.addChristmasDiscount(date: LocalDate) {
        val daysFromBeginning = ChronoUnit.DAYS.between(beginningOfDecember, date).toInt()
        val discountAmount =
            Discount.DEFAULT_CHRISTMAS_DISCOUNT_AMOUNT + Discount.CHRISTMAS_DISCOUNT_AMOUNT_STEP * daysFromBeginning
        add(Discount.Christmas(discountAmount = discountAmount))
    }

    private fun MutableList<Discount>.addWeekendDiscount(menusAndAmounts: List<Pair<Menu, Int>>) {
        val mainMenuAmounts = menusAndAmounts.sumOf { (menu, amount) -> if (menu.type == Menu.Type.MAIN) amount else 0 }
        if (mainMenuAmounts == 0) return
        add(Discount.Weekend(discountAmount = Discount.WEEKEND_DISCOUNT_AMOUNT * mainMenuAmounts))
    }

    private fun MutableList<Discount>.addWeekdayDiscount(menusAndAmounts: List<Pair<Menu, Int>>) {
        val dessertMenuAmounts =
            menusAndAmounts.sumOf { (menu, amount) -> if (menu.type == Menu.Type.DESSERT) amount else 0 }
        if (dessertMenuAmounts == 0) return
        add(Discount.Weekday(discountAmount = Discount.WEEKDAY_DISCOUNT_AMOUNT * dessertMenuAmounts))
    }

    private fun LocalDate.isWeekend(): Boolean =
        this.dayOfWeek == DayOfWeek.FRIDAY || this.dayOfWeek == DayOfWeek.SATURDAY

    // 25일을 제외한 별이 달린 날은 모두 일요일
    private fun LocalDate.isStarDay(): Boolean = this.dayOfWeek == DayOfWeek.SUNDAY || this == christmas
}

