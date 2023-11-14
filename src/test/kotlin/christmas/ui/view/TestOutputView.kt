package christmas.ui.view

import christmas.domain.model.Menu

class TestOutputView : OutputView {
    val capturedTexts = mutableListOf<String>()

    override fun displayMessage(message: String) {
        capturedTexts.add(message)
    }

    override fun displayErrorMessage(message: String?) {
        if (message == null) return
        capturedTexts.add(message)
    }

    override fun displayMenusAndAmounts(menusAndAmounts: List<Pair<Menu, Int>>) {
//        TODO("Not yet implemented")
    }

    override fun displayDiscountNotAppliedTotalPrice(totalPrice: Int) {
//        TODO("Not yet implemented")
    }

    override fun displayShouldGiveaway(shouldGiveaway: Boolean) {
//        TODO("Not yet implemented")
    }
}