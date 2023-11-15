package christmas.domain.model

enum class Badge(val priceThreshold: Int) {
    NOTHING(priceThreshold = 0),
    STAR(priceThreshold = -5_000),
    TREE(priceThreshold = -10_000),
    SANTA(priceThreshold = -20_000);

    companion object {
        fun getBadgeByTotalDiscountAmount(totalDiscountAmount: Int): Badge =
            entries.sortedBy { it.priceThreshold }.find { it.priceThreshold >= totalDiscountAmount } ?: NOTHING
    }
}